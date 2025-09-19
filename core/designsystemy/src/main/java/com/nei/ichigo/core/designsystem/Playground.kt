@file:Suppress("unused")

package com.nei.ichigo.core.designsystem

/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.PlaceHolderSize.Companion.animatedSize
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils
import androidx.compose.ui.unit.sp
import com.nei.ichigo.core.designsystem.utils.AnimatedLoaderBarConstants
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
fun ContainerTransformDemo(model: MyModel = remember { MyModel().apply { selected = items[1] } }) {
    SharedTransitionLayout {
        LaunchedEffect(key1 = Unit) {
            while (true) {
                delay(2500)
                if (model.selected == null) {
                    model.selected = model.items[1]
                } else {
                    model.selected = null
                }
            }
        }
        AnimatedContent(
            model.selected,
            transitionSpec = {
                fadeIn(tween(600)) togetherWith
                        fadeOut(tween(600)) using SizeTransform { _, _ ->
                    spring()
                }
            },
            label = ""
        ) {
            // TODO: Double check on container transform scrolling
            if (it != null) {
                DetailView(model = model, selected = it, model.items[6])
            } else {
                GridView(model = model)
            }
        }
    }
}

context(scope: SharedTransitionScope)
@Composable
fun Details(kitty: Kitty) {
    with(scope) {
        Column(
            Modifier
                .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                .fillMaxHeight()
                .wrapContentHeight(Alignment.Top)
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 10.dp, end = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Spacer(Modifier.size(20.dp))
                    Text(
                        kitty.name,
                        fontSize = 25.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Text(
                        kitty.breed,
                        fontSize = 22.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(start = 10.dp)
                    )
                    Spacer(Modifier.size(10.dp))
                }
                Spacer(Modifier.weight(1f))
                Icon(
                    Icons.Outlined.Favorite,
                    contentDescription = null,
                    Modifier
                        .background(Color(0xffffddee), CircleShape)
                        .padding(10.dp)
                )
                Spacer(Modifier.size(10.dp))
            }
            Box(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .height(2.dp)
                    .fillMaxWidth()
                    .background(Color(0xffeeeeee))
            )
            Text(
                text =
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent fringilla" +
                            " mollis efficitur. Maecenas sit amet urna eu urna blandit suscipit efficitur" +
                            " eget mauris. Nullam eget aliquet ligula. Nunc id euismod elit. Morbi aliquam" +
                            " enim eros, eget consequat dolor consequat id. Quisque elementum faucibus" +
                            " congue. Curabitur mollis aliquet turpis, ut pellentesque justo eleifend nec.\n" +
                            "\n" +
                            "Suspendisse ac consequat turpis, euismod lacinia quam. Nulla lacinia tellus" +
                            " eu felis tristique ultricies. Vivamus et ultricies dolor. Orci varius" +
                            " natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus." +
                            " Ut gravida porttitor arcu elementum elementum. Phasellus ultrices vel turpis" +
                            " volutpat mollis. Vivamus leo diam, placerat quis leo efficitur, ultrices" +
                            " placerat ex. Nullam mollis et metus ac ultricies. Ut ligula metus, congue" +
                            " gravida metus in, vestibulum posuere velit. Sed et ex nisl. Fusce tempor" +
                            " odio eget sapien pellentesque, sed cursus velit fringilla. Nullam odio" +
                            " ipsum, eleifend non consectetur vitae, congue id libero. Etiam tincidunt" +
                            " mauris at urna dictum ornare.\n" +
                            "\n" +
                            "Etiam at facilisis ex. Sed quis arcu diam. Quisque semper pharetra leo eget" +
                            " fermentum. Nulla dapibus eget mi id porta. Nunc quis sodales nulla, eget" +
                            " commodo sem. Donec lacus enim, pharetra non risus nec, eleifend ultrices" +
                            " augue. Donec sit amet orci porttitor, auctor mauris et, facilisis dolor." +
                            " Nullam mattis luctus orci at pulvinar.\n" +
                            "\n" +
                            "Sed accumsan est massa, ut aliquam nulla dignissim id. Suspendisse in urna" +
                            " condimentum, convallis purus at, molestie nisi. In hac habitasse platea" +
                            " dictumst. Pellentesque id justo quam. Cras iaculis tellus libero, eu" +
                            " feugiat ex pharetra eget. Nunc ultrices, magna ut gravida egestas, mauris" +
                            " justo blandit sapien, eget congue nisi felis congue diam. Mauris at felis" +
                            " vitae erat porta auctor. Pellentesque iaculis sem metus. Phasellus quam" +
                            " neque, congue at est eget, sodales interdum justo. Aenean a pharetra dui." +
                            " Morbi odio nibh, hendrerit vulputate odio eget, sollicitudin egestas ex." +
                            " Fusce nisl ex, fermentum a ultrices id, rhoncus vitae urna. Aliquam quis" +
                            " lobortis turpis.\n" +
                            "\n",
                modifier = Modifier.skipToLookaheadSize(),
                color = Color.Gray,
                fontSize = 15.sp,
            )
        }
    }
}

