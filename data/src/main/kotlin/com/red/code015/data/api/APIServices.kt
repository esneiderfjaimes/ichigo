package com.red.code015.data.api

import com.red.code015.data.api.APIConstants.KEY
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface SummonersService {

    @GET("by-name/{summonerName}?api_key=$KEY")
    fun summonerByName(@Path("summonerName") name: String): Single<SummonerResponseServer>

}