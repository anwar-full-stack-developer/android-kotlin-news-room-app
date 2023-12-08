package com.example.newsroom.data

import com.google.gson.annotations.SerializedName

data class NewsNewRequestData(

    @SerializedName("title")
    var title: String,

    @SerializedName("details")
    var details: String,

    @SerializedName("status")
    var status: String,

)