context(scope: AnimatedVisibilityScope, transitionScope: SharedTransitionScope)
@Suppress("UNUSED_PARAMETER")
@Composable
fun DetailView(
    model: MyModel,
    selected: Kitty,
    next: Kitty?
) {
    with(transitionScope) {
        Column(
            Modifier
                .sharedBounds(
                    rememberSharedContentState(key = "container + ${selected.id}"),
                    scope,
                    clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(20.dp))
                )
        ) {
            Row(Modifier.fillMaxHeight(0.5f)) {
                Image(
                    painter = painterResource(selected.photoResId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(10.dp)
                        .sharedElement(
                            rememberSharedContentState(key = selected.id),
                            scope,
                            placeHolderSize = animatedSize
                        )
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(20.dp))
                )
                if (next != null) {
                    Image(
                        painter = painterResource(next.photoResId),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 10.dp, end = 10.dp)
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(20.dp))
                            .blur(10.dp)
                    )
                }
            }
            Details(kitty = selected)
        }
    }
}

context(scope: AnimatedVisibilityScope, transitionScope: SharedTransitionScope)
@Composable
fun GridView(model: MyModel) {
    with(transitionScope) {
        with(scope) {
            Box(Modifier.background(lessVibrantPurple)) {
                Box(
                    Modifier
                        .padding(20.dp)
                        .renderInSharedTransitionScopeOverlay(zIndexInOverlay = 2f)
                        .animateEnterExit(fadeIn(), fadeOut())
                ) {
                    // SearchBar(expanded = false)
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(top = 90.dp)
                ) {
                    items(6) {
                        KittyItem(model.items[it])
                    }
                }
            }
        }
    }
}

class MyModel {
    val items = mutableListOf<Kitty>(
        /*
        Kitty("Waffle", R.drawable.ic_launcher_foreground, "American Short Hair", 0),
        Kitty("油条", R.drawable.ic_launcher_foreground, "Tabby", 1),
        Kitty("Cowboy", R.drawable.ic_launcher_foreground, "American Short Hair", 2),
        Kitty("Pepper", R.drawable.ic_launcher_foreground, "Tabby", 3),
        Kitty("Unknown", R.drawable.ic_launcher_foreground, "Unknown", 4),
        Kitty("Unknown", R.drawable.ic_launcher_foreground, "Unknown", 5),
        Kitty("YT", R.drawable.ic_launcher_foreground, "Tabby", 6),
        */
    )
    var selected: Kitty? by mutableStateOf(null)
}

context(scope: AnimatedVisibilityScope, transitionScope: SharedTransitionScope)
@Composable
fun KittyItem(kitty: Kitty) {
    with(transitionScope) {
        Column(
            Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                .sharedBounds(
                    rememberSharedContentState(key = "container + ${kitty.id}"),
                    scope
                )
                .background(Color.White, RoundedCornerShape(20.dp))
        ) {
            Image(
                painter = painterResource(kitty.photoResId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = kitty.id),
                        scope,
                        placeHolderSize = animatedSize
                    )
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xffaaaaaa))
            )
            Spacer(Modifier.size(10.dp))
            Text(
                kitty.name,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(Modifier.size(5.dp))
            Text(
                kitty.breed,
                fontSize = 15.sp,
                color = Color.Gray,
                modifier = Modifier
                    .padding(start = 10.dp)
            )
            Spacer(Modifier.size(10.dp))
        }
    }
}

