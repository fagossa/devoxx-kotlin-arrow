package com.github.devoxx.transformers

import arrow.Kind
import arrow.fx.typeclasses.MonadDefer


interface Logger<F> {
    fun log(msg: String) : Kind<F, Unit>
}

class DefaultConsoleLogger<F>(private val delay: MonadDefer<F>): Logger<F>, MonadDefer<F> by delay {
    override fun log(msg: String): Kind<F, Unit> =
            later { println(msg) }
}