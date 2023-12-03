package com.zoho.weatherapp.remote.model


data class NewsDto(
    val count: Int,
    val next: String?,
    val previous: String,
    val results: List<Result>
)

data class Result(
    val events: List<Any>,
    val featured: Boolean,
    val id: Int,
    val image_url: String,
    val launches: List<Any>,
    val news_site: String,
    val published_at: String,
    val summary: String,
    val title: String,
    val updated_at: String,
    val url: String
)