data class Kitty(val name: String, val photoResId: Int, val breed: String, val id: Int) {
    override fun equals(other: Any?): Boolean {
        return other is Kitty && other.id == id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

private val lessVibrantPurple = Color(0xfff3edf7)

data class Snack(val name: String, val description: String, val image: Int)

private val listSnacks = listOf<Snack>(
    /*
    Snack("Cupcake", "", R.drawable.ic_launcher_foreground),
    Snack("Donut", "", R.drawable.ic_launcher_foreground),
    Snack("Eclair", "", R.drawable.ic_launcher_foreground),
    Snack("Froyo", "", R.drawable.ic_launcher_foreground),
    Snack("Gingerbread", "", R.drawable.ic_launcher_foreground),
    Snack("Honeycomb", "", R.drawable.ic_launcher_foreground),
    */
)

val shapeForSharedElement = RoundedCornerShape(16.dp)

@Preview
@Composable
private fun AnimatedVisibilitySharedElementShortenedExample() {
    // [START android_compose_shared_elements_animated_visibility]
    var selectedSnack by remember { mutableStateOf<Snack?>(null) }

    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            // [START_EXCLUDE]
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray.copy(alpha = 0.5f))
                .padding(16.dp),
            // verticalArrangement = Arrangement.spacedBy(8.dp)
            // [END_EXCLUDE]
        ) {
            items(listSnacks) { snack ->
                AnimatedVisibility(
                    visible = snack != selectedSnack,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut(),
                    modifier = Modifier.animateItem()
                ) {
                    Box(
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "${snack.name}-bounds"),
                                // Using the scope provided by AnimatedVisibility
                                animatedVisibilityScope = this,
                                clipInOverlayDuringTransition = OverlayClip(shapeForSharedElement)
                            )
                            .background(Color.White, shapeForSharedElement)
                            .clip(shapeForSharedElement)
                    ) {
                        SnackContents(
                            snack = snack,
                            modifier = Modifier.sharedElement(
                                sharedContentState = rememberSharedContentState(key = snack.name),
                                animatedVisibilityScope = this@AnimatedVisibility
                            ),
                            onClick = {
                                selectedSnack = snack
                            }
                        )
                    }
                }
            }
        }
        // Contains matching AnimatedContent with sharedBounds modifiers.
        SnackEditDetails(
            snack = selectedSnack,
            onConfirmClick = {
                selectedSnack = null
            }
        )
    }
    // [END android_compose_shared_elements_animated_visibility]
}

@Composable
fun SharedTransitionScope.SnackEditDetails(
    snack: Snack?,
    modifier: Modifier = Modifier,
    onConfirmClick: () -> Unit
) {
    AnimatedContent(
        modifier = modifier,
        targetState = snack,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "SnackEditDetails"
    ) { targetSnack ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (targetSnack != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onConfirmClick()
                        }
                        .background(Color.Black.copy(alpha = 0.5f))
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "${targetSnack.name}-bounds"),
                            animatedVisibilityScope = this@AnimatedContent,
                            clipInOverlayDuringTransition = OverlayClip(shapeForSharedElement)
                        )
                        .background(Color.White, shapeForSharedElement)
                        .clip(shapeForSharedElement)
                ) {

                    SnackContents(
                        snack = targetSnack,
                        modifier = Modifier.sharedElement(
                            sharedContentState = rememberSharedContentState(key = targetSnack.name),
                            animatedVisibilityScope = this@AnimatedContent,
                        ),
                        onClick = {
                            onConfirmClick()
                        }
                    )
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp, end = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { onConfirmClick() }) {
                            Text(text = "Save changes")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SnackContents(
    snack: Snack,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
    ) {
        Image(
            painter = painterResource(id = snack.image),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(20f / 9f),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
        Text(
            text = snack.name,
            modifier = Modifier
                .wrapContentWidth()
                .padding(8.dp),
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun ZoomableBox(
    modifier: Modifier = Modifier,
    maxZoom: Float = 5f,
    minZoom: Float = 1f,
    doubleTapZoom: Float = 2f,
    content: @Composable (Modifier) -> Unit
) {
    val scale = remember { Animatable(1f) }
    val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val coroutineScope = rememberCoroutineScope()
    val transformableState = rememberTransformableState { zoomChange, offsetChange, _ ->
        coroutineScope.launch {
            val newScale = (scale.value * zoomChange).coerceIn(minZoom, maxZoom)
            scale.snapTo(newScale)
            offset.snapTo(offset.value + offsetChange)
        }
    }


    // Detect end of gesture and animate back
    LaunchedEffect(transformableState.isTransformInProgress) {
        if (!transformableState.isTransformInProgress) {
            if (scale.value <= 1f) {
                scale.animateTo(1f, animationSpec = tween(300))
                offset.animateTo(Offset.Zero, animationSpec = tween(300))
            }
        }
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        coroutineScope.launch {
                            if (scale.value > 1f) {
                                scale.animateTo(1f, animationSpec = tween(300))
                                offset.animateTo(Offset.Zero, animationSpec = tween(300))
                            } else {
                                scale.animateTo(doubleTapZoom, animationSpec = tween(300))
                            }
                        }
                    }
                )
            }
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value,
                translationX = offset.value.x,
                translationY = offset.value.y
            )
            .transformable(state = transformableState)
            .clipToBounds()
    ) {
        content(Modifier.fillMaxSize())
    }
}

