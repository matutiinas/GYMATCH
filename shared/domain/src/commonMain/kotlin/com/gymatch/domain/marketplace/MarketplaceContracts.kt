package com.gymatch.domain.marketplace

interface MarketplaceRepository {
    suspend fun getVerifiedCoaches(regionCode: String, limit: Int): List<CoachProfile>
    suspend fun getCoachOffers(coachId: String): List<TrainingPlanOffer>
    suspend fun createSessionOrder(userId: String, coachId: String, offerId: String): SessionOrder
    suspend fun markSessionPaid(orderId: String, paymentRef: String): SessionOrder
}

interface PaymentGateway {
    suspend fun createPaymentIntent(orderId: String, amount: Long, currency: String): PaymentIntent
    suspend fun confirmPayment(paymentIntentId: String): PaymentResult
}

data class PaymentIntent(
    val paymentIntentId: String,
    val clientSecret: String,
)

data class PaymentResult(
    val paymentIntentId: String,
    val success: Boolean,
)
