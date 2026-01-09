package shub39.hackzenith.findin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import shub39.hackzenith.findin.presentation.auth.SignInScreen
import shub39.hackzenith.findin.presentation.main_page.MainPage
import shub39.hackzenith.findin.ui.theme.FindInTheme
import shub39.hackzenith.findin.viewmodel.MainPageViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var showMainPage by remember { mutableStateOf(false) }

            FindInTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!showMainPage) {
                        SignInScreen(
                            onNavigateToMainPage = { showMainPage = true }
                        )
                    } else {
                        val viewModel = koinViewModel<MainPageViewModel>()
                        val state = viewModel.state.collectAsStateWithLifecycle()

                        MainPage(
                            state = state.value,
                            onAction = viewModel::onAction
                        )
                    }
                }
            }
        }
    }
}