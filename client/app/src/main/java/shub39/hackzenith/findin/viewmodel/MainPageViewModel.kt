package shub39.hackzenith.findin.viewmodel

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.http.ContentDisposition
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.http.content.PartData
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import shub39.hackzenith.findin.domain.createServerApi
import shub39.hackzenith.findin.presentation.auth.MainPageAction
import shub39.hackzenith.findin.presentation.main_page.MainPageState

class MainPageViewModel(
    private val user: User,
    private val contentResolver: ContentResolver
) : ViewModel() {

    val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    private val _state = MutableStateFlow(MainPageState())
    val state = _state.asStateFlow()
        .onStart {
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        posts = json.decodeFromString(api.getAllPosts())
                    )
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            MainPageState()
        )

    private val ktorfit = Ktorfit.Builder()
        .baseUrl("https://hack-zenith.onrender.com/")
        .build()
    private val api = ktorfit.createServerApi()

    fun onAction(action: MainPageAction) {
        when (action) {
            is MainPageAction.PostContent -> viewModelScope.launch {
                Log.d("MainPageViewModel", "Sending Post")

                val firebaseUser = user.firebaseUser.value

                if (firebaseUser == null) {
                    Log.d("MainPageViewModel", "User not logged in")
                    return@launch
                }

                val images = withContext(Dispatchers.IO) {
                    action.images.mapNotNull { uriString ->
                        val uri = Uri.parse(uriString)
                        contentResolver.openInputStream(uri)?.let { inputStream ->
                            val bytes = inputStream.readBytes()
                            PartData.FileItem(
                                provider = { ByteReadChannel(bytes) },
                                dispose = {},
                                partHeaders = Headers.build {
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        ContentDisposition.File.withParameter(
                                            ContentDisposition.Parameters.FileName,
                                            "image.jpg"
                                        )
                                    )
                                    append(HttpHeaders.ContentType, "image/jpeg")
                                }
                            )
                        }
                    }
                }

                val result = api.createPost(
                    types = action.types,
                    title = action.title,
                    description = action.description,
                    tags = action.tags.joinToString(" "),
                    place = action.location.place,
                    area = action.location.area,
                    images = images,
                    userUid = firebaseUser.uid,
                    userEmail = firebaseUser.email,
                    userName = firebaseUser.name,
                    photoURL = firebaseUser.photoURL ?: "",
                )

                Log.d("MainPageViewModel", "Sent Post $result")

                delay(1000)

                _state.update {
                    it.copy(
                        posts = json.decodeFromString(api.getAllPosts())
                    )
                }
            }
        }
    }
}