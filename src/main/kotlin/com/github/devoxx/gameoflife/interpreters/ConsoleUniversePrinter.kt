package com.github.devoxx.gameoflife.interpreters

import arrow.fx.IO
import com.github.devoxx.gameoflife.Universe
import com.github.devoxx.gameoflife.UniversePrinter

class ConsoleUniversePrinter : UniversePrinter {
    override fun print(universe: Universe): IO<String> = IO {
        println("-------------------------")
        val stringRepresentation = universe.toString()
        print(stringRepresentation)
        stringRepresentation
    }
}