package com.gymatch.data.seed

import kotlinx.datetime.Clock
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

class ProfileSeedGenerator(
    private val random: Random = Random(42),
) {
    private val sportsPool = listOf("gym", "running", "crossfit", "swimming", "cycling")
    private val levels = listOf("principiante", "intermedio", "avanzado")
    private val objectives = listOf("ganar músculo", "perder grasa", "mejorar marca")
    private val names = listOf(
        "Alex", "Sofía", "Mateo", "Valeria", "Lucas", "Emma", "Hugo", "Martina", "Leo", "Paula",
        "Daniel", "Noa", "Nicolás", "Elena", "Adrián", "Marta", "Iker", "Clara", "Bruno", "Julia"
    )

    fun generate(count: Int = 50, centerLat: Double = 40.4168, centerLng: Double = -3.7038): List<SeedProfileRecord> {
        return (1..count).map { index ->
            val primarySport = sportsPool.random(random)
            val otherSports = sportsPool.filterNot { it == primarySport }.shuffled(random).take(random.nextInt(1, 3))
            val sports = (listOf(primarySport) + otherSports).distinct()
            SeedProfileRecord(
                userId = "seed-user-$index",
                name = "${names.random(random)} $index",
                age = random.nextInt(20, 43),
                photos = buildPhotos(index),
                sports = sports,
                level = levels.random(random),
                objective = objectives.random(random),
                personalBests = buildPersonalBests(sports, random),
                latitude = (centerLat + random.nextDouble(-0.03, 0.03)).round(6),
                longitude = (centerLng + random.nextDouble(-0.03, 0.03)).round(6),
                isOnline = random.nextBoolean(),
                lastActiveEpochSec = Clock.System.now().epochSeconds - random.nextLong(60, 60 * 60 * 24 * 5),
            )
        }
    }

    private fun buildPhotos(index: Int): List<String> {
        val count = random.nextInt(2, 5)
        return (1..count).map { slot -> "https://picsum.photos/seed/gymatch-$index-$slot/720/960" }
    }

    private fun buildPersonalBests(sports: List<String>, random: Random): Map<String, String> {
        val pb = mutableMapOf<String, String>()
        if ("gym" in sports) pb["bench_press_kg"] = random.nextInt(45, 121).toString()
        if ("running" in sports) pb["5k_sec"] = random.nextInt(19 * 60, 33 * 60).toString()
        if ("swimming" in sports) pb["swim_1k_sec"] = random.nextInt(900, 1700).toString()
        if ("cycling" in sports) pb["20k_sec"] = random.nextInt(1800, 3600).toString()
        if ("crossfit" in sports) pb["fran_sec"] = random.nextInt(240, 720).toString()
        return pb
    }

    private fun Double.round(decimals: Int): Double {
        val factor = 10.0.pow(decimals)
        return (this * factor).roundToInt() / factor
    }
}
