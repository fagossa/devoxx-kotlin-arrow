package com.github.devoxx.formvalidator

import com.github.devoxx.formvalidation.UserCreation
import io.kotlintest.properties.Gen
import java.time.LocalDateTime

object Generators {

    class FormGeneartor(private val referenceDate: LocalDateTime) : Gen<UserCreation> {
        override fun constants(): Iterable<UserCreation> = emptyList()

        private val nonEmptyStringGen = { Gen.string().random().filter { it.isNotEmpty() } }
        private val nomEmptyStringWithLetters = { nonEmptyStringGen().filter { it.any { char -> char.isLetter() } } }

        override fun random(): Sequence<UserCreation> =
                generateSequence {
                    UserCreation(
                            firstName = nonEmptyStringGen().first(),
                            lastName = nonEmptyStringGen().first(),
                            birthday = referenceDate.minusYears(Gen.choose(18L, 1000L).random().first()),
                            documentId = "${Gen.choose(10000000, 99999999).random().first()}${nomEmptyStringWithLetters().first().first { it.isLetter() }}",
                            phoneNumber = "${Gen.choose(100000000, 999999999).random().first()}",
                            email = "${nonEmptyStringGen().first()}@${nonEmptyStringGen().first()}.${Gen.oneOf(Gen.constant("com"), Gen.constant("es"), Gen.constant("net")).random().first()}}"
                    )
                }
    }
}
