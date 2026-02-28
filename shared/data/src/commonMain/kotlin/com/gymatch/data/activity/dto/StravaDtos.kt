package com.gymatch.data.activity.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StravaTokenResponseDto(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("expires_at") val expiresAt: Long,
    val athlete: StravaAthleteDto,
)

@Serializable
data class StravaAthleteDto(
    val id: Long,
)

@Serializable
data class StravaActivityDto(
    val id: Long,
    val type: String,
    @SerialName("start_date") val startDateIso: String,
    @SerialName("elapsed_time") val elapsedTimeSec: Int,
    val distance: Double? = null,
    @SerialName("kilojoules") val kiloJoules: Double? = null,
    val city: String? = null,
)
