package com.gymatch.data.activity.remote

import com.gymatch.data.activity.dto.StravaActivityDto
import com.gymatch.data.activity.dto.StravaTokenResponseDto

interface StravaApi {
    suspend fun getAuthorizationUrl(state: String): String
    suspend fun exchangeAuthorizationCode(code: String): StravaTokenResponseDto
    suspend fun getActivities(accessToken: String): List<StravaActivityDto>
}
