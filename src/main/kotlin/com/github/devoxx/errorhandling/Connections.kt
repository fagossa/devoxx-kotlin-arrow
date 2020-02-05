package com.github.devoxx.errorhandling

import arrow.core.*
import arrow.core.extensions.fx
import kotlinx.coroutines.runBlocking
import java.io.BufferedInputStream
import java.io.InputStream
import java.lang.IllegalArgumentException

import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection

object Connections {

    val defaultUrl = Try { URL("http://duckduckgo.com") }

    /* Try got deprecated :'(
     * https://www.47deg.com/blog/arrow-v0-10-3-release/
     */
    object UsingTry {

        /*
         * TODO:
         * we return a value of type Try<URL>.
         * If the given url is syntactically correct, this will be a Success<URL>.
         * If the URL constructor throws a MalformedURLException, however, it will be a Failure<URL>.
         */
        fun parseURL(url: String): Try<URL> = Try { URL(url) }

        /*
         * TODO:
         *  'parseUrl' and use 'orElse' to return the 'defaultUrl'
         */
        fun urlOrElse(url: String): Try<URL> = parseURL(url).orElse { defaultUrl }

        /*
         * TODO:
         *  parseUrl, then open the connection, and finally get the inputStream
         *  chaining with map is probably NOT what we want ... but let's do it anyways
         */
        fun inputStreamForURLWithMap(url: String): Try<Try<Try<InputStream>>> = parseURL(url)
                .map { u ->
                    Try { u.openConnection() }
                            .map { conn -> Try { conn.getInputStream() } }
                }

        /*
         * TODO:
         *  chaining with flatMap is what we are looking for :D
         */
        fun inputStreamForURL(url: String): Try<InputStream> = parseURL(url)
                .flatMap { u ->
                    Try { u.openConnection() }
                            .flatMap { conn -> Try { conn.getInputStream() } }
                }

        /*
         * TODO:
         *  filter content the same way as we do with lists
         */
        fun parseHttpURL(url: String) = parseURL(url).filter { it.protocol == "http" }

        /*
         * TODO:
         *  use the monad comprehension to parseURL, url.openConnection, and conn.inputStream,
         * finally get an iterator using this code 'BufferedInputStream(iss).iterator()'.
         * Remember that each line can fail!
         */
        fun getURLContent(rawUrl: String): Try<ByteIterator> =
                Try.fx {
                    val (url) = parseURL(rawUrl)
                    val (connection) = Try { url.openConnection() }
                    val (iss) = Try { connection.inputStream }
                    BufferedInputStream(iss).iterator()
                }.fix()

        /*
         * TODO:
         * It should use 'handleErrorWith' to recover from exceptions
         * MalformedURLException -> IllegalStateException("...")
         * and other exceptions  -> UnsupportedOperationException("...")
         */
        fun handleErrors(content: String): Try<InputStream> = inputStreamForURL(content).handleErrorWith { e ->
            when (e) {
                is MalformedURLException -> Failure(IllegalStateException("Please make sure to enter a valid URL"))
                else -> Failure(UnsupportedOperationException("An unexpected error has occurred. We are so sorry!"))
            }
        }

    }

    object UsingEither {

        /*
         * TODO:
         *  Use the function 'catch' to build a new URL.
         *  If the given url is syntactically correct, this will be a Right<Throwable, URL>.
         *  If the URL constructor throws a MalformedURLException, however, it will be a Left<Throwable, URL>.
         */
        suspend fun parseURL(url: String): Either<Throwable, URL> = Either.catch { URL(url) }

        /*
         * TODO:
         *  'parseUrl' and use 'orElse' to return the 'defaultUrl'
         */
        suspend fun urlOrElse(url: String): URL = parseURL(url)
                .getOrHandle { URL("http://duckduckgo.com") }

        /*
         * TODO:
         *  use the monad comprehension to 'parseURL', 'u.openConnection', and 'conn.getInputStream'.
         *  however to make things easier, handle errors with 'Try', then transform them to Either by using '.toEither'
         *  its a shame, but we might need to use 'runBlocking{...}' to call 'parseURL'
         *  use a monad comprehension by following this pattern
         *  Either.fx {
         *    val v1 = !aFunction() // this function returns an Either
         *    val v2 = !anotherFunction() // this function returns an Either
         *    v1.calculate(v2) // this is not an Either
         *  }
         */
        fun inputStreamForURL(url: String): Either<Throwable, InputStream> {
            fun open(u: URL): Either<Throwable, URLConnection> = Try { u.openConnection() }.toEither()
            fun stream(conn: URLConnection): Either<Throwable, InputStream> = Try { conn.getInputStream() }.toEither()
            fun parse(url: String): Either<Throwable, URL> = runBlocking { parseURL(url) }
            return Either.fx {
                val parsed = !parse(url)
                val conn = !open(parsed)
                !stream(conn)
            }
        }

        /*
         * TODO:
         *  'parseURL' then use 'filterOrElse' to filter content the same way as we do with lists
         *  Only allow urls of protocol 'http', otherwise 'IllegalArgumentException'
         */
        suspend fun parseHttpURL(url: String): Either<Throwable, URL> =
                parseURL(url).filterOrElse(
                        { it.protocol == "http" },
                        { IllegalArgumentException("Not http protocol") }
                )

        /*
         * TODO:
         *  It should use 'handleErrorWith' to recover from exceptions
         *  MalformedURLException -> IllegalStateException("...")
         *  and other exceptions  -> UnsupportedOperationException("...")
         */
        fun handleErrors(content: String): Either<Throwable, InputStream> =
                inputStreamForURL(content).handleErrorWith { e ->
                    when (e) {
                        is MalformedURLException -> Either.left(IllegalStateException("Please make sure to enter a valid URL"))
                        else -> Either.left(UnsupportedOperationException("An unexpected error has occurred. We are so sorry!"))
                    }
                }
    }
}
