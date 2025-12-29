package org.lifetrack.ltapp.ui.components.carousels

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.data.dclass.AppointmentStatus
import org.lifetrack.ltapp.presenter.UserPresenter
import org.lifetrack.ltapp.ui.components.homescreen.HealthSummaryCard
import kotlin.math.absoluteValue
import org.lifetrack.ltapp.ui.components.homescreen.TodayScheduleCard


@SuppressLint("FrequentlyChangingValue")
@Composable
fun LtHomeCarousel(
    autoRotate: Boolean,
    itemsCount: Int,
    rotationInterval: Long = 5000L,
    userPresenter: UserPresenter,
    onEmergencyClickAction: () -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { itemsCount }
    )
    val nextUp by userPresenter.nextUpcomingAppointment.collectAsState()
    val totalCount = userPresenter.getCountForStatus(AppointmentStatus.UPCOMING)
    val scope = rememberCoroutineScope()

    if (autoRotate) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(rotationInterval)
                scope.launch {
                    val nextPage = (pagerState.currentPage + 1) % itemsCount
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 10.dp),
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        ) { page ->
            val pageOffset = (pagerState.currentPage - page + pagerState.currentPageOffsetFraction).absoluteValue
            val scale = 1f - (pageOffset * 0.18f)
            val rotY = pageOffset * 14f
            val pAlpha = 1f - (pageOffset * 0.30f)

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        alpha = pAlpha
                        rotationY = rotY
                        cameraDistance = 10_000f
                    }
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
            ) {
                when (page) {
                    0 -> TodayScheduleCard(
                        appointmentCount = totalCount,
                        nextAppointment = nextUp,
                        onEmergencyClick = onEmergencyClickAction
                    )
                    1 -> HealthSummaryCard()
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        LtCarouselIndicator(pagerState, itemsCount)
    }
}

@Composable
fun LtCarouselIndicator(pagerState: PagerState, pageCount: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 6.dp)
    ) {
        repeat(pageCount) { index ->
            val color = if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(color)
            )
        }
    }
}
