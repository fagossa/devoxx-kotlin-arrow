package com.github.devoxx.monadtransformer

import arrow.core.Either
import arrow.core.Option
import arrow.core.Some

data class Person(val name: String, val address: Option<Address>)

const val personId = 1
val personDB: Map<Int, Person> = mapOf(
        personId to Person(
                name = "Alfredo Lambda",
                address = Some(
                        Address(
                                id = 1,
                                city = "San Francisco",
                                street = Some("123 Main St")
                        )
                )
        )
)

fun findPerson(personId: Int): Either<Throwable, Option<Person>> =
        Either.right(Option.fromNullable(personDB[personId])) //mock impl for simplicity