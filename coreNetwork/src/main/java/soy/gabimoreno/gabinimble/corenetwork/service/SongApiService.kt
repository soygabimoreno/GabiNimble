package soy.gabimoreno.gabinimble.corenetwork.service

import retrofit2.http.GET
import soy.gabimoreno.gabinimble.coredomain.Song

interface SongApiService {

    @GET("songs.json")
    suspend fun getSongs(
    ): List<Song>
}
