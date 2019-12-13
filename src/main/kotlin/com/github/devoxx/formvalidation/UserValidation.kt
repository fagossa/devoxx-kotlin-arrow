package com.github.devoxx.formvalidation

import arrow.core.*
import arrow.core.extensions.nonemptylist.semigroup.semigroup
import arrow.core.extensions.validated.applicative.applicative
import java.time.LocalDateTime

typealias FormValidationResult<T> = Validated<NonEmptyList<UserCreationError>, T>
typealias ErrorOr<T> = Either<NonEmptyList<UserCreationError>, T>

object UserValidation {

    fun validateUserCreation(referenceDate: LocalDateTime, userCreation: UserCreation): FormValidationResult<UserCreation> {
        return Validated.applicative(NonEmptyList.semigroup<UserCreationError>())
                .tupled(
                        Validated.fromEither(validateFirstName(userCreation.firstName)),
                        Validated.fromEither(validateLastName(userCreation.lastName)),
                        Validated.fromEither(validateBirthday(referenceDate, userCreation.birthday)),
                        Validated.fromEither(validateDocumentId(userCreation.documentId)),
                        Validated.fromEither(validatePhoneNumber(userCreation.phoneNumber)),
                        Validated.fromEither(validateEmail(userCreation.email))
                ).fix()
                .map { t: Tuple6<String, String, LocalDateTime, String, String, String> ->
                    // @product does not work!
                    UserCreation(t.a, t.b, t.c, t.d, t.e, t.f)
                }
    }

    private fun validateFirstName(firstName: String): ErrorOr<String> =
            Either.cond(firstName.trim().isNotEmpty(),
                    { firstName },
                    { NonEmptyList.of(UserCreationError.EmptyFirstName(firstName)) })

    private fun validateLastName(lastName: String): ErrorOr<String> =
            Either.cond(lastName.trim().isNotEmpty(),
                    { lastName },
                    { NonEmptyList.of(UserCreationError.EmptyLastName(lastName)) })

    private fun validateBirthday(referenceDate: LocalDateTime, birthday: LocalDateTime): ErrorOr<LocalDateTime> =
            Either.cond(birthday <= referenceDate.minusYears(18),
                    { birthday },
                    { NonEmptyList.of(UserCreationError.UserTooYoung(birthday)) })

    private fun validateDocumentId(documentId: String): ErrorOr<String> {
        val documentRegex = "\\d{8}[a-zA-Z]{1}".toRegex()
        return Either.cond(documentRegex.matches(documentId),
                { documentId },
                { NonEmptyList.of(UserCreationError.InvalidDocumentId(documentId)) })
    }

    private fun validatePhoneNumber(phoneNumber: String): ErrorOr<String> {
        val phoneRegex = "\\d{9}".toRegex()
        return Either.cond(phoneRegex.matches(phoneNumber),
                { phoneNumber },
                { NonEmptyList.of(UserCreationError.InvalidPhoneNumber(phoneNumber)) })
    }

    private fun validateEmail(email: String): ErrorOr<String> =
            Either.cond(email.contains("@"),
                    { email },
                    { NonEmptyList.of(UserCreationError.InvalidEmail(email)) })

}

// @product  this should activate toUserCreation() in Tuple6
data class UserCreation(
        val firstName: String,
        val lastName: String,
        val birthday: LocalDateTime,
        val documentId: String,
        val phoneNumber: String,
        val email: String
) {
    companion object
}

sealed class UserCreationError {
    data class EmptyFirstName(val name: String) : UserCreationError()
    data class EmptyLastName(val name: String) : UserCreationError()
    data class UserTooYoung(val birthday: LocalDateTime) : UserCreationError()
    data class InvalidDocumentId(val documentId: String) : UserCreationError()
    data class InvalidPhoneNumber(val phoneNumber: String) : UserCreationError()
    data class InvalidEmail(val email: String) : UserCreationError()
}
