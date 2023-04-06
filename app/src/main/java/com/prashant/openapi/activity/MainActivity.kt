package com.prashant.openapi.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.rememberNavController
import com.prashant.openapi.navigation.SetNavGraph
import com.prashant.openapi.ui.theme.OpenAPITheme
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class MainActivity : ComponentActivity(), ShowLoader {

    private var showLoader by mutableStateOf(false)

    companion object {
        var weakReference = WeakReference<MainActivity>(null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weakReference = WeakReference(this)
        setContent {
            val navHostController = rememberNavController()
            OpenAPITheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    /**
                     *The following code creates a Box composable element that fills the maximum size of the container.
                     *The content inside is centered vertically and horizontally.
                     *If the showLoader variable is true, it shows a CircularProgressAnimated composable element on any api request.
                     * Otherwise, it shows a SetNavGraph composable element that takes in a navHostController.
                     */
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (showLoader) {
                            CircularProgressAnimated()
                        }
                        SetNavGraph(navHostController)
                    }

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        weakReference = WeakReference(null)
    }

    override fun showLoader(boolean: Boolean) {
        showLoader = boolean
    }

    /**

     *This is a Composable function to show a circular progress bar with a loading text and some information text.

     *It uses the rememberInfiniteTransition to animate the progress bar infinitely.

     *@property progressValue the progress value for the progress bar, set to 1f.

     *@property infiniteTransition an infinite transition object to animate the progress bar.

     *@property progressAnimationValue an animated value for the progress bar, it is updated by the infinite transition animation.

     *@return the UI element composed of the circular progress bar, loading text and information text in a Card inside a Dialog.
     */
    @Composable
    private fun CircularProgressAnimated() {
        val progressValue = 1f
        val infiniteTransition = rememberInfiniteTransition()

        val progressAnimationValue by infiniteTransition.animateFloat(
            initialValue = 0.0f,
            targetValue = progressValue,
            animationSpec = infiniteRepeatable(animation = tween(900))
        )

        Dialog(onDismissRequest = { }) {
            Card(
                shape = RoundedCornerShape(10.dp), modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    CircularProgressIndicator(progress = progressAnimationValue)
                    Spacer(modifier = Modifier.width(15.dp))
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(text = "Loading", style = MaterialTheme.typography.h6)
                        Text(text = "Please wait.....", style = MaterialTheme.typography.caption)
                    }
                }
            }

        }
    }
}

