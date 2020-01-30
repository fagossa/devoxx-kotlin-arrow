package com.github.devoxx.optics

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class LensExerciseSpec : StringSpec({
    "should get address street name" {
        // TODO : make this test pass
        getStreetName(address).shouldBe(street.name)
    }

    "should set address street name" {
        // TODO : make this test pass
        setStreetName(address, newStreetName).street.name.shouldBe(newStreetName)
    }

    "should get street name" {
        // TODO : make this test pass
        streetNameLens.get(street).shouldBe(street.name)
    }

    "should set street name" {
        // TODO : make this test pass
        streetNameLens.set(street, newStreetName).name.shouldBe(newStreetName)
    }

    "should modify street name" {
        // TODO : make this test pass
        updateStreetNameToUpperCase(street).name.shouldBe(street.name.toUpperCase())
    }

    "should get street" {
        // TODO : make this test pass
        addressStreetLens.get(address).shouldBe(address.street)
    }

    "should set street" {
        // TODO : make this test pass
        addressStreetLens.set(address, newStreet).street.shouldBe(newStreet)
    }

    "should get street name from address" {
        // TODO : make this test pass
        composeAddressStreetNameLenses.get(address).shouldBe(address.street.name)
    }

    "should set street name on address" {
        // TODO : make this test pass
        composeAddressStreetNameLenses.set(address, newStreetName).street.name.shouldBe(newStreetName)
    }
})