package com.syouth.dollarapp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.syouth.dollarapp.ui.main_screen.MainScreen
import com.syouth.dollarapp.ui.main_screen.di.MainScreenApiCreator
import com.syouth.dollarapp.ui.theme.DollarAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DollarAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        MainScreen.Widget(
                            api = MainScreenApiCreator.create(
                                dependencies = (application as DollarApp).applicationComponent,
                                viewModelStoreOwner = this@MainActivity,
                            )
                        )
                    }
                }
            }
        }
    }
}