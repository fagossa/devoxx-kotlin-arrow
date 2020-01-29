package com.github.devoxx.errorhandling

import arrow.core.Try
import com.github.devoxx.errorhandling.Connections.UsingTry.defaultUrl
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.io.InputStream

class UserValidationSpec : StringSpec({
    "should catch exceptions" {
        val result = listOf(
                Connections.UsingTry.parseURL("http://google.com"),
                Connections.UsingTry.parseURL("azsdvbhytfd.co.uk") // no protocol specified
        )
        result.map { it.isSuccess() }.shouldBe(listOf(true, false))
    }

    "should default values" {
        val result = Connections.UsingTry.urlOrElse("azsdvbhytfd.co.uk") // no protocol specified
        result.shouldBe(defaultUrl)
    }

    "should default values" {
        val result: Try<Try<Try<InputStream>>> = Connections.UsingTry.inputStreamForURLWithMap("http://google.com")
        result.isSuccess().shouldBe(true)
    }
})


/*
[
Success(value=http://google.com),
Failure(exception=java.net.MalformedURLException: no protocol: azsdvbhytfd.co.uk)
]

[
Success(value=http://google.com),
Failure(exception=java.net.MalformedURLException: no protocol: azsdvbhytfd.co.uk)
]
*/
