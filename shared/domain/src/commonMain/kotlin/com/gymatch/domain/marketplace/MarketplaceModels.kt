package com.gymatch.domain.marketplace

import kotlinx.datetime.Instant

enum class CoachVerificationStatus { PENDING, VERIFIED, SUSPENDED }
enum class SessionPaymentStatus { PENDING, PAID, REFUNDED }

data class CoachProfile(
    val coachId: String,
    val displayName: String,
    val verificationStatus: CoachVerificationStatus,
    val specialties: Set<String>,
    val rating: Double,
)

data class TrainingPlanOffer(
    val offerId: String,
    val coachId: String,
    val title: String,
    val description: String,
    val priceAmount: Long,
    val currency: String,
)

data class SessionOrder(
    val orderId: String,
    val userId: String,
    val coachId: String,
    val scheduledAt: Instant,
    val grossAmount: Long,
    val currency: String,
    val commissionAmount: Long,
    val netCoachAmount: Long,
    val paymentStatus: SessionPaymentStatus,
)
