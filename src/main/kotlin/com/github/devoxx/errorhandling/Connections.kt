package com.github.devoxx.errorhandling

import arrow.core.Try
import arrow.core.extensions.`try`.applicativeError.handleError
import arrow.core.extensions.fx
import arrow.core.orElse
import java.io.BufferedInputStream
import java.io.FileNotFoundException
import java.io.InputStream

import java.net.MalformedURLException
import java.net.URL

object Connections {

    fun String.iterator(): ByteIterator = BufferedInputStream(this.byteInputStream()).iterator()

    object UsingTry {
        val defaultUrl = Try { URL("http://duckduckgo.com") }

        /*
         * TODO:
         * we return a value of type Try<URL>.
         * If the given url is syntactically correct, this will be a Success<URL>.
         * If the URL constructor throws a MalformedURLException, however, it will be a Failure<URL>.
         */
        fun parseURL(url: String): Try<URL> = Try { URL(url) }

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
                }

        /*
         * TODO:
         * Handle FileNotFoundException, MalformedURLException and others with 'handleError'
         */
        fun handleErrors(content: String): Try<ByteIterator> = getURLContent(content).handleError { e ->
            when (e) {
                is FileNotFoundException -> "Requested page does not exist".iterator()
                is MalformedURLException -> "Please make sure to enter a valid URL".iterator()
                else -> "An unexpected error has occurred. We are so sorry!".iterator()
            }
        }

    }


}