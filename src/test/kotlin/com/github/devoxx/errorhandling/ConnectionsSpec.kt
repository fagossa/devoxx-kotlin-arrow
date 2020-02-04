package com.github.devoxx.errorhandling

import arrow.core.Try
import com.github.devoxx.errorhandling.Connections.defaultUrl
import io.kotlintest.fail
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.io.InputStream

class ConnectionsSpec : StringSpec({

    // ---------------------------
    // Using Try
    // ---------------------------
    "should catch exceptions using Try<T>" {
        val result = listOf(
                Connections.UsingTry.parseURL("http://google.com"),
                Connections.UsingTry.parseURL("azsdvbhytfd.co.uk") // no protocol specified
        )
        result.map { it.isSuccess() }.shouldBe(listOf(true, false))
    }

    "should default values for Try<T>" {
        val result = Connections.UsingTry.urlOrElse("azsdvbhytfd.co.uk") // no protocol specified
        result.shouldBe(defaultUrl)
    }

    "should demonstrate Try<T>.map" {
        val result: Try<Try<Try<InputStream>>> = Connections.UsingTry.inputStreamForURLWithMap("http://google.com")
        result.isSuccess().shouldBe(true)
    }

    "should demonstrate Try<T>.flatMap" {
        val result: Try<InputStream> = Connections.UsingTry.inputStreamForURL("http://google.com")
        result.isSuccess().shouldBe(true)
    }

    "should demonstrate Try<T>.filter" {
        Connections.UsingTry.parseHttpURL("http://google.com").isSuccess().shouldBe(true)
        Connections.UsingTry.parseHttpURL("https://google.com").isSuccess().shouldBe(false)
    }

    "should demonstrate monad comprehension" {
        val urlContent: Try<ByteIterator> = Connections.UsingTry.getURLContent("http://google.com")
        urlContent.isSuccess().shouldBe(true)
    }

    "should demonstrate Try<T>.handleError" {
        // It must handle a MalformedURLException
        Connections.UsingTry.handleErrors("azsdvbhytfd.co.uk").fold (
                { it.shouldBeInstanceOf<IllegalStateException>() },
                { fail("it was expected to fail")}
        )
        // It must handle any other exception
        Connections.UsingTry.handleErrors("http://theguardian.ru").fold (
                { it.shouldBeInstanceOf<UnsupportedOperationException>() },
                { fail("it was expected to fail")}
        )
    }

    // ---------------------------
    // Using Either
    // ---------------------------
    "should catch exceptions using Either<U, T>" {
        val result = listOf(
                Connections.UsingEither.parseURL("http://google.com"),
                Connections.UsingEither.parseURL("azsdvbhytfd.co.uk") // no protocol specified
        )
        result.map { it.isRight() }.shouldBe(listOf(true, false))
    }

    "should default values for Either<U, T>" {
        val result = Connections.UsingEither.urlOrElse("azsdvbhytfd.co.uk") // no protocol specified
        defaultUrl.fold({fail("unexpected")}, { url -> result.shouldBe(url) })
    }

    "should demonstrate Either<U, T>.filter" {
        Connections.UsingEither.parseHttpURL("http://google.com").isRight().shouldBe(true)
        Connections.UsingEither.parseHttpURL("https://google.com").isRight().shouldBe(false)
    }

    "should demonstrate Either<U, T>.handleError" {
        // It must handle a MalformedURLException
        Connections.UsingEither.handleErrors("azsdvbhytfd.co.uk").fold (
                { it.shouldBeInstanceOf<IllegalStateException>() },
                { fail("it was expected to fail") }
        )
        // It must handle any other exception
        Connections.UsingEither.handleErrors("http://theguardian.ru").fold (
                { it.shouldBeInstanceOf<UnsupportedOperationException>() },
                { fail("it was expected to fail") }
        )
    }
})

