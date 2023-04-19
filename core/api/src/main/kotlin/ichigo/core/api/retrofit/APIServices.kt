package ichigo.core.api.retrofit

import ichigo.core.api.retrofit.responses.ChampionsResponse
import ichigo.core.api.retrofit.responses.RealmResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DataDragonService {

    @GET("api/versions.json")
    suspend fun versions(): List<String>

    @GET("cdn/languages.json")
    suspend fun languages(): List<String>

    @GET("realms/{platform}.json")
    suspend fun realm(
        @Path("platform") platform: String,
    ): RealmResponse

    @GET("cdn/{version}/data/{lang}/champion.json")
    suspend fun champions(
        @Path("version") version: String,
        @Path("lang") lang: String,
    ): ChampionsResponse

    @GET("cdn/{version}/data/{lang}/champion/{champKey}.json")
    suspend fun champion(
        @Path("version") version: String,
        @Path("lang") lang: String,
        @Path("champKey") champKey: String,
    ): ChampionsResponse

}