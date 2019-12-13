package com.github.devoxx.formvalidator

import arrow.core.NonEmptyList
import arrow.core.invalid

import com.github.devoxx.formvalidation.UserCreation
import com.github.devoxx.formvalidation.UserCreationError
import com.github.devoxx.formvalidation.UserValidation

import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.properties.forAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.time.LocalDateTime

class UserValidationSpec : StringSpec({
    "should not validate an empty form where every field is wrong" {
        val form = UserCreation(
                firstName = "",
                lastName = "  ",
                birthday = LocalDateTime.now(),
                documentId = "48632500",
                phoneNumber = "6799",
                email = "pedro"
        )

        val result = UserValidation.validateUserCreation(LocalDateTime.now(), form)

        result.isInvalid.shouldBeTrue()
        result.shouldBe(
                NonEmptyList.of(
                        UserCreationError.InvalidEmail(form.email),
                        UserCreationError.InvalidPhoneNumber(form.phoneNumber),
                        UserCreationError.InvalidDocumentId(form.documentId),
                        UserCreationError.UserTooYoung(form.birthday),
                        UserCreationError.EmptyFirstName(form.firstName),
                        UserCreationError.EmptyLastName(form.lastName)
                ).invalid()
        )
    }

    "should not validate any form where just one field is invalid" {
        val form = UserCreation(
                firstName = "Pedro",
                lastName = "Gómez",
                birthday = LocalDateTime.MIN,
                documentId = "48632500A",
                phoneNumber = "677673299",
                email = "p"
        )

        val result = UserValidation.validateUserCreation(LocalDateTime.now(), form)

        result.isInvalid.shouldBeTrue()
        result.shouldBe(
                NonEmptyList.of(
                        UserCreationError.InvalidEmail(form.email)
                ).invalid()
        )
    }

    "should validate any form where all the fields are correct" {
        val referenceDate = LocalDateTime.now()
        forAll(Generators.FormGeneartor(referenceDate)) { form ->
            UserValidation.validateUserCreation(referenceDate, form).isValid
        }
    }
})
