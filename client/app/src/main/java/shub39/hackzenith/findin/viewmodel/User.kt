package shub39.hackzenith.findin.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import shub39.hackzenith.findin.domain.UserDto

class User {
    val firebaseUser = MutableStateFlow<UserDto?>(null)
}