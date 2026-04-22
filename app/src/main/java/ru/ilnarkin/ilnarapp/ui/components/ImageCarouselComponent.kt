package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.models.FileInfo


@Composable
fun ImageCarouselComponent(
	initialIndex: Int,
	images: List<FileInfo>,
	onClose: () -> Unit)
{

	val pagerState = rememberPagerState(
		initialPage = initialIndex,
		pageCount = { images.size }
	)

	Dialog(
		onDismissRequest = { onClose() },
		properties = DialogProperties(usePlatformDefaultWidth = false))
	{
		Surface(
			modifier = Modifier.fillMaxSize(),
			color = Color.Black)
		{
			Box(modifier = Modifier.fillMaxSize()) {

				HorizontalPager(
					state = pagerState,
					modifier = Modifier.fillMaxSize()
				) { index ->
					val zoomState = rememberZoomState(maxScale = 5f)

					LaunchedEffect(pagerState.currentPage) {
						zoomState.reset()
					}

					AsyncImage(
						model = images[index].url,
						contentDescription = null,
						modifier = Modifier
							.fillMaxSize()
							.zoomable(zoomState),
						contentScale = ContentScale.Fit
					)
				}


				IconButton(
					onClick = { onClose() },
					modifier = Modifier
						.statusBarsPadding()
						.align(Alignment.TopEnd)
						.padding(15.dp)
				) {
					Icon(
						painter = painterResource(R.drawable.ic_close),
						contentDescription = "Close",
						tint = Color.White
					)
				}
			}
		}

	}
}