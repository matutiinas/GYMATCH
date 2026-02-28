package com.gymatch.data.activity.mapper

import com.gymatch.data.activity.dto.StravaActivityDto
import com.gymatch.domain.activity.ExternalProvider
import com.gymatch.domain.activity.VerificationStatus
import com.gymatch.domain.activity.VerifiedWorkout
import com.gymatch.domain.model.SportType

class StravaWorkoutMapper {
    fun toDomain(userId: String, dto: StravaActivityDto): VerifiedWorkout {
        return VerifiedWorkout(
            workoutId = "strava-${dto.id}",
            userId = userId,
            provider = ExternalProvider.STRAVA,
            sportType = mapSportType(dto.type),
            cityCode = dto.city?.uppercase() ?: "UNKNOWN",
            categoryCode = mapCategory(dto.type),
            startedAtEpochSec = parseEpoch(dto.startDateIso),
            durationSec = dto.elapsedTimeSec,
            distanceMeters = dto.distance,
            calories = dto.kiloJoules?.times(0.239006)?.toInt(),
            verificationStatus = VerificationStatus.PENDING,
        )
    }

    private fun mapSportType(type: String): SportType = when (type.uppercase()) {
        "RUN", "VIRTUALRUN" -> SportType.RUNNING
        "WEIGHTTRAINING", "WORKOUT" -> SportType.GYM
        else -> SportType.OTHER
    }

    private fun mapCategory(type: String): String = when (type.uppercase()) {
        "RUN", "VIRTUALRUN" -> "ENDURANCE"
        "WEIGHTTRAINING", "WORKOUT" -> "STRENGTH"
        else -> "GENERAL"
    }

    private fun parseEpoch(isoDate: String): Long =
        kotlin.runCatching { kotlinx.datetime.Instant.parse(isoDate).epochSeconds }.getOrDefault(0L)
}
