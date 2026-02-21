package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lifetrack.ltapp.R
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.ui.components.aboutscreen.FeatureItem
import org.lifetrack.ltapp.ui.components.supportscreen.SectionCard
import org.lifetrack.ltapp.ui.navigation.LTNavDispatch
import org.lifetrack.ltapp.ui.theme.Purple40


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(sharedPresenter: SharedPresenter) {
    val ltSettings by sharedPresenter.ltSettings.collectAsState()
    val colorScheme = MaterialTheme.colorScheme
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "", //  "About LifeTrack",
                        style = MaterialTheme.typography.headlineSmall.copy()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { LTNavDispatch.navigateBack() }) {
                        Icon(
                            Icons.Default.ArrowCircleLeft,
                            contentDescription = "Back",
//                            tint = Color(0xFF2E5EAA)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) colorScheme.background else Purple40,
                    titleContentColor = colorScheme.onPrimaryContainer,
                    navigationIconContentColor = colorScheme.onPrimaryContainer
                )
            )
        },
        containerColor = colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4A90E2)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = "App Icon",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.companion),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(
                        0xFF2E5EAA
                    ),
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.companionDesc),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant, // Color(0xFF6C757D),
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = colorScheme.background
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(0.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SectionCard(
                        title = stringResource(R.string.about_lt),
                        icon = Icons.Filled.Info
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.app_description),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.version),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Black
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = ltSettings.appVersion,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Black
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = colorScheme.background
                    )
                    Text(
                        text = stringResource(R.string.key_features),
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(
                                0xFF2E5EAA
                            )
                        )
                    )

                    FeatureItem(
                        iconColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(
                            0xFF4A90E2
                        ),
                        title = stringResource(R.string.med_timeline_records),
                        description = stringResource(R.string.med_timeline_records_desc)
                    )
                    FeatureItem(
                        iconColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(
                            0xFF4A90E2
                        ),
                        title = stringResource(R.string.tele_med_appointment),
                        description = stringResource(R.string.tele_med_appointment_desc)
                    )
                    FeatureItem(
                        iconColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(
                            0xFF4A90E2
                        ),
                        title = stringResource(R.string.med_reminder),
                        description = stringResource(R.string.med_reminder_desc)
                    )
                    FeatureItem(
                        iconColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(
                            0xFF4A90E2
                        ),
                        title = stringResource(R.string.emergency_triggers_and_alerts),
                        description = stringResource(R.string.emergency_triggers_and_alerts_desc)
                    )
                    FeatureItem(
                        iconColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(
                            0xFF4A90E2
                        ),
                        title = stringResource(R.string.hiva),
                        description = stringResource(R.string.hiva_desc)
                    )
                    FeatureItem(
                        iconColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(
                            0xFF4A90E2
                        ),
                        title = stringResource(R.string.alma),
                        description = stringResource(R.string.alma_desc)
                    )
                    FeatureItem(
                        iconColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(
                            0xFF4A90E2
                        ),
                        title = stringResource(R.string.presc_manager),
                        description = stringResource(R.string.presc_manager_desc)
                    )
                    FeatureItem(
                        iconColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(
                            0xFF4A90E2
                        ),
                        title = stringResource(R.string.fuva),
                        description = stringResource(R.string.fuva_desc)
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = colorScheme.background
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = colorScheme.background),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(0.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SectionCard(
                        title = stringResource(R.string.connect_with_us),
                        icon = Icons.Filled.Call
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.whoAmI),
//                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(R.string.whoAmISite),
//                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    SectionCard(
                        title = stringResource(R.string.developer),
                        icon = Icons.Filled.PeopleAlt
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.whoWeAre),
//                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant, // color = ThatOneColor
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(R.string.whatWeDo),
//                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(R.string.copyright),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF6C757D),
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}


