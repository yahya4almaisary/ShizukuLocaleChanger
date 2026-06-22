# البنية المعمارية لتطبيق Shizuku Locale Changer

## نظرة عامة

تطبيق Shizuku Locale Changer مبني على معمارية نظيفة وقابلة للتوسع تفصل بين طبقات مختلفة من التطبيق.

```
┌─────────────────────────────────────────────┐
│         Presentation Layer                  │
│  (MainActivity, UI Components)              │
└────────────────┬────────────────────────────┘
                 │
┌────────────────▼────────────────────────────┐
│         Business Logic Layer                │
│  (LocaleChangerService, ShizukuManager)     │
└────────────────┬────────────────────────────┘
                 │
┌────────────────▼────────────────────────────┐
│         Data Access Layer                   │
│  (LocaleChangeHelper, Settings)             │
└────────────────┬────────────────────────────┘
                 │
┌────────────────▼────────────────────────────┐
│         System Layer                        │
│  (Shizuku, Android Framework)               │
└─────────────────────────────────────────────┘
```

## الطبقات المعمارية

### 1. Presentation Layer (طبقة العرض)

**المسؤولية**: عرض واجهة المستخدم والتفاعل مع المستخدم

**المكونات الرئيسية:**

#### MainActivity.kt
- النشاط الرئيسي للتطبيق
- إدارة دورة حياة التطبيق
- معالجة أحداث واجهة المستخدم
- عرض قائمة اللغات

```kotlin
class MainActivity : AppCompatActivity() {
    // إدارة واجهة المستخدم
    // معالجة الأحداث
    // عرض الرسائل
}
```

#### LanguageAdapter.kt
- محول لعرض قائمة اللغات
- إدارة اختيار اللغة
- تحديث الواجهة

```kotlin
class LanguageAdapter : RecyclerView.Adapter<ViewHolder>() {
    // عرض قائمة اللغات
    // معالجة الاختيار
}
```

#### Layout Files
- `activity_main.xml` - التخطيط الرئيسي
- `language_item.xml` - عنصر اللغة

### 2. Business Logic Layer (طبقة المنطق التجاري)

**المسؤولية**: معالجة المنطق التجاري والتحقق من الصحة

**المكونات الرئيسية:**

#### LocaleChangerService.kt
- خدمة تغيير اللغة الرئيسية
- تطبيق واجهة AIDL
- إدارة قائمة اللغات المدعومة
- معالجة الأخطاء

```kotlin
class LocaleChangerService : ILocaleChanger.Stub() {
    override fun changeLocale(locale: String): Boolean
    override fun getCurrentLocale(): String
    override fun getSupportedLocales(): Array<String>
}
```

#### ShizukuManager.kt
- إدارة اتصالات Shizuku
- التحقق من توفر Shizuku
- طلب الصلاحيات
- معالجة الأخطاء

```kotlin
object ShizukuManager {
    fun isShizukuAvailable(): Boolean
    fun hasPermission(): Boolean
    fun requestPermission(requestCode: Int)
}
```

### 3. Data Access Layer (طبقة الوصول إلى البيانات)

**المسؤولية**: الوصول إلى البيانات والإعدادات

**المكونات الرئيسية:**

#### LocaleChangeHelper.kt
- تغيير اللغة عبر Settings.Secure
- تغيير اللغة عبر IActivityManager
- الحصول على اللغة الحالية
- معالجة الأخطاء

```kotlin
object LocaleChangeHelper {
    fun changeLocaleViaSettings(context: Context, locale: String): Boolean
    fun changeLocaleViaActivityManager(locale: String): Boolean
    fun getCurrentLocaleFromSettings(context: Context): String
}
```

#### Locale.kt
- فئة تمثل اللغة
- تخزين بيانات اللغة

```kotlin
data class Locale(
    val code: String,
    val displayName: String
)
```

### 4. System Layer (طبقة النظام)

**المسؤولية**: التفاعل مع نظام أندرويد

**المكونات الرئيسية:**

#### AIDL Interface (ILocaleChanger.aidl)
- واجهة للاتصال بين العمليات
- تعريف الطرق المتاحة

```aidl
interface ILocaleChanger {
    boolean changeLocale(String locale);
    String getCurrentLocale();
    String[] getSupportedLocales();
}
```

#### Android Framework
- Settings.Secure
- IActivityManager
- Shizuku API

## تدفق البيانات

### تدفق تغيير اللغة

```
User Action
    ↓
MainActivity.applyLanguageChange()
    ↓
ShizukuManager.hasPermission()
    ↓
LocaleChangerService.changeLocale()
    ↓
LocaleChangeHelper.changeLocaleViaSettings()
    ↓
Settings.Secure.putString()
    ↓
System Language Changed
    ↓
UI Updated
```

### تدفق الحصول على اللغة الحالية

