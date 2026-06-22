# تعليمات بناء تطبيق Shizuku Locale Changer

## المتطلبات

### البرامج المطلوبة
- **Android Studio** 2023.1 أو أحدث
- **Java Development Kit (JDK)** 11 أو أحدث
- **Android SDK** مع API Level 34

### إعدادات Android SDK

تأكد من تثبيت المكونات التالية:

```
Android SDK Platform 34
Android SDK Build-Tools 34.x.x
Android Emulator (اختياري)
```

## خطوات البناء

### 1. استنساخ المشروع أو فتحه

```bash
# إذا كنت تستخدم Git
git clone <repository-url>
cd ShizukuLocaleChanger_Android

# أو افتح المشروع مباشرة في Android Studio
```

### 2. تحديث التبعيات

```bash
# تحديث Gradle
./gradlew wrapper --gradle-version=8.1.0

# تحميل التبعيات
./gradlew build
```

### 3. بناء التطبيق

#### بناء Debug APK

```bash
./gradlew assembleDebug
```

الملف الناتج: `app/build/outputs/apk/debug/app-debug.apk`

#### بناء Release APK

```bash
./gradlew assembleRelease
```

الملف الناتج: `app/build/outputs/apk/release/app-release.apk`

### 4. توقيع Release APK (اختياري)

إذا كنت تريد توقيع APK بشهادة خاصة:

```bash
# إنشاء keystore
keytool -genkey -v -keystore my-release-key.keystore -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias

# توقيع APK
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore my-release-key.keystore app/build/outputs/apk/release/app-release.apk my-key-alias

# تحسين APK
zipalign -v 4 app/build/outputs/apk/release/app-release.apk app-release-aligned.apk
```

## التثبيت على الجهاز

### عبر ADB

```bash
# تثبيت Debug APK
adb install app/build/outputs/apk/debug/app-debug.apk

# أو
adb install-multiple app/build/outputs/apk/debug/app-debug.apk

# إعادة تثبيت
adb install -r app/build/outputs/apk/debug/app-debug.apk

# إلغاء التثبيت
adb uninstall com.example.shizukulocalechanger
```

### عبر Android Studio

1. اختر **Run** → **Run 'app'** أو اضغط **Shift + F10**
2. اختر الجهاز أو المحاكي
3. اضغط **OK**

## استكشاف مشاكل البناء

### المشكلة: "SDK location not found"

**الحل:**
```bash
# إنشاء ملف local.properties
echo "sdk.dir=/path/to/android/sdk" > local.properties

# على Linux/Mac
echo "sdk.dir=$ANDROID_HOME" > local.properties

# على Windows
echo "sdk.dir=C:\\Android\\sdk" > local.properties
```

### المشكلة: "Gradle build failed"

**الحل:**
```bash
# تنظيف المشروع
./gradlew clean

# إعادة البناء
./gradlew build
```

### المشكلة: "Compilation failed"

**الحل:**
```bash
# تحديث التبعيات
./gradlew dependencies --refresh-dependencies

# حذف ملفات الذاكرة المؤقتة
rm -rf ~/.gradle/caches
./gradlew build
```

## التشغيل والاختبار

### تشغيل التطبيق

```bash
# تشغيل على جهاز متصل أو محاكي
./gradlew installDebug
adb shell am start -n com.example.shizukulocalechanger/.MainActivity
```

### عرض السجلات

```bash
# عرض جميع السجلات
adb logcat

# تصفية السجلات
adb logcat | grep "LocaleChanger"

# حفظ السجلات في ملف
adb logcat > logs.txt
```

### الاختبارات

```bash
# تشغيل اختبارات Unit
./gradlew test

# تشغيل اختبارات Instrumented
./gradlew connectedAndroidTest
```

## إنشاء Build Variants

### Debug Build

```bash
# يحتوي على معلومات التصحيح
./gradlew assembleDebug
```

### Release Build

```bash
# محسّن وموقّع
./gradlew assembleRelease
```

## تحسين الأداء

### تقليل حجم APK

```bash
# تفعيل ProGuard/R8
# في build.gradle:
# minifyEnabled true
# shrinkResources true

./gradlew assembleRelease
```

### تسريع البناء

```bash
# استخدام Gradle Daemon
org.gradle.daemon=true

# استخدام Parallel Builds
org.gradle.parallel=true

# استخدام Build Cache
org.gradle.caching=true
```

## الملفات المهمة

| الملف | الوصف |
|------|-------|
| `build.gradle` | إعدادات البناء الرئيسية |
| `app/build.gradle` | إعدادات تطبيق |
| `settings.gradle` | إعدادات المشروع |
| `gradle.properties` | خصائص Gradle |
| `local.properties` | إعدادات محلية (SDK path) |

## نصائح مهمة

1. **استخدم Android Studio**: يوفر واجهة رسومية سهلة للبناء والتشغيل
2. **تحديث SDK**: تأكد من تحديث Android SDK بانتظام
3. **استخدام Gradle Wrapper**: استخدم `./gradlew` بدلاً من `gradle`
4. **حذف ملفات الذاكرة المؤقتة**: إذا واجهت مشاكل، حاول حذف مجلد `build/` و `.gradle/`
5. **استخدام ProGuard**: لتقليل حجم APK وحماية الكود

## المراجع

- [Android Studio Build Documentation](https://developer.android.com/studio/build)
- [Gradle Documentation](https://gradle.org/documentation/)
- [Android SDK Documentation](https://developer.android.com/studio/releases/gradle-plugin)

---

**ملاحظة**: تأكد من أن جميع الملفات موجودة وأن الهيكل صحيح قبل محاولة البناء.
