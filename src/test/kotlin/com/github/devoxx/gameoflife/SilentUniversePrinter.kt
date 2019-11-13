package com.github.devoxx.gameoflife

import arrow.fx.IO

class SilentUniversePrinter : UniversePrinter {
    override fun print(universe: Universe): IO<String> = IO.just("")
}