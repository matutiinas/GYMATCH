package com.gymatch.domain.usecase

import com.gymatch.domain.activity.ExternalProvider
import com.gymatch.domain.activity.WorkoutSyncRepository
import com.gymatch.domain.activity.WorkoutVerificationService

class SyncStravaWorkoutsUseCase(
    private val workoutSyncRepository: WorkoutSyncRepository,
    private val verificationService: WorkoutVerificationService,
) {
    suspend operator fun invoke(userId: String): Int {
        val pulled = workoutSyncRepository.syncVerifiedWorkouts(userId, ExternalProvider.STRAVA)
        val verified = pulled.map { it.copy(verificationStatus = verificationService.verify(it)) }
            .filter { it.verificationStatus == com.gymatch.domain.activity.VerificationStatus.VERIFIED }

        workoutSyncRepository.saveWorkouts(verified)
        return verified.size
    }
}
