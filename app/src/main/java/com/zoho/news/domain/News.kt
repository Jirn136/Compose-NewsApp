package com.zoho.news.domain

data class News(
    var id: Int,
    var title: String? = null,
    var url: String? = null,
    var imageUrl: String? = null,
    var newsSite: String? = null,
    var summary: String? = null,
    var publishedAt: String? = null,
    var updatedAt: String? = null
)
