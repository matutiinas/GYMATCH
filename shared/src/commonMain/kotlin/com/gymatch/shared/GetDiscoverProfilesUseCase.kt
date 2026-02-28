package com.gymatch.shared

class GetDiscoverProfilesUseCase(private val repo: FakeProfileRepository) {
  operator fun invoke(): List<Profile> = repo.getDiscoverProfiles()
}
