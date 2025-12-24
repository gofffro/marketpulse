package com.example.marketpulse.data.remote.api

import com.example.marketpulse.data.remote.dto.FinnhubQuoteDto
import com.example.marketpulse.data.remote.dto.FinnhubSearchDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FinnhubApi {
    // /quote?symbol=AAPL&token=...
    @GET("quote")
    suspend fun getQuote(
        @Query("symbol") symbol: String,
        @Query("token") token: String
    ): FinnhubQuoteDto

    // /search?q=apple&exchange=US&token=...
    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("exchange") exchange: String? = null,
        @Query("token") token: String
    ): FinnhubSearchDto
}
