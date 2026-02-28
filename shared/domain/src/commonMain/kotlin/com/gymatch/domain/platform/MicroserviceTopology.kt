package com.gymatch.domain.platform

enum class ServiceName {
    AUTH,
    MATCHING,
    TRAINING,
    RANKING,
    MARKETPLACE,
    EVENTS,
    RECOMMENDATION,
    PAYMENT,
}

data class ServiceEndpoint(
    val serviceName: ServiceName,
    val baseUrl: String,
    val isCriticalPath: Boolean,
)

interface ServiceDiscovery {
    suspend fun resolve(serviceName: ServiceName): ServiceEndpoint
}
