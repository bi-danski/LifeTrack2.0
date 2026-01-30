package org.lifetrack.ltapp.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
//val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
//val PurpleGrey40 = Color(0xFF625b71)
//val Pink40 = Color(0xFF7D5260)
//val ShadowColor = Color(0xFF94A0AC)

val AvailableColor = Color(0xFF4CAF50)
val BusyColor = Color(0xFFF44336)
val DisabledColor = Color(0xFF9E9E9E)
val CardBackground = Color(0xFFF5F5F5)
val RatingColor = Color(0xFFFFC107)

val PremiumTeal = Color(0xFF26A69A)
val PremiumGold = Color(0xFFFDD835)
val PremiumPurple = Color(0xFFAB47BC)
val DeepOrange = Color(0xFFFF5722)

val GradientStart = Color(0xFFE0F7FA)
val GradientEnd = Color(0xFF80DEEA)

val CriticalAlert = Color(0xFFD32F2F)
val HighAlert = Color(0xFFFFA000)
val MediumAlert = Color(0xFFFFC107)
val LowAlert = Color(0xFF388E3C)

val HospitalBlue = Color(0xFF0077B6)
val EmergencyRed = Color(0xFFE53935)
//val SuccessGreen = Color(0xFF4CAF50)
//val WarningYellow = Color(0xFFFFC107)

val BlueFulani = Color(0xFF0288D1)

val LightColors = lightColorScheme(
    primary = Color(0xFF4CAF50),
    onPrimary = Color.White,
    secondary = BlueFulani,
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFE0E0E0),
    error = EmergencyRed,
    onError = Color.White,
    errorContainer = CriticalAlert.copy(alpha = 0.1f),
    onErrorContainer = CriticalAlert
)

val DarkColors = darkColorScheme(
    primary = Purple80,
    secondary = Color(0xFF4CAF50),
    tertiary = Pink80,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E)
)