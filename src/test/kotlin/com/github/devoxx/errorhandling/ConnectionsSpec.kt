package com.github.devoxx.errorhandling

import arrow.core.Try
import arrow.fx.IO
import com.github.devoxx.errorhandling.Connections.defaultUrl
import io.kotlintest.Tag
import io.kotlintest.fail
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import java.io.InputStream
import java.net.MalformedURLException

object TryTests : Tag()
object EitherTests: Tag()
object IOTests: Tag()

class ConnectionsSpec : StringSpec({

    // ---------------------------
    // Using Try
    // ---------------------------
    "Try<T> should catch exceptions".config(tags = setOf(TryTests)) {
        val result = listOf(
                Connections.UsingTry.parseURL("http://google.com"),
                Connections.UsingTry.parseURL("azsdvbhytfd.co.uk") // no protocol specified
        )
        result.map { it.isSuccess() }.shouldBe(listOf(true, false))
    }

    "Try<T> should default values" .config(tags = setOf(TryTests)) {
        val result = Connections.UsingTry.urlOrElse("azsdvbhytfd.co.uk") // no protocol specified
        result.shouldBe(defaultUrl)
    }

    "Try<T>.map should demonstrate".config(tags = setOf(TryTests)) {
        val result: Try<Try<Try<InputStream>>> = Connections.UsingTry.inputStreamForURLWithMap("http://google.com")
        result.isSuccess().shouldBe(true)
    }

    "Try<T>.flatMap should demonstrate".config(tags = setOf(TryTests)) {
        val result: Try<InputStream> = Connections.UsingTry.inputStreamForURL("http://google.com")
        result.isSuccess().shouldBe(true)
    }

    "Try<T>.filter should demonstrate".config(tags = setOf(TryTests)) {
        Connections.UsingTry.parseHttpURL("http://google.com").isSuccess().shouldBe(true)
        Connections.UsingTry.parseHttpURL("https://google.com").isSuccess().shouldBe(false)
    }

    "Try<T>.filter should demonstrate monad comprehension".config(tags = setOf(TryTests)) {
        val urlContent: Try<ByteIterator> = Connections.UsingTry.getURLContent("http://google.com")
        urlContent.isSuccess().shouldBe(true)
    }

    "Try<T>.handleError should demonstrate".config(tags = setOf(TryTests)) {
        // It must handle a MalformedURLException
        Connections.UsingTry.handleErrors("azsdvbhytfd.co.uk").fold(
                { it.shouldBeInstanceOf<IllegalStateException>() },
                { fail("it was expected to fail") }
        )
        // It must handle any other exception
        Connections.UsingTry.handleErrors("http://theguardian.ru").fold(
                { it.shouldBeInstanceOf<UnsupportedOperationException>() },
                { fail("it was expected to fail") }
        )
    }

    // ---------------------------
    // Using Either
    // ---------------------------
    "Either<U, T> should catch exceptions".config(tags = setOf(EitherTests)) {
        val result = listOf(
                Connections.UsingEither.parseURL("http://google.com"),
                Connections.UsingEither.parseURL("azsdvbhytfd.co.uk") // no protocol specified
        )
        result.map { it.isRight() }.shouldBe(listOf(true, false))
    }

    "Either<U, T> should handle values".config(tags = setOf(EitherTests)) {
        val result = Connections.UsingEither.urlOrElse("azsdvbhytfd.co.uk") // no protocol specified
        defaultUrl.fold({ fail("unexpected") }, { url -> result.shouldBe(url) })
    }

    "Either<U, T>.filter should demonstrate filtering".config(tags = setOf(EitherTests)) {
        Connections.UsingEither.parseHttpURL("http://google.com").isRight().shouldBe(true)
        Connections.UsingEither.parseHttpURL("https://google.com").isRight().shouldBe(false)
    }

    "Either<U, T>.handleError should handle errors :)".config(tags = setOf(EitherTests)) {
        // It must handle a MalformedURLException
        Connections.UsingEither.handleErrors("azsdvbhytfd.co.uk").fold(
                { it.shouldBeInstanceOf<IllegalStateException>() },
                { fail("it was expected to fail") }
        )
        // It must handle any other exception
        Connections.UsingEither.handleErrors("http://theguardian.ru").fold(
                { it.shouldBeInstanceOf<UnsupportedOperationException>() },
                { fail("it was expected to fail") }
        )
    }

    // ---------------------------
    // Using IO
    // ---------------------------

    "IO<T> should catch exceptions".config(tags = setOf(IOTests)) {
        Connections.UsingIO.parseURL("http://google.com").unsafeRunSync().shouldNotBeNull()
        shouldThrow<MalformedURLException> {
            Connections.UsingIO.parseURL("azsdvbhytfd.co.uk").unsafeRunSync()
        }
    }

    "IO<T>.flatMap should demonstrate".config(tags = setOf(IOTests)) {
        val result: IO<InputStream> = Connections.UsingIO.inputStreamForURL("http://google.com")
        result.unsafeRunSync().shouldNotBeNull()
    }
})
