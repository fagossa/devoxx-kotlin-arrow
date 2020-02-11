package com.github.devoxx.monadtransformer

import arrow.core.Either
import arrow.core.Option
import arrow.core.Some
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class MonadTransformerExerciseSpec : StringSpec({
    val streetName = "123 Main St"
    val maybePerson: Option<Person> = Option.just(
            Person(
                    name = "Alfredo Lambda",
                    address = Some(
                            Address(
                                    id = 1,
                                    city = "San Francisco",
                                    street = Some(streetName)
                            )
                    )
            )
    )

    "should get person address street name" {
        // TODO : make this test pass
        getStreetNameUsingFlatMap(maybePerson).shouldBe(Some(streetName))
    }

    "should get person address street name using monad comprehension" {
        // TODO : make this test pass
        getStreetNameUsingMonadComprehension(maybePerson).shouldBe(Some(streetName))
    }

    "should get person address street name calling database" {
        // TODO : make this test pass
        getPersonAddressStreetName(personId).shouldBe(Either.right(Option.just(Option.just(Either.right(Some(streetName))))))
    }

    "should get person address street name calling database and using monad comprehension" {
        // TODO : make this test pass
        getPersonAddressStreetNameUsingMonadComprehension(personId).shouldBe(Either.right((Some(streetName))))
    }

    "should get person address street name using monad transformer and monad comprehension" {
        // TODO : make this test pass
        getPersonAddressStreetNameUsingMonadTransformer(personId).shouldBe(Either.right((Some(streetName))))
    }
})