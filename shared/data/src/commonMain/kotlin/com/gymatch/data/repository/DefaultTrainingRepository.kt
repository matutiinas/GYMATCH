package com.gymatch.data.repository

import com.gymatch.domain.model.AthleteProfile
import com.gymatch.domain.model.SportType
import com.gymatch.domain.repository.TrainingRepository
import com.gymatch.trainingengine.api.TrainingPlanGenerator
import com.gymatch.trainingengine.model.AthleteTrainingSnapshot
import com.gymatch.trainingengine.model.TrainingContext
import com.gymatch.trainingengine.model.TrainingPlan

class DefaultTrainingRepository(
    private val generator: TrainingPlanGenerator,
) : TrainingRepository {

    override suspend fun generatePlan(userA: AthleteProfile, userB: AthleteProfile, context: TrainingContext): TrainingPlan {
        val snapshotA = AthleteTrainingSnapshot(
            sportTypes = userA.sports,
            estimatedBenchKg = userA.personalBests[SportType.GYM]?.toDoubleOrNull(),
            estimatedSquatKg = userA.personalBests[SportType.FUNCTIONAL]?.toDoubleOrNull(),
            fiveKmTimeSec = userA.personalBests[SportType.RUNNING]?.toIntOrNull(),
        )
        val snapshotB = AthleteTrainingSnapshot(
            sportTypes = userB.sports,
            estimatedBenchKg = userB.personalBests[SportType.GYM]?.toDoubleOrNull(),
            estimatedSquatKg = userB.personalBests[SportType.FUNCTIONAL]?.toDoubleOrNull(),
            fiveKmTimeSec = userB.personalBests[SportType.RUNNING]?.toIntOrNull(),
        )
        return generator.generate(snapshotA, snapshotB, context)
    }
}
