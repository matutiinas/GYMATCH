package com.gymatch.data.ads

import com.gymatch.data.ads.datasource.AdsProvider
import com.gymatch.domain.ads.SponsoredProfile
import com.gymatch.domain.ads.SponsoredProfileRepository
import com.gymatch.domain.model.DiscoverCandidate
import com.gymatch.domain.model.PresenceTag
import com.gymatch.domain.model.SportType

class SponsoredProfileRepositoryImpl(
    private val provider: AdsProvider,
) : SponsoredProfileRepository {

    override suspend fun getSponsoredProfiles(userId: String, limit: Int): List<SponsoredProfile> {
        return provider.fetchSponsoredProfiles(userId, limit).map { payload ->
            SponsoredProfile(
                adId = payload.adId,
                candidate = DiscoverCandidate(
                    userId = "sponsored-${payload.adId}",
                    displayName = payload.displayName,
                    sports = payload.sportCodes.map { it.toSportType() }.toSet(),
                    tags = setOf(PresenceTag.NEW),
                ),
                campaignLabel = payload.campaignLabel,
            )
        }
    }

    override suspend fun registerImpression(adId: String, viewerUserId: String) {
        provider.trackImpression(adId, viewerUserId)
    }

    override suspend fun registerClick(adId: String, viewerUserId: String) {
        provider.trackClick(adId, viewerUserId)
    }
}

private fun String.toSportType(): SportType = when (uppercase()) {
    "GYM" -> SportType.GYM
    "RUNNING" -> SportType.RUNNING
    "FUNCTIONAL" -> SportType.FUNCTIONAL
    else -> SportType.OTHER
}
