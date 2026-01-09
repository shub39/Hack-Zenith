package shub39.hackzenith.findin.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    val id: String? = null,          // Mongo adds this
    val types: String,                // "lost" or "found"
    val title: String,
    val user: UserDto,
    val description: String,
    val images: List<String> = emptyList(),
    val location: LocationDto,
    val tags: List<String> = emptyList(),
    @SerialName("created_at")
    val createdAt: String? = null,   // ISO date string
    @SerialName("is_solved")
    val isSolved: Boolean = false
)


@Serializable
data class UserDto(
    val uid: String,
    val email: String,
    val name: String,
    val photoURL: String? = null,
    val phone: String? = null,
    val address: String? = null
)

@Serializable
data class LocationDto(
    val place: String,
    val area: String
)
