package com.gymatch.domain.activity

interface OAuthLinker {
    suspend fun buildAuthorizationUrl(userId: String, provider: ExternalProvider): String
    suspend fun exchangeCodeForConnection(
        userId: String,
        provider: ExternalProvider,
        authorizationCode: String,
    ): ExternalConnection
}

interface WorkoutSyncRepository {
    suspend fun syncVerifiedWorkouts(userId: String, provider: ExternalProvider): List<VerifiedWorkout>
    suspend fun saveWorkouts(workouts: List<VerifiedWorkout>)
}

interface WorkoutVerificationService {
    suspend fun verify(workout: VerifiedWorkout): VerificationStatus
}
