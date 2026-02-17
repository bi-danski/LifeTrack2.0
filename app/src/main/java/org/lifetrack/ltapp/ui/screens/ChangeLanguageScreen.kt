package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.lifetrack.ltapp.R
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.ui.navigation.LTNavDispatch
import org.lifetrack.ltapp.ui.theme.Purple40
import org.lifetrack.ltapp.ui.theme.Purple80

data class LanguageOption(
    val code: String,
    val name: String,
    val nativeName: String,
    val flag: String
)

val AVAILABLE_LANGUAGES = listOf(
    LanguageOption("en", "English", "English", "🇬🇧"),
    LanguageOption("es", "Spanish", "Español", "🇪🇸"),
    LanguageOption("fr", "French", "Français", "🇫🇷"),
    LanguageOption("de", "German", "Deutsch", "🇩🇪"),
    LanguageOption("it", "Italian", "Italiano", "🇮🇹"),
    LanguageOption("pt", "Portuguese", "Português", "🇵🇹"),
    LanguageOption("ru", "Russian", "Русский", "🇷🇺"),
    LanguageOption("zh", "Chinese", "中文", "🇨🇳"),
    LanguageOption("ja", "Japanese", "日本語", "🇯🇵"),
    LanguageOption("ko", "Korean", "한국어", "🇰🇷"),
    LanguageOption("ar", "Arabic", "العربية", "🇸🇦"),
    LanguageOption("hi", "Hindi", "हिन्दी", "🇮🇳"),
    LanguageOption("sw", "Swahili", "Kiswahili", "🇰🇪"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeLanguageScreen(sharedPresenter: SharedPresenter) {
    val colorScheme = MaterialTheme.colorScheme
    val snackbarHostState = remember { SnackbarHostState() }
    val currentLanguage by sharedPresenter.currentLanguage.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        snackbarHostState.showSnackbar(
            message = "Select your preferred language",
            duration = SnackbarDuration.Short
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.change_language)) },
                navigationIcon = {
                    IconButton(onClick = { LTNavDispatch.navigateBack() }) {
                        Icon(Icons.Default.ArrowCircleLeft, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) colorScheme.primary.copy(0.1f) else Purple40,
                    titleContentColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .background(colorScheme.background)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.select_preferred_language),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.choose_language_description),
                    fontSize = 14.sp,
                    color = colorScheme.onBackground.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            items(AVAILABLE_LANGUAGES.size) { index ->
                LanguageCard(
                    language = AVAILABLE_LANGUAGES[index],
                    isSelected = AVAILABLE_LANGUAGES[index].code == currentLanguage,
                    onClick = {
                        // Removed direct Locale.setDefault here. The presenter persists the preference
                        // and MainActivity/ProvideLocalization will apply the selected locale centrally.
                        sharedPresenter.updateLanguage(AVAILABLE_LANGUAGES[index].code)
                    },
                    colorScheme = colorScheme,
                    isDark = isSystemInDarkTheme()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.language_change_note),
                    fontSize = 12.sp,
                    color = colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun LanguageCard(
    language: LanguageOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    colorScheme: androidx.compose.material3.ColorScheme,
    isDark: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                if (isDark) Purple80.copy(alpha = 0.3f) else Purple40.copy(alpha = 0.15f)
            } else {
                if (isDark) Color(0xFF1E1E1E) else colorScheme.surfaceVariant.copy(alpha = 0.5f)
            }
        ),
        border = if (isSelected) {
            BorderStroke(2.dp, if (isDark) Purple80 else Purple40)
        } else {
            null
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flag Emoji
            Text(
                text = language.flag,
                fontSize = 32.sp,
                modifier = Modifier.padding(end = 16.dp)
            )

            // Language Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = language.nativeName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground
                )
                Text(
                    text = language.name,
                    fontSize = 13.sp,
                    color = colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = if (isDark) Purple80 else Purple40,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
