package com.nei.ichigo.core.designsystem

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.nei.ichigo.core.designsystem.utils.AnimatedLoaderBarConstants
import kotlin.math.roundToInt

/*

import android.animation.ObjectAnimator
import android.content.Context
import android.text.format.DateUtils.SECOND_IN_MILLIS
import android.view.View.SCALE_X
import android.view.View.SCALE_Y
import android.view.View.TRANSLATION_X
import android.view.animation.PathInterpolator
private const val CONTROL_X1 = 0.81f
private const val CONTROL_Y1 = 0.0f
private const val CONTROL_X2 = 0.63f
private const val CONTROL_Y2 = 1f
private const val TEN_MILLIS = 10L
class LoaderBarView : ConstraintLayout {
    private lateinit var binding: LayoutLoaderBarBinding
    private val pathInterpolator = PathInterpolator(CONTROL_X1, CONTROL_Y1, CONTROL_X2, CONTROL_Y2)
    private val translationAnimator = ObjectAnimator().apply {
        setProperty(TRANSLATION_X)
        repeatCount = ObjectAnimator.INFINITE
        repeatMode = ObjectAnimator.REVERSE
        duration = SECOND_IN_MILLIS
        interpolator = pathInterpolator
    }
    private val scaleAnimator = ObjectAnimator().apply {
        duration = SECOND_IN_MILLIS / 2
        setProperty(SCALE_X)
        startDelay = TEN_MILLIS
        interpolator = pathInterpolator
    }
    private val scaleYAnimator = ObjectAnimator().apply {
        duration = SECOND_IN_MILLIS / 2
        setProperty(SCALE_Y)
        startDelay = TEN_MILLIS
        interpolator = pathInterpolator
    }
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init()
    }
    private fun init() {
        binding = LayoutLoaderBarBinding.inflate(LayoutInflater.from(context), this, true)
        binding.apply {
            root.viewTreeObserver.addOnPreDrawListener(
                object : OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        root.viewTreeObserver.removeOnPreDrawListener(this)
                        val endX = root.width.toFloat() - flLoadingBarProgress.width.toFloat()
                        translationAnimator.apply {
                            target = flLoadingBarProgress
                            setFloatValues(endX)
                            if (flLoadingBarProgress.width == 0) {
                                duration = 0L
                            }
                            start()
                        }
                        return true
                    }
                }
            )
        }
    }
    fun finishAnimation(onAnimationFinished: () -> Unit) {
        if (context.areAnimationsDisabled()) {
            onAnimationFinished.invoke()
            return
        }
        binding.root.viewTreeObserver.addOnPreDrawListener {
            setScaleAnimator(onAnimationFinished)
            setTranslationAnimatorListener()
            true
        }
    }
    private fun setScaleAnimator(onAnimationFinished: () -> Unit) = binding.apply {
        val parentWidth = root.width.toFloat()
        val progressWidth = if (flLoadingBarProgress.width.toFloat() == 0f) 1f else flLoadingBarProgress.width.toFloat()
        val scale = ((parentWidth + progressWidth) / progressWidth) * 2
        scaleAnimator.apply {
            target = flLoadingBarProgress
            setFloatValues(1.0F, scale)
            addListener(object : AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                    // Do nothing
                }
                override fun onAnimationEnd(animation: Animator) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        onAnimationFinished.invoke()
                    }, TEN_MILLIS)
                }
                override fun onAnimationCancel(p0: Animator) {
                    // Do nothing
                }
                override fun onAnimationRepeat(p0: Animator) {
                    // Do nothing
                }
            })
            if (flLoadingBarProgress.width == 0) {
                duration = 0L
            }
        }
        scaleYAnimator.apply {
            target = flLoadingBarProgress
            setFloatValues(1.0F, scale)
            if (flLoadingBarProgress.width == 0) {
                duration = 0L
            }
        }
    }
    private fun setTranslationAnimatorListener() = translationAnimator.addListener(object : AnimatorListener {
        override fun onAnimationStart(p0: Animator) {
            // Do nothing
        }
        override fun onAnimationEnd(animation: Animator) {
            // Do nothing
        }
        override fun onAnimationCancel(p0: Animator) {
            // Do nothing
        }
        override fun onAnimationRepeat(p0: Animator) {
            translationAnimator.cancel()
            scaleAnimator.start()
            scaleYAnimator.start()
        }
    })
    fun pause() = translationAnimator.pause()
    fun resume() = translationAnimator.resume()
    fun cancelAnimation() {
        translationAnimator.cancel()
        scaleAnimator.cancel()
        scaleYAnimator.cancel()
    }
}*/



