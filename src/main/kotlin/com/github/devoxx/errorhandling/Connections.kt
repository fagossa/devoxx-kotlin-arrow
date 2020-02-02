package com.github.devoxx.errorhandling

import arrow.core.*
import arrow.core.extensions.`try`.applicativeError.handleErrorWith
import arrow.core.extensions.either.monad.monad
import arrow.core.extensions.fx
import org.apache.commons.io.IOUtils
import java.io.BufferedInputStream
import java.io.InputStream
import java.lang.IllegalArgumentException

import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection

fun InputStream.consume(): String = IOUtils.toString(this, "UTF-8")

object Connections {

    object UsingTry {
        val defaultUrl = Try { URL("http://duckduckgo.com") }

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
        fun urlOrElse(url: String) = parseURL(url).orElse { defaultUrl }

        /*
         * TODO:
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
                    val (connection) = Try{ url.openConnection() }
                    val (iss) = Try { connection.inputStream }
                    BufferedInputStream(iss).iterator()
                }.fix()

        /*
         * TODO:
         * It should use 'handleErrorWith' to recover from exceptions
         * MalformedURLException -> IllegalStateException("...")
         * and other exceptions  -> UnsupportedOperationException("...")
         */
        fun handleErrors(content: String): Try<InputStream> = inputStreamForURL(content). handleErrorWith { e ->
            when (e) {
                is MalformedURLException -> Failure(IllegalStateException("Please make sure to enter a valid URL"))
                else -> Failure(UnsupportedOperationException("An unexpected error has occurred. We are so sorry!"))
            }
        }

    }

    object UsingEither {

        /*
         * TODO:
         * we return a value of type Try<URL>.
         * If the given url is syntactically correct, this will be a Success<URL>.
         * If the URL constructor throws a MalformedURLException, however, it will be a Failure<URL>.
         */
        suspend fun parseURL(url: String): Either<Throwable, URL> = Either.catch { URL(url) }

        /*
         * TODO:
         *  'parseUrl' and use 'orElse' to return the 'defaultUrl'
         */
        suspend fun urlOrElse(url: String) = parseURL(url)
                .getOrHandle { URL("http://duckduckgo.com") }

        /*
         * TODO:
         *  chaining with map is probably NOT what we want ... but let's do it anyways
         */
        suspend fun inputStreamForURLWithMap(url: String): Either<Throwable, Either<Throwable, Either<Throwable, InputStream>>> =
                parseURL(url)
                .map { u ->
                    Either.catch { u.openConnection() }
                            .map { conn -> Either.catch { conn.getInputStream() } }
                }

        /*
         * TODO:
         *  chaining with flatMap is what we are looking for :D
         * You could also flatten with: Either.monad<InputStream>().flatten(result)
         */
        suspend fun inputStreamForURL(url: String): Either<Throwable, InputStream> {
            suspend fun open(u: URL): Either<Throwable, URLConnection> = Either.catch { u.openConnection() }
            suspend fun stream(conn: URLConnection): Either<Throwable, InputStream> = Either.catch { conn.getInputStream() }
            return parseURL(url)
                    .flatMap { u -> open(u)
                            .flatMap { conn ->  stream(conn) }
            }
        }


        /*
         * TODO:
         *  filter content the same way as we do with lists using 'filterOrElse'
         */
        suspend fun parseHttpURL(url: String) =
                parseURL(url).filterOrElse (
                        { it.protocol == "http" },
                        { IllegalArgumentException ("Not http protocol")}
                )

        /*
         * TODO:
         *  use the monad comprehension to parseURL, url.openConnection, and conn.inputStream,
         * finally get an iterator using this code 'BufferedInputStream(iss).iterator()'.
         * Remember that each line can fail!
         */
         // https://youtu.be/q6HpChSq-xc?t=218
        suspend fun getURLContent(rawUrl: String) =
                Either.monad<Throwable>.fx {
                    val (url) = parseURL(rawUrl)
                    val (connection) = Either.catch{ url.openConnection() }
                    val (iss) = Either.catch { connection.inputStream }
                    BufferedInputStream(iss).iterator()
                }

        /*
         * TODO:
         * It should use 'handleErrorWith' to recover from exceptions
         * MalformedURLException -> IllegalStateException("...")
         * and other exceptions  -> UnsupportedOperationException("...")
         */
        suspend fun handleErrors(content: String): Try<InputStream> = inputStreamForURL(content). handleErrorWith { e ->
            when (e) {
                is MalformedURLException -> Failure(IllegalStateException("Please make sure to enter a valid URL"))
                else -> Failure(UnsupportedOperationException("An unexpected error has occurred. We are so sorry!"))
            }
        }
    }


}