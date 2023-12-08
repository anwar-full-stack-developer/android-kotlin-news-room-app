package com.example.newsroom.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiInterface {
    @GET("news")
    suspend fun getAllNews(): Response<ResponseListNews>

    @POST("news")
    suspend fun saveNews(@Body newsData: NewsNewRequestData): Response<ResponseNews>

    @PUT("news/{id}")
    suspend fun updateNews(@Path("id") id: String, @Body newsData: NewsData): Response<ResponseNews>

    @DELETE("news/{id}")
    suspend fun deleteNews(@Path("id") id: String): Response<ResponseNews>
}