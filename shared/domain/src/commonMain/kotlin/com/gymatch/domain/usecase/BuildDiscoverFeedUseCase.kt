package com.gymatch.domain.usecase

import com.gymatch.domain.ads.SponsoredProfile
import com.gymatch.domain.model.DiscoverCandidate
import com.gymatch.domain.repository.DiscoverRepository

sealed class DiscoverFeedItem {
    data class Organic(val candidate: DiscoverCandidate) : DiscoverFeedItem()
    data class Sponsored(val sponsored: SponsoredProfile) : DiscoverFeedItem()
}

class BuildDiscoverFeedUseCase(
    private val discoverRepository: DiscoverRepository,
    private val adInsertionFrequency: Int = 5,
) {
    suspend operator fun invoke(userId: String, limit: Int): List<DiscoverFeedItem> {
        val organic = discoverRepository.getDiscoverCandidates(userId, limit)
        val sponsored = discoverRepository.getSponsoredCandidates(userId, limit / adInsertionFrequency + 1)

        if (sponsored.isEmpty()) return organic.map { DiscoverFeedItem.Organic(it) }

        val feed = mutableListOf<DiscoverFeedItem>()
        var sponsoredIndex = 0
        organic.forEachIndexed { index, candidate ->
            feed += DiscoverFeedItem.Organic(candidate)
            if ((index + 1) % adInsertionFrequency == 0 && sponsoredIndex < sponsored.size) {
                feed += DiscoverFeedItem.Sponsored(sponsored[sponsoredIndex++])
            }
        }
        return feed
    }
}
