package com.example.dailiestracker

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first

// Extension property to initialize DataStore
val Context.dataStore by preferencesDataStore(name = "dailies_tracker_prefs")

class DataStorageManager(private val context: Context) {
    private val gson = Gson()
    private val sectionsKey = stringPreferencesKey("saved_sections")
    private val lastSavedTimeKey = stringPreferencesKey("last_saved_time")

    // Save data along with the current system timestamp
    suspend fun saveSections(sections: List<TrackerSection>) {
        context.dataStore.edit { preferences ->
            preferences[sectionsKey] = gson.toJson(sections)
            preferences[lastSavedTimeKey] = System.currentTimeMillis().toString()
        }
    }

    // Load data and return both the list and the timestamp when it was saved
    suspend fun loadSections(): Pair<List<TrackerSection>?, Long> {
        val preferences = context.dataStore.data.first()
        val jsonString = preferences[sectionsKey]
        val lastTimeStr = preferences[lastSavedTimeKey]

        val sections = if (!jsonString.isNullOrEmpty()) {
            val type = object : TypeToken<List<TrackerSection>>() {}.type
            gson.fromJson<List<TrackerSection>>(jsonString, type)
        } else {
            null
        }

        val lastTime = lastTimeStr?.toLongOrNull() ?: System.currentTimeMillis()
        return Pair(sections, lastTime)
    }
}