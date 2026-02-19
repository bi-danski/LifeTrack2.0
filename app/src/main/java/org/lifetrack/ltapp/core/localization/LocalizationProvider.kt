package org.lifetrack.ltapp.core.localization

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LocalizationProvider {

    @Suppress("UNUSED_PARAMETER")
    fun setLocale(context: Context, languageCode: String): Boolean {
        return try {
            val localeList = LocaleListCompat.forLanguageTags(languageCode)
            AppCompatDelegate.setApplicationLocales(localeList)
            val locale = Locale.forLanguageTag(languageCode)
            Locale.setDefault(locale)
            AppCompatDelegate.getApplicationLocales() == localeList
        } catch (t: Throwable) {
            false
        }
    }

    fun getLocalizedContext(context: Context, languageCode: String): Context {
        val locale = Locale.forLanguageTag(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}