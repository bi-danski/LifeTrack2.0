package org.lifetrack.ltapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.SupervisorJob
import org.lifetrack.ltapp.ui.navigation.AppNavigation
import org.lifetrack.ltapp.ui.theme.LTAppTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        enableEdgeToEdge()
        setContent {
            LTAppTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        rememberNavController()
//                        scope = scope
                    )
                }
            }
        }
    }

}