package com.github.devoxx.monadtransformer

import arrow.core.Either
import arrow.core.Option
import arrow.core.Some

data class Address(val id: Int, val city: String, val street: Option<String>)

val addressDB: Map<Int, Address> = mapOf(
        1 to Address(
                id = 1,
                city = "San Francisco",
                street = Some("123 Main St")
        )
)

fun findAddressStreetName(addressId: Int): Either<Throwable, Option<String>> =
        Either.right(
                Option.fromNullable(addressDB[addressId]).flatMap { it.street }
        ) //mock impl for simplicity