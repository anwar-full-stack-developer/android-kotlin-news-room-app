package com.example.newsroom.data

import com.google.gson.annotations.SerializedName

data class ResponseListNews(
    @SerializedName("data")
    var data: List<NewsData>?,

    @SerializedName("status")
    var status: Int,

    @SerializedName("message")
    var message: String,
)