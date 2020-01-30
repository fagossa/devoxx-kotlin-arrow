package com.github.devoxx.optics

import arrow.optics.Lens

val street = Street(num = 23, name = "high street")
const val newStreetName = "Paris"
val address: Address = Address(city = "London", street = street)
val newStreet = Street(num = 24, name = "new street")

/*
 * TODO:
 * Get address street name without arrow
 */
fun getStreetName(address: Address): String = address.street.name

/*
 * TODO:
 * Set address street name without arrow
 */
fun setStreetName(address: Address, name: String): Address = address.copy(street = street.copy(name = name))

/*
 * Optics are essentially abstractions to update immutable data structures in an elegant way.
 * A Lens (aka functional reference) is an optic that can focus into a structure and get, modify, or set its focus (target).
 * They’re mostly used for product types such as a data class or a TupleN.
 *
 * Lenses can be seen as a pair of functions, a getter, and a setter.
 * A Lens<S, A> represents a getter: get: (S) -> A, and setter: (A) -> (S) -> S,
 * where S is called the source of the Lens and A is called the focus or target of the Lens.
 *
 * https://arrow-kt.io/docs/optics/lens/
 */

/*
 * TODO:
 * Define a Lens to get and set street name
 */
val streetNameLens: Lens<Street, String> = Lens(
        get = { it.name },
        set = { street, name -> street.copy(name = name) }
)

/*
 * TODO:
 * Use streetNameLens to write street name with upper cases
 */
fun updateStreetNameToUpperCase(street: Street) = streetNameLens.modify(street, String::toUpperCase)

/*
 * TODO:
 * Define a Lens to get and set street
 */
val addressStreetLens: Lens<Address, Street> = Lens(
        get = { it.street },
        set = { address, street -> address.copy(street = street) }
)

/*
 * TODO:
 * Compose streetNameLens and addressStreetLens
 */
val composeAddressStreetNameLenses: Lens<Address, String> = addressStreetLens compose streetNameLens