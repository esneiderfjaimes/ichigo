package com.red.code015.api

import com.red.code015.api.APIConstants.KEY
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Path

interface SummonersService {

    @GET("by-name/{summonerName}?api_key=$KEY")
    fun summonerByName(@Path("summonerName") name: String): Maybe<SummonerResponseServer>

}