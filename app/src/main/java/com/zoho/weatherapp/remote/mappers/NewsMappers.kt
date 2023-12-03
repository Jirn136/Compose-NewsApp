package com.zoho.weatherapp.remote.mappers

import com.zoho.weatherapp.database.NewsEntity
import com.zoho.weatherapp.domain.News
import com.zoho.weatherapp.remote.model.NewsDto

fun NewsDto.toNewsEntity(): List<NewsEntity> {
    return this.results.map {
        NewsEntity(
            id = it.id,
            title = it.title,
            url = it.url,
            imageUrl = it.image_url,
            newsSite = it.news_site,
            summary = it.summary,
            publishedAt = it.published_at,
            updatedAt = it.updated_at
        )
    }
}

fun NewsEntity.toNews(): News {
    return News(
        id = id,
        title = title,
        url = url,
        imageUrl = imageUrl,
        newsSite = newsSite,
        summary = summary,
        publishedAt = publishedAt,
        updatedAt = updatedAt
    )
}