package com.github.devoxx.transformers

import arrow.core.ListK
import arrow.core.Try
import arrow.core.k
import arrow.core.toOption
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

private val moshi: Moshi = Moshi.Builder()
        //.add(KotlinJsonAdapterFactory())
        .build()

val gistListJsonAdapter: JsonAdapter<List<Holiday>> =
        moshi.adapter(Types.newParameterizedType(List::class.java, Holiday::class.java))

fun fromJson(json: String): ListK<Holiday> =
        Try { gistListJsonAdapter.lenient().fromJson(json).toOption() }
                .fold({ emptyList<Holiday>().k() }, { parsed ->
                    parsed.fold({ emptyList<Holiday>().k() }, { it.k() })
                })
