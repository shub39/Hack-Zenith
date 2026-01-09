package shub39.hackzenith.findin.presentation.auth

import shub39.hackzenith.findin.domain.LocationDto

sealed interface MainPageAction {
    data class PostContent(
        val types: String,                // "lost" or "found"
        val title: String,
        val description: String,
        val images: List<String> = emptyList(),
        val location: LocationDto,
        val tags: List<String> = emptyList(),
    ): MainPageAction
}