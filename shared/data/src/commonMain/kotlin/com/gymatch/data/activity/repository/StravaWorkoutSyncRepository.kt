package com.gymatch.data.activity.repository

import com.gymatch.data.activity.mapper.StravaWorkoutMapper
import com.gymatch.data.activity.remote.StravaApi
import com.gymatch.domain.activity.ExternalProvider
import com.gymatch.domain.activity.VerifiedWorkout
import com.gymatch.domain.activity.WorkoutSyncRepository

interface WorkoutLocalStore {
    suspend fun getAccessToken(userId: String, provider: ExternalProvider): String
    suspend fun upsertWorkouts(workouts: List<VerifiedWorkout>)
}

class StravaWorkoutSyncRepository(
    private val stravaApi: StravaApi,
    private val localStore: WorkoutLocalStore,
    private val mapper: StravaWorkoutMapper,
) : WorkoutSyncRepository {

    override suspend fun syncVerifiedWorkouts(userId: String, provider: ExternalProvider): List<VerifiedWorkout> {
        require(provider == ExternalProvider.STRAVA) { "StravaWorkoutSyncRepository only supports STRAVA" }
        val token = localStore.getAccessToken(userId, provider)
        return stravaApi.getActivities(token).map { mapper.toDomain(userId, it) }
    }

    override suspend fun saveWorkouts(workouts: List<VerifiedWorkout>) {
        localStore.upsertWorkouts(workouts)
    }
}
