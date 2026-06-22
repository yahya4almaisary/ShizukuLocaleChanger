# هيكل المشروع

## نظرة عامة على الملفات والمجلدات

```
ShizukuLocaleChanger_Android/
├── app/                                    # مجلد التطبيق الرئيسي
│   ├── build.gradle                       # إعدادات بناء التطبيق
│   ├── proguard-rules.pro                 # قواعد ProGuard
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml        # ملف البيان
│           ├── aidl/                      # ملفات AIDL
│           │   └── com/example/shizukulocalechanger/
│           │       └── ILocaleChanger.aidl
│           ├── java/                      # ملفات Kotlin/Java
│           │   └── com/example/shizukulocalechanger/
│           │       ├── MainActivity.kt
│           │       ├── LocaleChangerService.kt
│           │       ├── LocaleChangeHelper.kt
│           │       ├── ShizukuManager.kt
│           │       ├── LanguageAdapter.kt
│           │       └── Locale.kt
│           └── res/                       # الموارد
│               ├── layout/                # ملفات التخطيط
│               │   ├── activity_main.xml
│               │   └── language_item.xml
│               └── values/                # القيم
│                   ├── strings.xml
│                   ├── colors.xml
│                   └── styles.xml
├── build.gradle                           # إعدادات بناء المشروع الرئيسية
├── settings.gradle                        # إعدادات المشروع
├── gradle.properties                      # خصائص Gradle
├── .gitignore                            # ملف تجاهل Git
├── README.md                             # ملف التعريف الرئيسي
├── BUILD_INSTRUCTIONS.md                 # تعليمات البناء
├── INSTALLATION_GUIDE.md                 # دليل التثبيت
├── ARCHITECTURE.md                       # البنية المعمارية
└── PROJECT_STRUCTURE.md                  # هذا الملف
```

## شرح الملفات والمجلدات

### ملفات المستوى الأعلى

| الملف | الوصف |
|------|-------|
| `build.gradle` | إعدادات بناء المشروع الرئيسية (Gradle Plugins) |
| `settings.gradle` | إعدادات المشروع (مثل أسماء المودولات) |
| `gradle.properties` | خصائص Gradle العامة |
| `.gitignore` | ملف تجاهل Git |
| `README.md` | ملف التعريف الرئيسي |
| `BUILD_INSTRUCTIONS.md` | تعليمات البناء والتجميع |
| `INSTALLATION_GUIDE.md` | دليل التثبيت والإعداد |
| `ARCHITECTURE.md` | شرح البنية المعمارية |

### مجلد app/

#### build.gradle
- إعدادات بناء التطبيق
- التبعيات المطلوبة
- إعدادات SDK
- إعدادات الإصدار

#### proguard-rules.pro
- قواعد ProGuard/R8
- حماية الكود
- تقليل حجم APK

#### src/main/AndroidManifest.xml
- تعريف التطبيق
- الأنشطة والخدمات
- الصلاحيات المطلوبة
- الـ Providers

### مجلد src/main/aidl/

#### ILocaleChanger.aidl
- واجهة AIDL لتغيير اللغة
- تعريف الطرق المتاحة:
  - `changeLocale(String)`: تغيير اللغة
  - `getCurrentLocale()`: الحصول على اللغة الحالية
  - `getSupportedLocales()`: قائمة اللغات المدعومة

### مجلد src/main/java/

#### MainActivity.kt
**الوصف**: النشاط الرئيسي للتطبيق

**المسؤوليات**:
- إدارة واجهة المستخدم
- معالجة أحداث الأزرار
- عرض قائمة اللغات
- معالجة الصلاحيات

**الطرق الرئيسية**:
- `onCreate()`: تهيئة النشاط
- `initializeUI()`: تهيئة الواجهة
- `checkShizukuAndPermissions()`: التحقق من Shizuku
- `applyLanguageChange()`: تطبيق تغيير اللغة
- `createLanguageList()`: إنشاء قائمة اللغات

#### LocaleChangerService.kt
**الوصف**: خدمة تغيير اللغة

**المسؤوليات**:
- تطبيق واجهة AIDL
- تغيير اللغة عبر Settings.Secure
- إدارة قائمة اللغات المدعومة

**الطرق الرئيسية**:
- `changeLocale(String)`: تغيير اللغة
- `getCurrentLocale()`: الحصول على اللغة الحالية
- `getSupportedLocales()`: قائمة اللغات المدعومة
- `getLocaleDisplayName(String)`: اسم اللغة المعروض

#### LocaleChangeHelper.kt
**الوصف**: مساعد لتغيير اللغة

**المسؤوليات**:
- تغيير اللغة عبر Settings.Secure
- تغيير اللغة عبر IActivityManager
- الحصول على اللغة الحالية

**الطرق الرئيسية**:
- `changeLocaleViaSettings()`: تغيير عبر Settings
- `changeLocaleViaActivityManager()`: تغيير عبر ActivityManager
- `getCurrentLocaleFromSettings()`: الحصول على اللغة الحالية

