package com.gymatch.data.seed

class SeedService(
    private val profileDataSource: ProfileSeedDataSource,
    private val generator: ProfileSeedGenerator = ProfileSeedGenerator(),
) {
    suspend fun seedProfilesIfEmpty(
        isDebug: Boolean,
        targetCount: Int = 50,
    ): SeedResult {
        if (!isDebug) return SeedResult.Skipped("Not DEBUG environment")

        val existing = profileDataSource.countProfiles()
        if (existing > 0) return SeedResult.Skipped("Profiles already exist: $existing")

        val profiles = generator.generate(targetCount)
        profileDataSource.saveProfiles(profiles)
        return SeedResult.Created(profiles.size)
    }
}

sealed class SeedResult {
    data class Created(val count: Int) : SeedResult()
    data class Skipped(val reason: String) : SeedResult()
}
