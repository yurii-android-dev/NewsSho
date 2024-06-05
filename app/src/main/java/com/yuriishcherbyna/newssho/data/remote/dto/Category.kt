package com.yuriishcherbyna.newssho.data.remote.dto

import com.squareup.moshi.Json

enum class Category {
    @Json(name = "business")
    BUSINESS,
    @Json(name = "entertainment")
    ENTERTAINMENT,
    @Json(name = "general")
    GENERAL,
    @Json(name = "health")
    HEALTH,
    @Json(name = "science")
    SCIENCE,
    @Json(name = "sports")
    SPORTS,
    @Json(name = "technology")
    TECHNOLOGY
}