@Composable
fun ZoomableBox3(
    modifier: Modifier = Modifier,
    maxZoom: Float = 5f,
    minZoom: Float = 1f,
    doubleTapZoom: Float = 2f,
    content: @Composable (Modifier) -> Unit
) {
    val scale = remember { Animatable(1f) }
    val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }

    val coroutineScope = rememberCoroutineScope()

    val transformableState = rememberTransformableState { zoomChange, offsetChange, _ ->
        coroutineScope.launch {
            val newScale = (scale.value * zoomChange).coerceIn(minZoom, maxZoom)
            scale.snapTo(newScale)
            offset.snapTo(offset.value + offsetChange)
        }
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        coroutineScope.launch {
                            if (scale.value > 1f) {
                                scale.animateTo(1f, animationSpec = tween(300))
                                offset.animateTo(Offset.Zero, animationSpec = tween(300))
                            } else {
                                scale.animateTo(doubleTapZoom, animationSpec = tween(300))
                            }
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.changes.all { it.changedToUp() }) {
                            // On gesture end
                            if (scale.value <= 1f) {
                                coroutineScope.launch {
                                    scale.animateTo(1f, animationSpec = tween(300))
                                    offset.animateTo(Offset.Zero, animationSpec = tween(300))
                                }
                            }
                        }
                    }
                }
            }
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value,
                translationX = offset.value.x,
                translationY = offset.value.y
            )
            .transformable(state = transformableState)
            .clipToBounds(),
        contentAlignment = Alignment.Center
    ) {
        content(Modifier.fillMaxSize())
    }
}

@Composable
fun ZoomableBox2(
    modifier: Modifier = Modifier,
    maxZoom: Float = 5f,
    minZoom: Float = 1f,
    doubleTapZoom: Float = 2f,
    content: @Composable (Modifier) -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val transformableState =
        rememberTransformableState { zoomChange, offsetChange, _ ->
            val newScale = (scale * zoomChange).coerceIn(minZoom, maxZoom)
            scale = newScale
            offset += offsetChange
        }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        coroutineScope.launch {
                            if (scale > 1f) {
                                scale = 1f
                                offset = Offset.Zero
                            } else {
                                scale = doubleTapZoom
                            }
                        }
                    }
                )
            }
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            )
            .transformable(state = transformableState)
            .clipToBounds()
    ) {
        content(Modifier.fillMaxSize())
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
@Preview
fun AnimatedLine2(
    isIntermittent: Boolean = true,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    rememberInfiniteTransition(label = "intermittent")

    // Tamaño y posición del indicador
    val indicatorWidth = 20.dp
    val indicatorOffset = remember { Animatable(0f) }
    val indicatorWidthAnim = remember { Animatable(indicatorWidth.value) }

    val density = LocalDensity.current

    LaunchedEffect(isIntermittent) {
        if (isIntermittent) {
            // Resetear ancho a 20dp y arrancar anim de ida y vuelta
            indicatorWidthAnim.snapTo(indicatorWidth.value)
            scope.launch {
                while (true) {
                    indicatorOffset.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(1000, easing = LinearEasing)
                    )
                    indicatorOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(1000, easing = LinearEasing)
                    )
                }
            }
        } else {
            // Expandirse ocupando todo el ancho disponible en 5000ms
            scope.launch {
                indicatorWidthAnim.animateTo(
                    targetValue = Float.POSITIVE_INFINITY, // se ajustará en draw
                    animationSpec = tween(5000, easing = LinearEasing)
                )
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
    ) {
        val fullWidthPx = constraints.maxWidth.toFloat()
        val indicatorWidthPx =
            if (indicatorWidthAnim.value == Float.POSITIVE_INFINITY) fullWidthPx
            else with(density) { indicatorWidthAnim.value.dp.toPx() }

        val offsetPx = indicatorOffset.value * (fullWidthPx - indicatorWidthPx)

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetPx.toInt(), 0) }
                .width(with(density) { indicatorWidthPx.toDp() })
                .fillMaxHeight()
                .background(Color.Blue, RoundedCornerShape(10.dp))
        )
    }
}

