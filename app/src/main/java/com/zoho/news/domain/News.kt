package com.zoho.news.domain

data class News(
    var id: Int? = null,
    var title: String? = null,
    var url: String? = null,
    var imageUrl: String? = null,
    var newsSite: String? = null,
    var summary: String? = null,
    var publishedAt: String? = null,
    var updatedAt: String? = null
) {
    fun doesMatchSearchQuery(queryText: String): Boolean {
        val matchingCombinations = listOf(
            "$title",
            "$newsSite",
            "$summary",
            "${title?.first()}",
            "${newsSite?.first()}",
            "${summary?.first()}"
        )
        return matchingCombinations.any {
            it.contains(queryText, ignoreCase = true)
        }
    }
}
