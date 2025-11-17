package org.lifetrack.ltapp.view.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val AvailableColor = Color(0xFF4CAF50)  // Green for available doctors
val BusyColor = Color(0xFFF44336)      // Red for busy doctors
val DisabledColor = Color(0xFF9E9E9E)  // Grey for disabled state
val CardBackground = Color(0xFFF5F5F5) // Light card background
val RatingColor = Color(0xFFFFC107)    // Amber for ratings
val PremiumTeal = Color(0xFF26A69A)    // Teal for premium features
val PremiumGold = Color(0xFFFDD835)    // Gold accent
val PremiumPurple = Color(0xFFAB47BC)  // Purple accent
val GradientStart = Color(0xFFE0F7FA)  // Light gradient start
val GradientEnd = Color(0xFF80DEEA)    // Teal gradient end

val CriticalAlert = Color(0xFFD32F2F)
val HighAlert = Color(0xFFFFA000)
val MediumAlert = Color(0xFFFFC107)
val LowAlert = Color(0xFF388E3C)


val LightColors = lightColorScheme(
    primary = Color(0xFF4CAF50), // Green
    onPrimary = Color.White,
    secondary = Color(0xFF0288D1), // Blue
    background = Color(0xFFF5F5F5), // Light gray
    surface = Color(0xFFE0E0E0)
)

val DarkColors = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)