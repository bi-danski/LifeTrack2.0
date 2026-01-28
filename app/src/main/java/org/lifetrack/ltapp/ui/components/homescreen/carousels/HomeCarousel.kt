package org.lifetrack.ltapp.ui.components.homescreen.carousels

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.data.dclass.AppointmentStatus
import org.lifetrack.ltapp.presenter.UserPresenter
import org.lifetrack.ltapp.ui.components.homescreen.cards.DailyGoalsCard
import org.lifetrack.ltapp.ui.components.homescreen.cards.HealthSummaryCard
import org.lifetrack.ltapp.ui.components.homescreen.cards.TodayScheduleCard
import kotlin.math.absoluteValue


@SuppressLint("FrequentlyChangingValue")
@Composable
fun LtHomeCarousel(autoRotate: Boolean, itemsCount: Int, rotationInterval: Long = 5000L,
    userPresenter: UserPresenter,
    onEmergencyClickAction: () -> Unit,
    onEmergencyContactClickAction: () -> Unit
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
            contentPadding = PaddingValues(horizontal = 0.dp),
            modifier = Modifier
//                .height(200.dp)
                .wrapContentHeight()
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
                    }.fillMaxWidth().clip(RoundedCornerShape(20.dp)
                )
            ) {
                when (page) {
                    0 -> TodayScheduleCard(
                        appointmentCount = totalCount,
                        nextAppointment = nextUp,
                        onEmergencyClick = onEmergencyClickAction,
                        onEmergencyContactClick = onEmergencyContactClickAction
                        )
                    1 -> HealthSummaryCard()
                    2 -> DailyGoalsCard()
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