#### ShizukuManager.kt
**الوصف**: مدير Shizuku

**المسؤوليات**:
- إدارة اتصالات Shizuku
- التحقق من توفر Shizuku
- طلب الصلاحيات

**الطرق الرئيسية**:
- `isShizukuAvailable()`: التحقق من توفر Shizuku
- `hasPermission()`: التحقق من الصلاحيات
- `requestPermission()`: طلب الصلاحيات

#### LanguageAdapter.kt
**الوصف**: محول لعرض قائمة اللغات

**المسؤوليات**:
- عرض قائمة اللغات
- معالجة اختيار اللغة
- تحديث الواجهة

**الطرق الرئيسية**:
- `onCreateViewHolder()`: إنشاء عنصر
- `onBindViewHolder()`: ربط البيانات
- `getSelectedLocale()`: الحصول على اللغة المختارة

#### Locale.kt
**الوصف**: فئة تمثل اللغة

**الخصائص**:
- `code`: رمز اللغة (مثل ar-SA)
- `displayName`: اسم اللغة المعروض
- `nativeName`: الاسم الأصلي للغة

### مجلد src/main/res/

#### layout/

##### activity_main.xml
- التخطيط الرئيسي للتطبيق
- عرض اللغة الحالية
- قائمة اختيار اللغة
- أزرار التطبيق والإلغاء

##### language_item.xml
- تخطيط عنصر اللغة الواحد
- زر راديو لاختيار اللغة
- اسم اللغة

#### values/

##### strings.xml
- جميع النصوص المستخدمة في التطبيق
- الترجمات (حالياً بالعربية والإنجليزية)

##### colors.xml
- تعريف الألوان المستخدمة
- الألوان الأساسية والثانوية

##### styles.xml
- تعريف الأنماط والثيمات
- نمط التطبيق الرئيسي
- أنماط الأزرار والعناصر

## تدفق الملفات

### تدفق البناء

```
build.gradle (Main)
    ↓
settings.gradle
    ↓
gradle.properties
    ↓
app/build.gradle
    ↓
AndroidManifest.xml
    ↓
Java/Kotlin Files
    ↓
AIDL Files
    ↓
Resource Files
    ↓
APK
```

### تدفق التشغيل

```
AndroidManifest.xml (تعريف التطبيق)
    ↓
MainActivity (النشاط الرئيسي)
    ↓
onCreate() (تهيئة)
    ↓
initializeUI() (إعداد الواجهة)
    ↓
checkShizukuAndPermissions() (التحقق)
    ↓
createLanguageList() (عرض اللغات)
    ↓
User Interaction
    ↓
applyLanguageChange() (تطبيق التغيير)
    ↓
LocaleChangerService (خدمة التغيير)
    ↓
LocaleChangeHelper (تنفيذ التغيير)
    ↓
Settings.Secure (تطبيق على النظام)
```

## المتغيرات البيئية

### gradle.properties

```properties
android.useAndroidX=true
kotlin.code.style=official
android.compileSdkVersion=34
```

### local.properties (لم يتم تضمينه)

```properties
sdk.dir=/path/to/android/sdk
```

## الإعدادات المهمة

### build.gradle (app)

```gradle
compileSdk 34
minSdk 30
targetSdk 34
```

### AndroidManifest.xml

```xml
<uses-permission android:name="android.permission.WRITE_SECURE_SETTINGS" />
<uses-permission android:name="android.permission.INTERACT_ACROSS_USERS_FULL" />
```

## الملفات المُولّدة (عند البناء)

```
app/
├── build/
│   ├── generated/
│   │   ├── aidl/
│   │   ├── java/
│   │   └── res/
│   ├── intermediates/
│   └── outputs/
│       └── apk/
│           ├── debug/
│           │   └── app-debug.apk
│           └── release/
│               └── app-release.apk
```

## ملفات التكوين

### .gitignore
- يتجاهل ملفات Gradle المؤقتة
- يتجاهل ملفات IDE
- يتجاهل ملفات النظام

## الملفات الإضافية

### README.md
- معلومات عامة عن المشروع
- المميزات والمتطلبات
- تعليمات الاستخدام

### BUILD_INSTRUCTIONS.md
- تعليمات البناء التفصيلية
- حل مشاكل البناء الشائعة

### INSTALLATION_GUIDE.md
- دليل التثبيت خطوة بخطوة
- استكشاف الأخطاء الشائعة

### ARCHITECTURE.md
- شرح البنية المعمارية
- تدفق البيانات
- الاتصالات بين المكونات

## الخلاصة

هيكل المشروع منظم بشكل جيد:
- **فصل الاهتمامات**: كل ملف له مسؤولية واحدة
- **سهولة الصيانة**: سهل إيجاد وتعديل الملفات
- **قابلية التوسع**: يمكن إضافة ميزات جديدة بسهولة
- **توثيق شامل**: ملفات توثيق تشرح كل شيء

---

**ملاحظة**: تأكد من وجود جميع الملفات قبل محاولة البناء.
