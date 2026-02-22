package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.lifetrack.ltapp.R
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.ui.components.other.LTSnackbar
import org.lifetrack.ltapp.ui.components.profilescreen.LanguageCard
import org.lifetrack.ltapp.ui.navigation.LTNavDispatch
import org.lifetrack.ltapp.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(sharedPresenter: SharedPresenter) {
    val colorScheme = MaterialTheme.colorScheme
    val snackbarHostState = remember { SnackbarHostState() }
    val currentLanguage by sharedPresenter.ltSettings.collectAsStateWithLifecycle()
    val availableLanguages = sharedPresenter.AVAILABLE_LANGUAGES

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) { data -> LTSnackbar(data) } },
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

            items(availableLanguages.size) { index ->
                LanguageCard(
                    language = availableLanguages[index],
                    isSelected = availableLanguages[index].code == currentLanguage.preferredLanguage,
                    onClick = { sharedPresenter.onLanguageUpdate(availableLanguages[index].code) },
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


