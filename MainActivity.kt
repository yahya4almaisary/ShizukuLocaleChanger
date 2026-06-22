package com.example.shizukulocalechanger

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import rikka.shizuku.Shizuku

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val PERMISSION_REQUEST_CODE = 100
    }

    private lateinit var currentLanguageText: TextView
    private lateinit var languageContainer: LinearLayout
    private lateinit var applyButton: MaterialButton
    private lateinit var cancelButton: MaterialButton
    private lateinit var localeService: LocaleChangerService

    private var selectedLocale: String = ""
    private val supportedLocales = listOf(
        "ar-SA" to "العربية - السعودية",
        "ar-AE" to "العربية - الإمارات",
        "ar-EG" to "العربية - مصر",
        "en-US" to "English - United States",
        "en-GB" to "English - United Kingdom",
        "fr-FR" to "Français - France",
        "es-ES" to "Español - España",
        "de-DE" to "Deutsch - Deutschland",
        "zh-CN" to "中文 - 简体",
        "ja-JP" to "日本語 - Japan",
        "ko-KR" to "한국어 - Korea"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // تهيئة المتغيرات
        currentLanguageText = findViewById(R.id.currentLanguageText)
        languageContainer = findViewById(R.id.languageContainer)
        applyButton = findViewById(R.id.applyButton)
        cancelButton = findViewById(R.id.cancelButton)

        // إنشاء خدمة تغيير اللغة
        localeService = LocaleChangerService(this)

        // تهيئة واجهة المستخدم
        initializeUI()

        // التحقق من Shizuku والصلاحيات
        checkShizukuAndPermissions()
    }

    /**
     * تهيئة واجهة المستخدم
     */
    private fun initializeUI() {
        // عرض اللغة الحالية
        updateCurrentLanguageDisplay()

        // إنشاء قائمة اللغات
        createLanguageList()

        // إعداد أزرار التطبيق والإلغاء
        applyButton.setOnClickListener {
            applyLanguageChange()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    /**
     * تحديث عرض اللغة الحالية
     */
    private fun updateCurrentLanguageDisplay() {
        try {
            val currentLocale = localeService.getCurrentLocale()
            selectedLocale = currentLocale

            val displayName = supportedLocales.find { it.first == currentLocale }?.second
                ?: currentLocale

            currentLanguageText.text = displayName
        } catch (e: Exception) {
            Log.e(TAG, "خطأ في تحديث اللغة الحالية: ${e.message}")
            currentLanguageText.text = "Unknown"
        }
    }

    /**
     * إنشاء قائمة اللغات
     */
    private fun createLanguageList() {
        languageContainer.removeAllViews()

        for ((localeCode, displayName) in supportedLocales) {
            val radioButton = RadioButton(this).apply {
                text = displayName
                isChecked = localeCode == selectedLocale
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedLocale = localeCode
                        applyButton.isEnabled = true
                    }
                }
            }

            languageContainer.addView(radioButton, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8)
            })
        }
    }

    /**
     * التحقق من Shizuku والصلاحيات
     */
    private fun checkShizukuAndPermissions() {
        try {
            // التحقق من توفر Shizuku
            if (!ShizukuManager.isShizukuAvailable()) {
                showError(getString(R.string.shizuku_not_available))
                disableUI()
                return
            }

            // التحقق من الصلاحيات
            if (!ShizukuManager.hasPermission()) {
                Log.d(TAG, "طلب صلاحيات Shizuku")
                ShizukuManager.requestPermission(PERMISSION_REQUEST_CODE)
            } else {
                Log.d(TAG, "الصلاحيات مُمنحة بالفعل")
                enableUI()
            }
        } catch (e: Exception) {
            Log.e(TAG, "خطأ في التحقق من Shizuku: ${e.message}")
            showError("خطأ في التحقق من Shizuku")
            disableUI()
        }
    }

    /**
     * تطبيق تغيير اللغة
     */
    private fun applyLanguageChange() {
        if (selectedLocale.isEmpty()) {
            showError(getString(R.string.error))
            return
        }

        try {
            Log.d(TAG, "محاولة تغيير اللغة إلى: $selectedLocale")

            // التحقق من الصلاحيات مرة أخرى
            if (!ShizukuManager.hasPermission()) {
                showError(getString(R.string.permission_denied))
                return
            }

            // تغيير اللغة
            val success = localeService.changeLocale(selectedLocale)

            if (success) {
                showSuccess(getString(R.string.success))
                updateCurrentLanguageDisplay()
                applyButton.isEnabled = false
            } else {
                showError(getString(R.string.error))
            }
        } catch (e: Exception) {
            Log.e(TAG, "خطأ في تغيير اللغة: ${e.message}")
            showError("خطأ: ${e.message}")
        }
    }

    /**
     * تفعيل واجهة المستخدم
     */
    private fun enableUI() {
        languageContainer.isEnabled = true
        applyButton.isEnabled = true
        cancelButton.isEnabled = true
    }

    /**
     * تعطيل واجهة المستخدم
     */
    private fun disableUI() {
        languageContainer.isEnabled = false
        applyButton.isEnabled = false
        cancelButton.isEnabled = true
    }

    /**
     * عرض رسالة نجاح
     */
    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * عرض رسالة خطأ
     */
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    /**
     * معالجة نتيجة طلب الصلاحية
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "تم منح الصلاحيات")
                enableUI()
            } else {
                Log.d(TAG, "تم رفض الصلاحيات")
                showError(getString(R.string.permission_denied))
                disableUI()
            }
        }
    }
}
