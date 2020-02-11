package com.github.devoxx.monadtransformer

import arrow.core.Either
import arrow.core.Option
import arrow.core.extensions.either.monad.monad
import arrow.core.extensions.fx
import arrow.core.fix
import arrow.mtl.OptionT
import arrow.mtl.extensions.fx


/*
 * TODO:
 * Get person address street name using flatMap
 */
fun getStreetNameUsingFlatMap(maybePerson: Option<Person>): Option<String> =
        maybePerson.flatMap { person ->
            person.address.flatMap { address ->
                address.street
            }
        }

/*
 * TODO:
 * Get person address street name using monad comprehensions
 */
fun getStreetNameUsingMonadComprehension(maybePerson: Option<Person>): Option<String> =
        Option.fx {
            val (person) = maybePerson
            val (address) = person.address
            val (street) = address.street
            street
        }

/*
 * TODO:
 * Get person address street name (do not use monad comprehension)
 */
fun getPersonAddressStreetName(personId: Int) =
        findPerson(personId).map { maybePerson ->
            maybePerson.map { person ->
                person.address.map { address ->
                    findAddressStreetName(address.id).map { maybeStreetName ->
                        maybeStreetName.map { it }
                    }
                }
            }
        }

/*
 * TODO:
 * Get person address street name using monad comprehension
 */
fun getPersonAddressStreetNameUsingMonadComprehension(personId: Int) =
        Either.fx<Throwable, Option<String>> {
            val maybePerson = findPerson(personId).bind()
            val person = maybePerson.fold(
                    { Either.left(NoSuchElementException("...")) },
                    { Either.right(it) }
            ).bind()
            val address = person.address.fold(
                    { Either.left(NoSuchElementException("...")) },
                    { Either.right(it) }
            ).bind()
            val maybeStreetName = findAddressStreetName(address.id).bind()
            maybeStreetName
        }

/*
    Monad Transformers enable you to combine two monads into a super monad.
    OptionT has the form of OptionT<F, A>.
    This means that, for any monad F surrounding an Option<A>, we can obtain an OptionT<F, A>.
    So our specialization OptionT<ForEither, A> is the OptionT transformer around values that are of Either<Throwable, Option<A>>.
 */

/*
 * TODO:
 * Get person address street name using monad transformer OptionT and monad comprehension
 */
fun getPersonAddressStreetNameUsingMonadTransformer(personId: Int) =
        OptionT.fx(Either.monad<Throwable>()) {
            val (person) = OptionT(findPerson(personId))
            val (address) = OptionT(Either.right(person.address))
            val (streetName) = OptionT(findAddressStreetName(address.id))
            streetName
        }.value().fix()