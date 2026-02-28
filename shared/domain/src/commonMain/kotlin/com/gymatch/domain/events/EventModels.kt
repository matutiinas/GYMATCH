package com.gymatch.domain.events

import kotlinx.datetime.Instant

enum class AttendanceStatus { REGISTERED, CHECKED_IN, NO_SHOW, CANCELED }

data class SportEvent(
    val eventId: String,
    val creatorUserId: String,
    val title: String,
    val sportCode: String,
    val cityCode: String,
    val startsAt: Instant,
    val capacity: Int,
)

data class EventRegistration(
    val eventId: String,
    val userId: String,
    val status: AttendanceStatus,
    val registeredAt: Instant,
)
