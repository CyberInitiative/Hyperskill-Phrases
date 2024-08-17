package org.hyperskill.phrases

import android.app.PendingIntent

object AlarmManagerHelper {
    private val _pendingIntentMap = mutableMapOf<String, PendingIntent>()
    val pendingIntentMap : Map<String, PendingIntent>
        get() = _pendingIntentMap

    fun addPendingIntent(key: String, pendingIntent: PendingIntent) {
        _pendingIntentMap[key] = pendingIntent
    }

    fun getPendingIntent(key: String): PendingIntent? {
        return _pendingIntentMap[key]
    }

    fun removePendingIntent(key: String) {
        _pendingIntentMap.remove(key)
    }
}