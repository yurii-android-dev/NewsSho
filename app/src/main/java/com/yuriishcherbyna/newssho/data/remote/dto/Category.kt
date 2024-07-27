package com.yuriishcherbyna.newssho.data.remote.dto

import androidx.annotation.StringRes
import com.squareup.moshi.Json
import com.yuriishcherbyna.newssho.R

enum class Category(@StringRes val nameId: Int) {
    @Json(name = "general")
    GENERAL(R.string.general),
    @Json(name = "business")
    BUSINESS(R.string.business),
    @Json(name = "entertainment")
    ENTERTAINMENT(R.string.entertainment),
    @Json(name = "health")
    HEALTH(R.string.health),
    @Json(name = "science")
    SCIENCE(R.string.science),
    @Json(name = "sports")
    SPORTS(R.string.sports),
    @Json(name = "technology")
    TECHNOLOGY(R.string.technology)
}