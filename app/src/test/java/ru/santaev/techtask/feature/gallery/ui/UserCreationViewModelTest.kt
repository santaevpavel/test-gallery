package ru.santaev.techtask.feature.gallery.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.santaev.techtask.feature.gallery.domain.UserInteractor
import ru.santaev.techtask.feature.gallery.ui.bus.UserListChangedEventBus
import ru.santaev.techtask.utils.Event
import ru.santaev.techtask.utils.MainDispatcherInitializerRule

@OptIn(ExperimentalCoroutinesApi::class)
class UserCreationViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherInitializerRule = MainDispatcherInitializerRule()

    @RelaxedMockK
    private lateinit var userInteractor: UserInteractor
    private lateinit var userListChangedEventBus: UserListChangedEventBus

    private lateinit var viewModel: UserCreationViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        userListChangedEventBus = UserListChangedEventBus()
    }

    @Test
    fun `should add button be disabled if name field was not filled`() = runBlockingTest {
        // Arrange
        createViewModel()
        // Act
        val isAddButtonEnabledObserver = mockk<suspend (Boolean) -> Unit>(relaxed = true)
        val job = launch { viewModel.isAddButtonEnabled.collect(isAddButtonEnabledObserver) }
        viewModel.email.value = "email@email.com"
        advanceUntilIdle()
        // Assert
        coVerifySequence {
            isAddButtonEnabledObserver.invoke(false)
        }
        job.cancel()
    }

    @Test
    fun `should add button be disabled if email field was not filled`() = runBlockingTest {
        // Arrange
        createViewModel()
        // Act
        val isAddButtonEnabledObserver = mockk<suspend (Boolean) -> Unit>(relaxed = true)
        val job = launch { viewModel.isAddButtonEnabled.collect(isAddButtonEnabledObserver) }
        viewModel.name.value = "Barak Obama"
        advanceUntilIdle()
        // Assert
        coVerifySequence {
            isAddButtonEnabledObserver.invoke(false)
        }
        job.cancel()
    }

    @Test
    fun `should add button be enabled if email and name were filled correctly`() = runBlockingTest {
        // Arrange
        createViewModel()
        // Act
        val isAddButtonEnabledObserver = mockk<suspend (Boolean) -> Unit>(relaxed = true)
        val job = launch { viewModel.isAddButtonEnabled.collect(isAddButtonEnabledObserver) }
        viewModel.name.value = "Barak Obama"
        viewModel.email.value = "email@email.com"
        advanceUntilIdle()
        // Assert
        coVerifySequence {
            isAddButtonEnabledObserver.invoke(false)
            isAddButtonEnabledObserver.invoke(true)
        }
        job.cancel()
    }

    @Test
    fun `should progress be enabled if new user is being added`() = runBlockingTest {
        // Arrange
        userInteractor = mockk {
            coEvery { createUser(any(), any()) } coAnswers { delay(1000L) }
        }
        createViewModel()

        // Act
        val progressObserver = mockk<suspend (Boolean) -> Unit>(relaxed = true)
        val job = launch { viewModel.progress.collect(progressObserver) }
        viewModel.name.value = "Barak Obama"
        viewModel.email.value = "email@email.com"
        viewModel.onAddClicked()
        mainDispatcherInitializerRule.dispatcher.advanceTimeBy(2000L)
        // advanceTimeBy(2000L)
        // Assert
        coVerifySequence {
            progressObserver.invoke(false)
            progressObserver.invoke(true)
            progressObserver.invoke(false)
        }
        job.cancel()
    }

    @Test
    fun `should toast be shown if new user throws error`() = runBlockingTest {
        // Arrange
        userInteractor = mockk {
            coEvery { createUser(any(), any()) } throws RuntimeException("Error")
        }
        createViewModel()
        // Act
        val toastObserver = mockk<suspend (Event<*>) -> Unit>(relaxed = true)
        val job = launch { viewModel.toast.collect(toastObserver) }
        viewModel.name.value = "Barak Obama"
        viewModel.email.value = "email@email.com"
        viewModel.onAddClicked()
        mainDispatcherInitializerRule.dispatcher.advanceUntilIdle()
        // Assert
        coVerifySequence {
            toastObserver.invoke(any())
        }
        job.cancel()
    }

    private fun createViewModel() {
        viewModel = UserCreationViewModel(userInteractor, userListChangedEventBus)
    }
}
