package ru.santaev.techtask.feature.gallery.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.santaev.techtask.factory.UserFactory
import ru.santaev.techtask.feature.gallery.domain.UserInteractor
import ru.santaev.techtask.feature.gallery.ui.bus.UserListChangedEventBus
import ru.santaev.techtask.feature.gallery.ui.entity.UserUiEntity
import ru.santaev.techtask.utils.MainDispatcherInitializerRule

@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherInitializerRule = MainDispatcherInitializerRule()

    @RelaxedMockK
    private lateinit var userInteractor: UserInteractor

    private lateinit var userListChangedEventBus: UserListChangedEventBus
    private lateinit var viewModel: UsersViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        userListChangedEventBus = UserListChangedEventBus()
    }

    @Test
    fun `should show correct list of users`() = runBlockingTest {
        // Arrange
        createViewModel()
        val users = listOf(
            UserFactory.createUser(id = 0),
            UserFactory.createUser(id = 1)
        )
        coEvery { userInteractor.getUsers() } returns users
        // Act
        var savedUsers: List<UserUiEntity>? = null
        val usersObserver: suspend (List<UserUiEntity>) -> Unit = { savedUsers = it }
        val job = launch { viewModel.users.collect(usersObserver) }
        mainDispatcherInitializerRule.dispatcher.advanceUntilIdle()
        // Assert
        savedUsers?.get(0)?.id shouldBe users[0].id
        savedUsers?.get(1)?.id shouldBe users[1].id
        job.cancel()
    }

    @Test
    fun `should call delete method in repository when deleting was confirmed`() = runBlockingTest {
        // Arrange
        createViewModel()
        // Act
        val userIdToDelete = 5L
        viewModel.onUserLongClicked(userIdToDelete)
        viewModel.onUserDeletingConfirmed()
        advanceUntilIdle()
        // Assert
        coVerifySequence {
            userInteractor.deleteUser(eq(userIdToDelete))
        }
    }

    @Test
    fun `should reload users if user change event was received`() = runBlockingTest {
        // Arrange
        createViewModel()
        // Act
        val usersObserver = mockk<suspend (List<UserUiEntity>) -> Unit>(relaxed = true)
        val job = launch { viewModel.users.collect(usersObserver) }
        userListChangedEventBus.notifyUserListChanged()
        mainDispatcherInitializerRule.dispatcher.advanceUntilIdle()
        // Assert
        coVerifySequence {
            userInteractor.getUsers()
            userInteractor.getUsers()
        }
        job.cancel()
    }

    private fun createViewModel() {
        viewModel = UsersViewModel(userInteractor, userListChangedEventBus)
    }
}