@Composable
@Preview
fun IntermittentCircle24(
    modifier: Modifier = Modifier,
    circleSize: Dp = 50.dp,
    durationMillis: Int = 1000
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
            .height(20.dp)
            .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
    ) {
        val maxWidthPx = with(LocalDensity.current) { this@BoxWithConstraints.maxWidth.toPx() }
        with(LocalDensity.current) { circleSize.toPx() }

        val x1 = remember { Animatable(0f) }

        // Animación infinita: ida y vuelta
        LaunchedEffect(Unit) {
            while (true) {
                x1.animateTo(
                    targetValue = maxWidthPx,
                    animationSpec = tween(durationMillis, easing = LinearEasing)
                )
                x1.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis, easing = LinearEasing)
                )
            }
        }

        // Dibujar círculo en posición X1
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(x1.value.toInt(), 0)
                }
                .size(width = circleSize, height = 20.dp)
                .background(Color.Blue, CircleShape)
        )
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun IntermittentCircle23(
    modifier: Modifier = Modifier,
    circleSize: Dp = 50.dp,
    durationMillis: Int = 1000
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
            .height(20.dp)
            .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
    ) {
        val maxWidthPx = with(LocalDensity.current) { this@BoxWithConstraints.maxWidth.toPx() }
        val circleSizePx = with(LocalDensity.current) { circleSize.toPx() }

        // Progreso normalizado [0f..1f]
        val progress = remember { Animatable(0f) }

        // Animación infinita: ida y vuelta
        LaunchedEffect(Unit) {
            while (true) {
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis, easing = LinearEasing)
                )
                progress.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis, easing = LinearEasing)
                )
            }
        }

        // Calcular posición X usando lerp
        val offsetX = MathUtils.lerp(
            start = 0f,
            stop = maxWidthPx - circleSizePx,
            amount = progress.value
        )

        // Dibujar círculo en posición X con graphicsLayer
        Box(
            modifier = Modifier
                .size(width = circleSize, height = 20.dp)
                .graphicsLayer {
                    translationX = offsetX
                }
                .background(Color.Blue, CircleShape)
        )
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun IntermittentBar(
    modifier: Modifier = Modifier,
    circleWidthFraction: Float = 0.2f, // ancho relativo (20% del contenedor)
    circleWidth: Dp = 20.dp,
    durationMillis: Int = AnimatedLoaderBarConstants.INFINITY_DURATION_MILLIS,
    isIntermittent: Boolean = true
) {
    val progress = remember { Animatable(0f) }
    val scale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = AnimatedLoaderBarConstants.INFINITY_DURATION_MILLIS,
                    easing = AnimatedLoaderBarConstants.easing
                ),
                repeatMode = RepeatMode.Reverse
            ),
        )
    }
    LaunchedEffect(isIntermittent) {
        scale.stop()
        scale.animateTo(
            targetValue = if (!isIntermittent) 1f else 0f,
            animationSpec = tween(
                durationMillis = AnimatedLoaderBarConstants.SCALE_DURATION_MILLIS,
                easing = AnimatedLoaderBarConstants.easing
            )
        )
    }

    val circleWidthPx = with(LocalDensity.current) { circleWidth.toPx() }

    Text(
        text = "${progress.value}",
        modifier = modifier
    )

    Text(
        text = "${scale.value}",
        modifier = modifier
    )



    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
    ) {
        val p = progress.value
        val w = size.width

        fun getXs(): Pair<Float, Float> {
            val wX1 = w - circleWidthPx

            val x1 = MathUtils.lerp(0f, wX1, p)
            val x2 = x1 + circleWidthPx

            return x1 to x2
        }


        val (x1, x2) = if (isIntermittent) {
            getXs()
        } else {
            val (x1Init, x2Init) = getXs()

            val x1End = 0f
            val x2End = w

            val s = scale.value

            val x1 = MathUtils.lerp(x1Init, x1End, s)
            val x2 = MathUtils.lerp(x2Init, x2End, s)

            x1 to x2
        }

        val startX = x1
        val endX = x2

        drawRoundRect(
            color = Color.Blue,
            topLeft = Offset(startX, 0f),
            size = Size(endX - startX, size.height),
            cornerRadius = CornerRadius(x = size.height / 2, y = size.height / 2)
        )
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun IntermittentBar565(
    modifier: Modifier = Modifier,
    circleWidthFraction: Float = 0.2f, // ancho relativo (20% del contenedor)
    circleWidth: Dp = 20.dp,
    durationMillis: Int = AnimatedLoaderBarConstants.INFINITY_DURATION_MILLIS,
    isIntermittent: Boolean = true
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(isIntermittent) {
        progress.stop() // detener animaciones previas
        if (isIntermittent) {
            // Rebotando ida y vuelta (0f..1f)
            while (true) {
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis, easing = AnimatedLoaderBarConstants.easing)
                )
                progress.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis, easing = AnimatedLoaderBarConstants.easing)
                )
            }
        } else {
            // Expansión en 5000ms desde cualquier punto
            launch {
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis, easing = AnimatedLoaderBarConstants.easing)
                )
            }
        }
    }

    val circleWidthPx = with(LocalDensity.current) { circleWidth.toPx() }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
    ) {
        val p = progress.value
        val w = size.width


        val (x1, x2) = if (isIntermittent) {
            val wX1 = w - circleWidthPx

            val x1 = MathUtils.lerp(0f, wX1, p)
            val x2 = x1 + circleWidthPx

            x1 to x2
        } else {
            val wX1 = w - circleWidthPx

            val x1Init = MathUtils.lerp(0f, wX1, p)
            val x2Init = x1Init + circleWidthPx

            val x1End = 0f
            val x2End = w

            val x1 = MathUtils.lerp(x1Init, x1End, p)
            val x2 = MathUtils.lerp(x2Init, x2End, p)

            x1 to x2
        }

        val startX = x1
        val endX = x2

        drawRoundRect(
            color = Color.Blue,
            topLeft = Offset(startX, 0f),
            size = Size(endX - startX, size.height),
            cornerRadius = CornerRadius(x = size.height / 2, y = size.height / 2)
        )
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun IntermittentBar3(
    modifier: Modifier = Modifier,
    circleWidthFraction: Float = 0.2f, // ancho relativo (20% del contenedor)
    durationMillis: Int = 1000,
    isIntermittent: Boolean = true
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(isIntermittent) {
        progress.stop() // detener animaciones previas
        if (isIntermittent) {
            // Rebotando ida y vuelta (0f..1f)
            while (true) {
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis, easing = LinearEasing)
                )
                progress.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis, easing = LinearEasing)
                )
            }
        } else {
            // Expansión en 5000ms desde cualquier punto
            launch {
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(5000, easing = LinearEasing)
                )
            }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
    ) {
        val p = progress.value

        val (x1, x2) = if (isIntermittent) {
            // 🔹 En intermitente: mapea progreso [0..1] → X1 [0..1-circleWidth]
            val start = MathUtils.lerp(0f, 1f - circleWidthFraction, p)
            val end = start + circleWidthFraction
            start to end
        } else {
            // 🔹 En expandido: mapea progreso [0..1] → X1 0→0, X2 0→1
            val start = 0f
            val end = p // crece hasta llenar
            start to end
        }

        val startX = size.width * x1
        val endX = size.width * x2

        drawRoundRect(
            color = Color.Blue,
            topLeft = Offset(startX, 0f),
            size = Size(endX - startX, size.height),
            cornerRadius = CornerRadius(x = size.height / 2, y = size.height / 2)
        )
    }
}


@Preview
@Composable
fun PreviewIntermittentCircle() {
    var isIntermittent by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IntermittentBar(isIntermittent = isIntermittent)

        Spacer(Modifier.height(16.dp))

        Button(onClick = { isIntermittent = !isIntermittent }) {
            Text(if (isIntermittent) "Expand" else "Intermittent")
        }
    }

}
