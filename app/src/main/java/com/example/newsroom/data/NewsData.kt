package com.example.newsroom.data

import com.google.gson.annotations.SerializedName

data class NewsData(
    @SerializedName("_id")
    var _id: String,

    @SerializedName("id")
    var id: String,

    @SerializedName("title")
    var title: String,

    @SerializedName("details")
    var details: String,

    @SerializedName("status")
    var status: String,

)