```
MainActivity.onCreate()
    ↓
LocaleChangerService.getCurrentLocale()
    ↓
LocaleChangeHelper.getCurrentLocaleFromSettings()
    ↓
Settings.Secure.getString()
    ↓
Display Current Language
```

## الاتصالات بين المكونات

### MainActivity ↔ LocaleChangerService

```kotlin
// MainActivity
val localeService = LocaleChangerService(this)
val success = localeService.changeLocale(selectedLocale)

// LocaleChangerService
override fun changeLocale(locale: String): Boolean {
    return LocaleChangeHelper.changeLocaleViaSettings(context, locale)
}
```

### MainActivity ↔ ShizukuManager

```kotlin
// MainActivity
if (!ShizukuManager.isShizukuAvailable()) {
    showError("Shizuku not available")
}

if (!ShizukuManager.hasPermission()) {
    ShizukuManager.requestPermission(PERMISSION_REQUEST_CODE)
}

// ShizukuManager
object ShizukuManager {
    fun isShizukuAvailable(): Boolean = Shizuku.getUid() != -1
    fun hasPermission(): Boolean = Shizuku.checkSelfPermission() == PERMISSION_GRANTED
}
```

## معالجة الأخطاء

### استراتيجية معالجة الأخطاء

1. **Try-Catch Blocks**: التقاط الاستثناءات
2. **Logging**: تسجيل الأخطاء
3. **User Feedback**: إبلاغ المستخدم

```kotlin
try {
    val result = localeService.changeLocale(selectedLocale)
    if (result) {
        showSuccess("Language changed successfully")
    } else {
        showError("Failed to change language")
    }
} catch (e: Exception) {
    Log.e(TAG, "Error: ${e.message}")
    showError("Error: ${e.message}")
}
```

## الصلاحيات والأمان

### الصلاحيات المطلوبة

```xml
<uses-permission android:name="android.permission.WRITE_SECURE_SETTINGS" />
<uses-permission android:name="android.permission.INTERACT_ACROSS_USERS_FULL" />
```

### Shizuku Provider

```xml
<provider
    android:name="rikka.shizuku.ShizukuProvider"
    android:authorities="${applicationId}.shizuku"
    android:permission="android.permission.INTERACT_ACROSS_USERS_FULL"
    android:exported="true" />
```

## التوسع المستقبلي

### إضافة ميزات جديدة

1. **دعم لغات إضافية**: إضافة لغات جديدة إلى قائمة اللغات المدعومة
2. **حفظ التفضيلات**: حفظ اختيار اللغة المفضل
3. **جدولة التغييرات**: تغيير اللغة في أوقات محددة
4. **واجهة متقدمة**: إضافة إعدادات متقدمة

### مثال على إضافة ميزة جديدة

```kotlin
// إضافة ميزة حفظ التفضيلات
class PreferenceManager(context: Context) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    
    fun savePreferredLocale(locale: String) {
        prefs.edit().putString("preferred_locale", locale).apply()
    }
    
    fun getPreferredLocale(): String? {
        return prefs.getString("preferred_locale", null)
    }
}
```

## الأداء والتحسينات

### تحسينات الأداء

1. **Caching**: تخزين مؤقت للغات المدعومة
2. **Lazy Loading**: تحميل البيانات عند الحاجة
3. **Async Operations**: العمليات غير المتزامنة

```kotlin
// استخدام Coroutines للعمليات غير المتزامنة
lifecycleScope.launch {
    val result = withContext(Dispatchers.IO) {
        localeService.changeLocale(selectedLocale)
    }
    updateUI(result)
}
```

## الاختبار

### أنواع الاختبارات

1. **Unit Tests**: اختبار الوحدات الفردية
2. **Integration Tests**: اختبار التكامل بين المكونات
3. **UI Tests**: اختبار واجهة المستخدم

```kotlin
// Unit Test Example
@Test
fun testChangeLocale() {
    val service = LocaleChangerService()
    val result = service.changeLocale("ar-SA")
    assertTrue(result)
}
```

## الملفات الرئيسية

| الملف | الطبقة | الوصف |
|------|--------|-------|
| MainActivity.kt | Presentation | النشاط الرئيسي |
| LocaleChangerService.kt | Business Logic | خدمة تغيير اللغة |
| ShizukuManager.kt | Business Logic | مدير Shizuku |
| LocaleChangeHelper.kt | Data Access | مساعد تغيير اللغة |
| ILocaleChanger.aidl | System | واجهة AIDL |

## الخلاصة

البنية المعمارية تتبع مبادئ:
- **Separation of Concerns**: فصل المسؤوليات
- **Single Responsibility**: مسؤولية واحدة لكل فئة
- **Dependency Injection**: حقن التبعيات
- **Error Handling**: معالجة الأخطاء بشكل صحيح
- **Scalability**: قابلية التوسع

هذا يجعل التطبيق سهل الصيانة والتطوير والاختبار.
