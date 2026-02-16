package org.lifetrack.ltapp.ui.components.loginscreen

//import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.lifetrack.ltapp.R
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.ui.theme.GreenFulani
import org.lifetrack.ltapp.ui.theme.Purple40
import org.lifetrack.ltapp.ui.theme.Purple80
import org.lifetrack.ltapp.ui.theme.ShadowColor

@Composable
fun LTBrandAppBar(modifier: Modifier = Modifier, sharedPresenter: SharedPresenter) {
    var scale by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(Unit) {
        while (sharedPresenter.ltSettings.value.animations) {
            scale = 1.02f
            delay(500)
            scale = 1f
            delay(500)
        }
    }

    Column(
        modifier = modifier
            .fillMaxHeight()
//            .height(120.dp)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.lifetrack_icon_logo_dark),
            contentDescription = "LifeTrack Logo",
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    compositingStrategy = CompositingStrategy.Offscreen
                )
        )
        Text(
            text = "LifeTrack",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = if (isSystemInDarkTheme()) Purple80 else Purple40
        )
        Text(
            text = "Better Healthcare, Simplified",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Black
            ),
            color = if (isSystemInDarkTheme()) ShadowColor else GreenFulani
        )
    }
}
