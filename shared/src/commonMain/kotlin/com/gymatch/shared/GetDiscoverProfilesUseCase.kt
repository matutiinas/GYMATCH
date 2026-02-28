package com.gymatch.shared

class GetDiscoverProfilesUseCase(
    private val repo: ProfileRepository,
) {
    suspend operator fun invoke(): List<Profile> = repo.getDiscoverProfiles()
}
