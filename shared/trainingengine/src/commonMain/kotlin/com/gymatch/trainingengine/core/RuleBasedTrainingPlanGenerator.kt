package com.gymatch.trainingengine.core

import com.gymatch.domain.model.SportType
import com.gymatch.trainingengine.api.TrainingPlanGenerator
import com.gymatch.trainingengine.model.AthleteTrainingSnapshot
import com.gymatch.trainingengine.model.FatigueLevel
import com.gymatch.trainingengine.model.TrainingContext
import com.gymatch.trainingengine.model.TrainingPlace
import com.gymatch.trainingengine.model.TrainingPlan

class RuleBasedTrainingPlanGenerator : TrainingPlanGenerator {
    override suspend fun generate(
        athleteA: AthleteTrainingSnapshot,
        athleteB: AthleteTrainingSnapshot,
        context: TrainingContext,
    ): TrainingPlan {
        val intensityFactor = when (context.fatigueLevel) {
            FatigueLevel.LOW -> 1.0
            FatigueLevel.MEDIUM -> 0.9
            FatigueLevel.HIGH -> 0.75
        }
        val minutes = context.availableMinutes.coerceIn(20, 120)

        val bothGym = athleteA.sportTypes.contains(SportType.GYM) && athleteB.sportTypes.contains(SportType.GYM)
        if (bothGym && context.place == TrainingPlace.GYM) {
            val bench = (listOfNotNull(athleteA.estimatedBenchKg, athleteB.estimatedBenchKg).averageOrNull() ?: 40.0) * intensityFactor
            val squat = (listOfNotNull(athleteA.estimatedSquatKg, athleteB.estimatedSquatKg).averageOrNull() ?: 60.0) * intensityFactor
            return TrainingPlan(
                title = "Gym Duo Strength",
                blocks = listOf(
                    "Warm-up ${minutes / 6} min",
                    "Bench 5x5 @ ${bench.toInt()}kg",
                    "Squat 5x5 @ ${squat.toInt()}kg",
                    if ("dumbbells" in context.availableEquipment) "Dumbbell finisher 3 rounds" else "Bodyweight finisher 3 rounds"
                ),
                rationale = "Plan dinámico por fatiga, tiempo y material disponible; extensible a sugerencias LLM."
            )
        }

        val bothRunning = athleteA.sportTypes.contains(SportType.RUNNING) && athleteB.sportTypes.contains(SportType.RUNNING)
        if (bothRunning && context.place in setOf(TrainingPlace.OUTDOOR, TrainingPlace.TRACK)) {
            val avg5k = listOfNotNull(athleteA.fiveKmTimeSec, athleteB.fiveKmTimeSec).averageOrNull() ?: 1800.0
            val intervalPaceSec = (avg5k / 5.0 * (0.92 / intensityFactor)).toInt()
            return TrainingPlan(
                title = "Running Duo Intervals",
                blocks = listOf(
                    "Warm-up ${minutes / 5} min",
                    "${(minutes / 8).coerceAtLeast(4)} x 400m @ ${intervalPaceSec}s/km",
                    "Recovery jog 200m",
                    "Cool down ${minutes / 6} min"
                ),
                rationale = "Intervalos ajustados por tiempo disponible y fatiga acumulada."
            )
        }

        return TrainingPlan(
            title = "Mixed Functional Pair",
            blocks = listOf(
                "EMOM ${(minutes * 0.4).toInt()}': squats, push-ups, plank",
                if ("kettlebell" in context.availableEquipment) "Kettlebell complex ${(minutes * 0.25).toInt()}'" else "Bodyweight circuit ${(minutes * 0.25).toInt()}'",
                "Mobility + breathing ${(minutes * 0.2).toInt()}'"
            ),
            rationale = "Deportes/lugar mixto: adaptación funcional contextual preparada para motor IA externo."
        )
    }
}

private fun List<Double>.averageOrNull(): Double? = if (isEmpty()) null else average()
