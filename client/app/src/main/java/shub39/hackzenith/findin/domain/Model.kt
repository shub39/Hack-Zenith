package shub39.hackzenith.findin.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post (
    val id: String? = null,
    val types: String,
    val title: String,
    val description: String,
    val images: List<String>,
    val user: User,
    val location: Location,
    val tags: List<String>,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("is_solved")
    val isSolved: Boolean
)

@Serializable
data class Location (
    val place: String,
    val area: String
)

@Serializable
data class User (
    val uid: String,
    val email: String,
    val name: String,
    val avatar: String
)