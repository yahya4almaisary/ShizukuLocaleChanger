package com.example.shizukulocalechanger

/**
 * فئة تمثل اللغة
 */
data class Locale(
    val code: String,
    val displayName: String,
    val nativeName: String = ""
) {
    companion object {
        fun fromCode(code: String): Locale {
            val service = LocaleChangerService()
            val displayName = service.getLocaleDisplayName(code)
            return Locale(code, displayName)
        }
    }
}
