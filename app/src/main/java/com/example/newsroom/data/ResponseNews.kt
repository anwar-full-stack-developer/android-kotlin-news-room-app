package com.example.newsroom.data
import com.google.gson.annotations.SerializedName


data class ResponseNews(
    @SerializedName("data")
    var data: NewsData?,

    @SerializedName("status")
    var status: Int,

    @SerializedName("message")
    var message: String?,
)