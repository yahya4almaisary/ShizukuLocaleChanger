package com.example.shizukulocalechanger

import android.content.pm.PackageManager
import android.util.Log
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuProvider

/**
 * مدير Shizuku للتعامل مع الصلاحيات والخدمات
 */
object ShizukuManager {
    private const val TAG = "ShizukuManager"
    private const val SHIZUKU_PERMISSION = "moe.shizuku.privileged.api.PERMISSION"

    /**
     * التحقق من توفر Shizuku
     */
    fun isShizukuAvailable(): Boolean {
        return try {
            Shizuku.getUid() != -1
        } catch (e: Exception) {
            Log.e(TAG, "خطأ في التحقق من توفر Shizuku: ${e.message}")
            false
        }
    }

    /**
     * التحقق من منح الصلاحية
     */
    fun hasPermission(): Boolean {
        return try {
            Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        } catch (e: Exception) {
            Log.e(TAG, "خطأ في التحقق من الصلاحية: ${e.message}")
            false
        }
    }

    /**
     * طلب الصلاحية
     */
    fun requestPermission(requestCode: Int) {
        try {
            if (Shizuku.isPreV11()) {
                Log.e(TAG, "إصدار Shizuku قديم جداً")
                return
            }

            if (hasPermission()) {
                Log.d(TAG, "الصلاحية مُمنحة بالفعل")
                return
            }

            if (Shizuku.shouldShowRequestPermissionRationale()) {
                Log.d(TAG, "المستخدم رفض الصلاحية سابقاً")
                return
            }

            Shizuku.requestPermission(requestCode)
        } catch (e: Exception) {
            Log.e(TAG, "خطأ في طلب الصلاحية: ${e.message}")
        }
    }

    /**
     * الحصول على معلومات Shizuku
     */
    fun getShizukuInfo(): String {
        return try {
            val uid = Shizuku.getUid()
            val version = Shizuku.getVersion()
            val isRoot = uid == 0
            val privilege = if (isRoot) "ROOT" else "ADB (Shell)"

            "Shizuku Version: $version\nPrivilege: $privilege\nUID: $uid"
        } catch (e: Exception) {
            "خطأ في الحصول على معلومات Shizuku: ${e.message}"
        }
    }
}
