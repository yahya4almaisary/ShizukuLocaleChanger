package com.example.shizukulocalechanger;

interface ILocaleChanger {
    /**
     * تغيير لغة النظام
     * @param locale رمز اللغة (مثل ar-SA, en-US)
     * @return true إذا نجح التغيير
     */
    boolean changeLocale(String locale);

    /**
     * الحصول على اللغة الحالية
     * @return رمز اللغة الحالي
     */
    String getCurrentLocale();

    /**
     * قائمة اللغات المدعومة
     * @return مصفوفة من رموز اللغات
     */
    String[] getSupportedLocales();
}
