package com.gymatch.domain.events

interface EventRepository {
    suspend fun createEvent(event: SportEvent): SportEvent
    suspend fun listEvents(cityCode: String, sportCode: String?, limit: Int): List<SportEvent>
    suspend fun register(eventId: String, userId: String): EventRegistration
    suspend fun markAttendance(eventId: String, userId: String): EventRegistration
}
