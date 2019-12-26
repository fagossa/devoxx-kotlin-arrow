package com.github.devoxx.transformers

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.result.Result

object HolidayClient {

    /*
     * TODO:
     */
    fun holidayFor(year: Int): Either<Throwable, List<Holiday>> {
        val (_, _, result) = fuelManager.request(Method.GET, "https://date.nager.at/api/v2/publicholidays/$year/CO").responseString()
        // We cannot use this method because the certificate is auto-signed!
        //val (_, _, result) = "https://date.nager.at/api/v2/publicholidays/$year/CO".httpGet().responseString() // blockingIO
        return when (result) {
            is Result.Failure -> result.getException().left()
            is Result.Success -> fromJson(result.value).right()
        }
    }

}