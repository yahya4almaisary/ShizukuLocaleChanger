package com.example.shizukulocalechanger

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import java.lang.reflect.Method

/**
 * مساعد لتغيير اللغة عبر WRITE_SECURE_SETTINGS
 * يدعم أندرويد 11+ و Android 13+
 */
object LocaleChangeHelper {
    private const val TAG = "LocaleChangeHelper"

    /**
     * تغيير اللغة باستخدام Settings.Secure
     * هذه الطريقة تتطلب صلاحية WRITE_SECURE_SETTINGS
     */
    fun changeLocaleViaSettings(context: Context, localeCode: String): Boolean {
        return try {
            Log.d(TAG, "محاولة تغيير اللغة عبر Settings.Secure إلى: $localeCode")

            val result = Settings.Secure.putString(
                context.contentResolver,
                "system_locales",
                localeCode
            )

            if (result) {
                Log.d(TAG, "تم تغيير اللغة بنجاح عبر Settings.Secure")
            } else {
                Log.e(TAG, "فشل تغيير اللغة عبر Settings.Secure")
            }

            result
        } catch (e: Exception) {
            Log.e(TAG, "خطأ في تغيير اللغة عبر Settings.Secure: ${e.message}", e)
            false
        }
    }

    /**
     * تغيير اللغة باستخدام IActivityManager (للإصدارات القديمة)
     * هذه الطريقة قد لا تعمل على Android 11+
     */
    fun changeLocaleViaActivityManager(localeCode: String): Boolean {
        return try {
            Log.d(TAG, "محاولة تغيير اللغة عبر IActivityManager إلى: $localeCode")

            // الحصول على IActivityManager
            val activityManagerClass = Class.forName("android.app.ActivityManagerNative")
            val getDefaultMethod = activityManagerClass.getMethod("getDefault")
            val activityManager = getDefaultMethod.invoke(null)

            // الحصول على Configuration الحالية
            val getConfigurationMethod = activityManager.javaClass.getMethod("getConfiguration")
            val configuration = getConfigurationMethod.invoke(activityManager)

            // تعيين اللغة الجديدة
            val configClass = configuration.javaClass
            val localeField = configClass.getField("locale")
            
            // إنشاء Locale جديد
            val localeClass = java.util.Locale::class.java
            val locale = if (localeCode.contains("-")) {
                val parts = localeCode.split("-")
                localeClass.getConstructor(String::class.java, String::class.java)
                    .newInstance(parts[0], parts[1])
            } else {
                localeClass.getConstructor(String::class.java)
                    .newInstance(localeCode)
            }

            localeField.set(configuration, locale)

            // تعيين userSetLocale
            val userSetLocaleField = configClass.getField("userSetLocale")
            userSetLocaleField.setBoolean(configuration, true)

            // تحديث Configuration
            val updateConfigurationMethod = activityManager.javaClass.getMethod(
                "updateConfiguration",
                android.content.res.Configuration::class.java
            )
            updateConfigurationMethod.invoke(activityManager, configuration)

            Log.d(TAG, "تم تغيير اللغة بنجاح عبر IActivityManager")
            true
        } catch (e: Exception) {
            Log.e(TAG, "خطأ في تغيير اللغة عبر IActivityManager: ${e.message}", e)
            false
        }
    }

    /**
     * الحصول على اللغة الحالية من Settings.Secure
     */
    fun getCurrentLocaleFromSettings(context: Context): String {
        return try {
            val currentLocale = Settings.Secure.getString(
                context.contentResolver,
                "system_locales"
            ) ?: java.util.Locale.getDefault().toString()

            Log.d(TAG, "اللغة الحالية: $currentLocale")
            currentLocale
        } catch (e: Exception) {
            Log.e(TAG, "خطأ في الحصول على اللغة الحالية: ${e.message}")
            java.util.Locale.getDefault().toString()
        }
    }

    /**
     * التحقق من دعم تغيير اللغة على هذا الإصدار من أندرويد
     */
    fun isLocaleChangeSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R // Android 11+
    }

    /**
     * الحصول على معلومات الإصدار
     */
    fun getAndroidVersionInfo(): String {
        return "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    }
}
