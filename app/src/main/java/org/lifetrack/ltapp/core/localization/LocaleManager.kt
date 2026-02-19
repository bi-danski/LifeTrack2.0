package org.lifetrack.ltapp.core.localization

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object LocaleManager {
    private const val PREFS_NAME = "lt_locale_prefs"
    private const val KEY_PREF_LANG = "preferred_language"
    private lateinit var prefs: SharedPreferences
    private val _languageFlow = MutableStateFlow("en")
    @Suppress("unused")
    val languageFlow: StateFlow<String> = _languageFlow

    fun init(context: Context) {
        if (!::prefs.isInitialized) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val saved = prefs.getString(KEY_PREF_LANG, "en") ?: "en"
            _languageFlow.value = saved
        }
    }

    fun getPreferredLanguage(): String {
        return if (::prefs.isInitialized) {
            prefs.getString(KEY_PREF_LANG, _languageFlow.value) ?: _languageFlow.value
        } else {
            _languageFlow.value
        }
    }

    fun setPreferredLanguage(languageCode: String) {
        if (!::prefs.isInitialized) return
        val current = prefs.getString(KEY_PREF_LANG, _languageFlow.value) ?: _languageFlow.value
        if (current == languageCode) return
        prefs.edit { putString(KEY_PREF_LANG, languageCode) }
        _languageFlow.value = languageCode
    }
}