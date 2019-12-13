package com.github.devoxx.formvalidation

import java.time.LocalDateTime

fun main() {
    val firstForm = UserCreation(
            firstName = "Pedro",
            lastName = "Gómez",
            birthday = LocalDateTime.MIN,
            documentId = "48632500A",
            phoneNumber = "677673299",
            email = "pedro@karumi.com"
    )
    val firstFormValidationResult = UserValidation.validateUserCreation(LocalDateTime.now(), firstForm)
    println("First form validation result $firstFormValidationResult")
    firstFormValidationResult.fold(
            { println("Error found in the first form $it") },
            { println("The first form is valid") }
    )

    val secondForm = UserCreation(
            firstName = "",
            lastName = "     ",
            birthday = LocalDateTime.MIN,
            documentId = "48632592",
            phoneNumber = "6777",
            email = "pg"
    )
    val secondFormValidationResult = UserValidation.validateUserCreation(LocalDateTime.now(), secondForm)
    println("First form validation result $secondFormValidationResult")
    secondFormValidationResult.fold(
            { println("Error found in the second form $it") },
            { println("The second form is valid") }
    )
}
