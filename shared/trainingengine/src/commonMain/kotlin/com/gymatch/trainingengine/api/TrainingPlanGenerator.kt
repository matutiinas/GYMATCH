package com.gymatch.trainingengine.api

import com.gymatch.trainingengine.model.AthleteTrainingSnapshot
import com.gymatch.trainingengine.model.TrainingContext
import com.gymatch.trainingengine.model.TrainingPlan

interface TrainingPlanGenerator {
    suspend fun generate(
        athleteA: AthleteTrainingSnapshot,
        athleteB: AthleteTrainingSnapshot,
        context: TrainingContext,
    ): TrainingPlan
}
