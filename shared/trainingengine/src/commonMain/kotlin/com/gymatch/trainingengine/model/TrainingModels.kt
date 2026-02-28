package com.gymatch.trainingengine.model

import com.gymatch.domain.model.SportType

enum class TrainingPlace { GYM, HOME, OUTDOOR, TRACK }

enum class FatigueLevel { LOW, MEDIUM, HIGH }

data class AthleteTrainingSnapshot(
    val sportTypes: Set<SportType>,
    val estimatedBenchKg: Double? = null,
    val estimatedSquatKg: Double? = null,
    val fiveKmTimeSec: Int? = null,
)

data class TrainingContext(
    val availableMinutes: Int,
    val place: TrainingPlace,
    val availableEquipment: Set<String>,
    val fatigueLevel: FatigueLevel,
    val llmHints: Map<String, String> = emptyMap(),
)

data class TrainingPlan(
    val title: String,
    val blocks: List<String>,
    val rationale: String,
)
