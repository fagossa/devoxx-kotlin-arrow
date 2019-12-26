package com.github.devoxx.transformers

import arrow.optics.optics
import com.squareup.moshi.JsonClass

@optics
@JsonClass(generateAdapter = true)
data class Holiday(
        val date: String,
        val localName: String,
        val name: String,
        val countryCode: String,
        val fixed: Boolean,
        val global: Boolean,
        val counties: List<String>,
        val launchYear: Int,
        val type: String) {
    companion object
}
