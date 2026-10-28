package com.peggiy.pather.lather.presentation.app

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.peggiy.pather.lather.core.di.AppViewModelFactory
import com.peggiy.pather.lather.core.navigation.Screen
import com.peggiy.pather.lather.presentation.game.GameRoute
import com.peggiy.pather.lather.presentation.gameover.GameOverRoute
import com.peggiy.pather.lather.presentation.menu.MenuRoute
import com.peggiy.pather.lather.presentation.splash.SplashRoute

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val roundToken by appViewModel.roundToken.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BackHandler(enabled = true) {
        if (!appViewModel.back()) {
            (context as? Activity)?.finish()
        }
    }

    AnimatedContent(
        targetState = screen,
        transitionSpec = { fadeIn(tween(320)) togetherWith fadeOut(tween(260)) },
        label = "screen_host",
        modifier = Modifier.fillMaxSize()
    ) { current ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (current) {
                Screen.Splash -> SplashRoute(onFinished = { appViewModel.toMenu() })
                Screen.Menu -> MenuRoute(onPlay = { appViewModel.toGame() })
                Screen.Game -> GameRoute(
                    roundToken = roundToken,
                    onLeave = { appViewModel.toMenu() },
                    onRoundFinished = { score, slot, newBest ->
                        appViewModel.toGameOver(score, slot, newBest)
                    }
                )
                is Screen.GameOver -> GameOverRoute(
                    result = current,
                    onPlayAgain = { appViewModel.toGame() },
                    onMenu = { appViewModel.toMenu() }
                )
            }
        }
    }
}
