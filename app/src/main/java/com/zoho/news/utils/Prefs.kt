package com.zoho.news.utils

import android.content.Context

object Prefs {
    private const val PERMISSION_DENIED_COUNT = "permission_denied_count"
    private const val PREFS_NAME = "Prefs"
    fun getPermissionDeniedCount(context: Context): Int =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getInt(PERMISSION_DENIED_COUNT, 0)

    fun insertPermissionDeniedCount(context: Context, count: Int) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().putInt(
            PERMISSION_DENIED_COUNT, count
        ).apply()
    }
}