# تطبيق Shizuku Locale Changer

تطبيق أندرويد متقدم يستخدم **Shizuku** لتغيير لغة النظام إلى العربية (أو أي لغة أخرى) دون الحاجة إلى Root أو أوامر ADB متكررة.

## المميزات

✅ **تغيير اللغة بسهولة**: واجهة مستخدم بديهية وسهلة الاستخدام  
✅ **بدون Root**: يستخدم Shizuku بدلاً من صلاحيات Root  
✅ **بدون ADB متكرر**: تفعيل لمرة واحدة فقط  
✅ **دعم لغات متعددة**: العربية والإنجليزية والفرنسية والإسبانية والألمانية والصينية واليابانية والكورية  
✅ **Android 11+**: يدعم الإصدارات الحديثة من أندرويد  
✅ **واجهة Material Design**: تصميم حديث وأنيق  

## المتطلبات

- **Android 11 أو أحدث** (API 30+)
- **تطبيق Shizuku** مثبت على الجهاز
- **Wireless Debugging** مفعل على الجهاز (لتفعيل Shizuku)

## التثبيت والإعداد

### 1. تثبيت Shizuku

أولاً، يجب تثبيت تطبيق Shizuku:

- قم بتحميل تطبيق Shizuku من [الموقع الرسمي](https://shizuku.rikka.app/download/)
- أو من [Google Play Store](https://play.google.com/store/apps/details?id=moe.shizuku.privileged.api)

### 2. تفعيل Wireless Debugging

على جهازك:

1. اذهب إلى **الإعدادات** → **حول الهاتف**
2. اضغط على **رقم الإصدار** 7 مرات لتفعيل وضع المطور
3. اذهب إلى **الإعدادات** → **خيارات المطور**
4. فعّل **Wireless Debugging**
5. اضغط على **Wireless Debugging** واختر **Pair with pairing code**
6. انسخ رمز الإقران

### 3. تفعيل Shizuku

1. افتح تطبيق Shizuku
2. اضغط على **Start** أو **تفعيل**
3. اختر **Wireless Debugging**
4. أدخل رمز الإقران الذي نسخته
5. اضغط **Pair**

### 4. تثبيت التطبيق

```bash
# بناء التطبيق
./gradlew assembleRelease

# تثبيت APK
adb install app/build/outputs/apk/release/app-release.apk
```

أو استخدم Android Studio مباشرة:
- اضغط على **Run** أو **Shift + F10**

## الاستخدام

1. افتح تطبيق **Shizuku Locale Changer**
2. اختر اللغة المطلوبة من القائمة
3. اضغط على زر **تطبيق** (Apply)
4. انتظر قليلاً حتى تتغير لغة النظام

## اللغات المدعومة

| الكود | اللغة |
|------|-------|
| ar-SA | العربية - السعودية |
| ar-AE | العربية - الإمارات |
| ar-EG | العربية - مصر |
| en-US | English - United States |
| en-GB | English - United Kingdom |
| fr-FR | Français - France |
| es-ES | Español - España |
| de-DE | Deutsch - Deutschland |
| zh-CN | 中文 - Simplified Chinese |
| ja-JP | 日本語 - Japanese |
| ko-KR | 한국어 - Korean |

## البنية المعمارية

```
ShizukuLocaleChanger/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/shizukulocalechanger/
│   │   │   │   ├── MainActivity.kt              # النشاط الرئيسي
│   │   │   │   ├── LocaleChangerService.kt      # خدمة تغيير اللغة
│   │   │   │   ├── LocaleChangeHelper.kt        # مساعد تغيير اللغة
│   │   │   │   ├── ShizukuManager.kt            # مدير Shizuku
│   │   │   │   ├── LanguageAdapter.kt           # محول قائمة اللغات
│   │   │   │   └── Locale.kt                    # فئة اللغة
│   │   │   ├── aidl/
│   │   │   │   └── ILocaleChanger.aidl          # واجهة AIDL
│   │   │   └── res/
│   │   │       ├── layout/                      # ملفات التخطيط
│   │   │       └── values/                      # الموارد
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── README.md
```

## الملفات الرئيسية

### MainActivity.kt
- النشاط الرئيسي للتطبيق
- إدارة واجهة المستخدم
- معالجة اختيار اللغة

### LocaleChangerService.kt
- خدمة تغيير اللغة الفعلية
- تطبيق واجهة AIDL
- تغيير اللغة عبر Settings.Secure

### ShizukuManager.kt
- إدارة اتصالات Shizuku
- التحقق من الصلاحيات
- طلب الصلاحيات

### LocaleChangeHelper.kt
- مساعد لتغيير اللغة
- دعم طرق متعددة
- معالجة الأخطاء

## الصلاحيات المطلوبة

```xml
<uses-permission android:name="android.permission.WRITE_SECURE_SETTINGS" />
<uses-permission android:name="android.permission.INTERACT_ACROSS_USERS_FULL" />
```

## معلومات تقنية

### التبعيات الرئيسية

```gradle
// Shizuku API
implementation 'dev.rikka.shizuku:api:13.1.5'
implementation 'dev.rikka.shizuku:provider:13.1.5'

// AndroidX
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.11.0'

// Kotlin
implementation 'org.jetbrains.kotlin:kotlin-stdlib:1.9.10'
```

### إصدارات أندرويد المدعومة

| الإصدار | الحد الأدنى | الهدف |
|---------|-----------|------|
| minSdk | 30 (Android 11) | - |
| targetSdk | 34 (Android 14) | - |
| compileSdk | 34 (Android 14) | - |

## استكشاف الأخطاء

### المشكلة: "Shizuku غير متاح"

**الحل:**
- تأكد من تثبيت تطبيق Shizuku
- تأكد من تفعيل Wireless Debugging
- أعد تشغيل تطبيق Shizuku

### المشكلة: "تم رفض الصلاحية"

**الحل:**
- افتح تطبيق Shizuku
- تأكد من منح صلاحيات التطبيق
- أعد تشغيل التطبيق

### المشكلة: اللغة لم تتغير

**الحل:**
- تأكد من أن الجهاز يدعم تغيير اللغة (Android 11+)
- أعد تشغيل الجهاز
- تحقق من سجلات التطبيق

## التطوير

### بناء المشروع

```bash
# بناء debug
./gradlew assembleDebug

# بناء release
./gradlew assembleRelease

# تشغيل الاختبارات
./gradlew test
```

### التصحيح

```bash
# عرض السجلات
adb logcat | grep "LocaleChanger"

# تثبيت وتشغيل debug
./gradlew installDebug
```

## الترخيص

هذا المشروع مرخص تحت رخصة MIT.

## المساهمة

نرحب بالمساهمات! يرجى:

1. Fork المشروع
2. إنشاء فرع للميزة الجديدة (`git checkout -b feature/AmazingFeature`)
3. Commit التغييرات (`git commit -m 'Add some AmazingFeature'`)
4. Push إلى الفرع (`git push origin feature/AmazingFeature`)
5. فتح Pull Request

## الدعم

إذا واجهت أي مشاكل، يرجى:

1. التحقق من [الأسئلة الشائعة](#استكشاف-الأخطاء)
2. فتح Issue جديد مع وصف المشكلة
3. إرفاق السجلات والتفاصيل

## المراجع

- [Shizuku API Documentation](https://github.com/RikkaApps/Shizuku-API)
- [Android Developer Documentation](https://developer.android.com/)
- [Material Design Guidelines](https://material.io/design)

---

**ملاحظة**: هذا التطبيق يتطلب Shizuku ولا يعمل بدونه. Shizuku هو تطبيق منفصل يوفر صلاحيات محدودة دون الحاجة إلى Root.
