package com.gymatch.domain.activity

class VerifiedWorkoutRules : WorkoutVerificationService {
    override suspend fun verify(workout: VerifiedWorkout): VerificationStatus {
        val hasDuration = workout.durationSec >= 10 * 60
        val hasValidStartTime = workout.startedAtEpochSec > 0
        val hasProviderIdentity = workout.workoutId.startsWith(workout.provider.name.lowercase())

        return if (hasDuration && hasValidStartTime && hasProviderIdentity) {
            VerificationStatus.VERIFIED
        } else {
            VerificationStatus.REJECTED
        }
    }
}
