package com.gymatch.domain.i18n

data class RegionConfig(
    val regionCode: String,
    val languageTag: String,
    val currencyCode: String,
    val timezoneId: String,
    val taxPercent: Double,
)

interface RegionConfigRepository {
    suspend fun getRegionConfig(regionCode: String): RegionConfig
    suspend fun getSupportedLanguages(regionCode: String): List<String>
    suspend fun getSupportedCurrencies(regionCode: String): List<String>
}
