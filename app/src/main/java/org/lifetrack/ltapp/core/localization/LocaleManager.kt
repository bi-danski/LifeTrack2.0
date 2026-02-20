package org.lifetrack.ltapp.core.localization

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object LocaleManager {
    private const val PREFS_NAME = "lt_locale_prefs"
    private const val KEY_PREF_LANG = "preferred_language"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        if (!::prefs.isInitialized) {
            prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    fun getPreferredLanguage(): String {
        return if (::prefs.isInitialized) {
            prefs.getString(KEY_PREF_LANG, "en") ?: "en"
        } else {
            "en"
        }
    }

    fun setPreferredLanguage(languageCode: String) {
        if (!::prefs.isInitialized) return

        prefs.edit(commit = true) {
            putString(KEY_PREF_LANG, languageCode)
        }
    }
}