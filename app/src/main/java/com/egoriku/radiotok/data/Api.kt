package com.egoriku.radiotok.data

import com.egoriku.radiotok.data.entity.StationEntity
import retrofit2.http.GET
import retrofit2.http.Url

interface Api {

    @GET
    suspend fun getAllStations(@Url url: String): List<StationEntity>
}