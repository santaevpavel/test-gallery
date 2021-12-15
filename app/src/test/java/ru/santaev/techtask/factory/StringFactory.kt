package ru.santaev.techtask.factory

import kotlin.random.Random

object StringFactory {

    private val random: Random
        get() = Random
    private val alphaNumericChars = ('0'..'9') + ('A'..'Z') + ('a'..'z')

    fun create(
        chars: List<Char> = alphaNumericChars,
        length: Int = 10
    ): String {
        return (0 until length)
            .asSequence()
            .map { chars[random.nextInt(0, chars.size)] }
            .joinToString("")
    }
}
