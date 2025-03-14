package model

import kotlinx.serialization.Serializable

@Serializable
data class Track(
    val path: String,
    val title: String,
    val artist: String,
)
