package shub39.hackzenith.findin.presentation.main_page

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import shub39.hackzenith.findin.domain.Post

@Stable
@Immutable
data class MainPageState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val desc: String = ""
)