@Composable
fun AnimatedLoaderBarL(
    modifier: Modifier = Modifier,
    isIntermittent: Boolean,
    onAnimationFinished: () -> Unit
) {
    val barWidth = 20.dp
    val barHeight = 8.dp
    val cornerRadius = 4.dp

// Estado para guardar el ancho del contenedor en píxeles
    var containerWidthPx by remember { mutableIntStateOf(0) }

    val customPathEasing = remember {
        AnimatedLoaderBarConstants.easing
    }

    var shouldAnimate by remember { mutableStateOf(false) }
    var shouldScale by remember { mutableStateOf(false) }

    val animatedTranslationX by animateFloatAsState(
        targetValue = if (shouldAnimate) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = AnimatedLoaderBarConstants.INFINITY_DURATION_MILLIS,
                easing = AnimatedLoaderBarConstants.easing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "translation_animation"
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (shouldScale) 20f else 1f,
        animationSpec = tween(
            durationMillis = AnimatedLoaderBarConstants.SCALE_DURATION_MILLIS,
            easing = customPathEasing
        ),
        label = "scale_animation",
        finishedListener = {
            if (shouldScale) {
                onAnimationFinished()
            }
        }
    )

    LaunchedEffect(isIntermittent) {
        // shouldAnimate = isIntermittent
        shouldScale = !isIntermittent
    }

    LaunchedEffect(Unit) {
        shouldAnimate = true
    }

    val density = LocalDensity.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight)
            .background(Color(0xFFE3F2FD), RoundedCornerShape(cornerRadius))
            .onSizeChanged { size -> containerWidthPx = size.width }
            .clip(RoundedCornerShape(cornerRadius))
    ) {
        val barWidthPx = with(density) { barWidth.toPx() }
        val translationPx = (containerWidthPx - barWidthPx) * animatedTranslationX

        Box(
            modifier = Modifier
                .width(barWidth)
                .fillMaxHeight()
                .offset { IntOffset(x = translationPx.roundToInt(), y = 0) }
                .scale(animatedScale)
                .background(Color(0xFF2196F3), RoundedCornerShape(cornerRadius))
        )
    }
}


@Composable
fun AnimatedLoaderBar2(
    modifier: Modifier = Modifier,
    onAnimationFinished: () -> Unit
) {
    val barWidth = 20.dp
    val barHeight = 8.dp
    val cornerRadius = 4.dp

// Estado para guardar el ancho del contenedor en píxeles
    var containerWidthPx by remember { mutableIntStateOf(0) }

    val customPathEasing = remember {
        AnimatedLoaderBarConstants.easing
    }

    var shouldAnimate by remember { mutableStateOf(false) }
    val shouldScale by remember { mutableStateOf(false) }

    val animatedTranslationX by animateFloatAsState(
        targetValue = if (shouldAnimate) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = customPathEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "translation_animation"
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (shouldScale) 20f else 1f,
        animationSpec = tween(durationMillis = 500, easing = customPathEasing),
        label = "scale_animation",
        finishedListener = {
            if (shouldScale) {
                onAnimationFinished()
            }
        }
    )

    LaunchedEffect(Unit) {
        shouldAnimate = true
    }

    val density = LocalDensity.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight)
            .background(Color(0xFFE3F2FD), RoundedCornerShape(cornerRadius))
            .onSizeChanged { size ->
                containerWidthPx = size.width
            }
    ) {
        val barWidthPx = with(density) { barWidth.toPx() }
        val translationPx = (containerWidthPx - barWidthPx) * animatedTranslationX

        Box(
            modifier = Modifier
                .width(barWidth)
                .fillMaxHeight()
                .offset { IntOffset(x = translationPx.roundToInt(), y = 0) }
                .scale(animatedScale)
                .background(Color(0xFF2196F3), RoundedCornerShape(cornerRadius))
        )
    }
}

@Preview
@Composable
fun AnimatedLoaderBarPreview() {
    Scaffold {
        var isIntermittent by remember { mutableStateOf(true) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedLoaderBar2(modifier = Modifier) {}

            Spacer(Modifier.weight(1f))

            Button(onClick = {
                isIntermittent = !isIntermittent
            }) {
                Text(if (isIntermittent) "Expand" else "Intermittent")
            }
        }
    }
}
