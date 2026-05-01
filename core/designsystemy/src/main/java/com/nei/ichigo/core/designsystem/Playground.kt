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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.PlaceholderSize
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                            placeholderSize = PlaceholderSize.AnimatedSize
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
                        placeholderSize = PlaceholderSize.AnimatedSize
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
    val transformableState = rememberTransformableState { _, zoomChange, offsetChange, _ ->
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

    val transformableState = rememberTransformableState { _, zoomChange, offsetChange, _ ->
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
        rememberTransformableState { _, zoomChange, offsetChange, _ ->
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
