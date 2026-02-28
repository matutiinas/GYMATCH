package com.gymatch.data.ads.datasource

data class SponsoredProfilePayload(
    val adId: String,
    val displayName: String,
    val sportCodes: Set<String>,
    val campaignLabel: String,
)

interface AdsProvider {
    suspend fun fetchSponsoredProfiles(userId: String, limit: Int): List<SponsoredProfilePayload>
    suspend fun trackImpression(adId: String, viewerUserId: String)
    suspend fun trackClick(adId: String, viewerUserId: String)
}
