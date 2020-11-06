package soy.gabimoreno.gabinimble.corenetwork.service

import retrofit2.http.GET
import retrofit2.http.Path
import soy.gabimoreno.gabinimble.corenetwork.dto.NetworkSong

interface SongApiService {

    @GET("{filename}.json")
    suspend fun getSongs(
        @Path("filename") filename: String
    ): List<NetworkSong>
}
