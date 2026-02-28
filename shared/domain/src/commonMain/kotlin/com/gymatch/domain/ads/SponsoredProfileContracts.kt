package com.gymatch.domain.ads

import com.gymatch.domain.model.DiscoverCandidate

data class SponsoredProfile(
    val adId: String,
    val candidate: DiscoverCandidate,
    val campaignLabel: String,
)

interface SponsoredProfileRepository {
    suspend fun getSponsoredProfiles(userId: String, limit: Int): List<SponsoredProfile>
    suspend fun registerImpression(adId: String, viewerUserId: String)
    suspend fun registerClick(adId: String, viewerUserId: String)
}
