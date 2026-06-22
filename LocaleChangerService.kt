package com.example.shizukulocalechanger

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.example.shizukulocalechanger.ILocaleChanger
import java.util.Locale

/**
 * خدمة تغيير اللغة باستخدام Shizuku
 * تعمل بصلاحيات shell (ADB)
 */
class LocaleChangerService(private val context: Context? = null) : ILocaleChanger.Stub() {

    companion object {
        private const val TAG = "LocaleChangerService"

        // قائمة اللغات المدعومة
        val SUPPORTED_LOCALES = arrayOf(
            "ar-SA",  // العربية - السعودية
            "ar-AE",  // العربية - الإمارات
            "ar-EG",  // العربية - مصر
            "en-US",  // الإنجليزية - الولايات المتحدة
            "en-GB",  // الإنجليزية - بريطانيا
            "fr-FR",  // الفرنسية
            "es-ES",  // الإسبانية
            "de-DE",  // الألمانية
            "zh-CN",  // الصينية - المبسطة
            "ja-JP",  // اليابانية
            "ko-KR"   // الكورية
        )

        private val LOCALE_DISPLAY_NAMES = mapOf(
            "ar-SA" to "العربية",
            "ar-AE" to "العربية (الإمارات)",
            "ar-EG" to "العربية (مصر)",
            "en-US" to "English (US)",
            "en-GB" to "English (UK)",
            "fr-FR" to "Français",
            "es-ES" to "Español",
            "de-DE" to "Deutsch",
            "zh-CN" to "中文 (简体)",
            "ja-JP" to "日本語",
            "ko-KR" to "한국어"
        )
    }

    /**
     * تغيير لغة النظام
     */
    override fun changeLocale(locale: String): Boolean {
        return try {
            Log.d(TAG, "محاولة تغيير اللغة إلى: $locale")

            // التحقق من أن اللغة مدعومة
            if (!SUPPORTED_LOCALES.contains(locale)) {
                Log.e(TAG, "اللغة غير مدعومة: $locale")
                return false
            }

            // تغيير اللغة عبر Settings.Secure
            // هذا يتطلب صلاحية WRITE_SECURE_SETTINGS
            val result = Settings.Secure.putString(
                context?.contentResolver,
                "system_locales",
                locale
            )

            if (result) {
                Log.d(TAG, "تم تغيير اللغة بنجاح إلى: $locale")
            } else {
                Log.e(TAG, "فشل تغيير اللغة")
            }

            result
        } catch (e: Exception) {
            Log.e(TAG, "خطأ في تغيير اللغة: ${e.message}", e)
            false
        }
    }

    /**
     * الحصول على اللغة الحالية
     */
    override fun getCurrentLocale(): String {
        return try {
            // محاولة الحصول على اللغة من Settings.Secure
            val currentLocale = Settings.Secure.getString(
                context?.contentResolver,
                "system_locales"
            ) ?: Locale.getDefault().toString()

            Log.d(TAG, "اللغة الحالية: $currentLocale")
            currentLocale
        } catch (e: Exception) {
            Log.e(TAG, "خطأ في الحصول على اللغة الحالية: ${e.message}", e)
            Locale.getDefault().toString()
        }
    }

    /**
     * قائمة اللغات المدعومة
     */
    override fun getSupportedLocales(): Array<String> {
        return SUPPORTED_LOCALES
    }

    /**
     * الحصول على اسم اللغة المعروض
     */
    fun getLocaleDisplayName(locale: String): String {
        return LOCALE_DISPLAY_NAMES[locale] ?: locale
    }
}
