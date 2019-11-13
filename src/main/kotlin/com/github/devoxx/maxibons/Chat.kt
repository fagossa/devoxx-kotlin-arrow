package com.github.devoxx.maxibons

import arrow.Kind

interface Chat<F> {
    fun sendMessage(message: String): Kind<F, String>
}