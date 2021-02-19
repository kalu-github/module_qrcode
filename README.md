#
#### demo

![image](https://github.com/153437803/zxing_lite/blob/master/logo.gif )

#
#### zxing-3.4.1
```
https://github.com/zxing/zxing/releases
```

#
#### 1.生成二维码图片
```
    /**
     * @param context 上下文
     * @param message 二维码信息
     * @param size    二维码大小
     * @return
     */
    @Keep
    public static String createQrcode(@NonNull Context context, @NonNull String message, @IntRange(from = 100, to = 4000) int size)
```

#
#### 2.生成带logo二维码图片 - bitmap
```
    /**
     * @param context 上下文
     * @param message 二维码信息
     * @param size    二维码大小
     * @param logo    logo-bitmap
     * @return
     */
    @Keep
    public static String createQrcode(@NonNull Context context, @NonNull String message, @IntRange(from = 100, to = 4000) int size, @Nullable Bitmap logo)
```

#
#### 3.生成带logo二维码图片 - InputStream
```
    /**
     * @param context     上下文
     * @param message     二维码信息
     * @param size        二维码大小
     * @param inputStream logo-inputStream流
     * @return
     */
    @Keep
    public static String createQrcodeFromInputStream(@NonNull Context context, @NonNull String message, @IntRange(from = 100, to = 4000) int size, @NonNull InputStream inputStream)
```

#
#### 4.生成带logo二维码图片 - raw
```
    /**
     * @param context 上下文
     * @param message 二维码信息
     * @param size    二维码大小
     * @param raw     logo-raw文件夹
     * @return
     */
    @Keep
    public static String createQrcodeFromRaw(@NonNull Context context, @NonNull String message, @IntRange(from = 100, to = 4000) int size, @RawRes int raw)
```

#
#### 5.生成带logo二维码图片 - asset
```
    /**
     * @param context 上下文
     * @param message 二维码信息
     * @param size    二维码大小
     * @param assets  logo-assets文件夹
     * @return
     */
    @Keep
    public static String createQrcodeFromAssets(@NonNull Context context, @NonNull String message, @IntRange(from = 100, to = 4000) int size, @NonNull String assets)
```

#
#### 6.生成带logo二维码图片 - base64字符串
```
    /**
     * @param context 上下文
     * @param message 二维码信息
     * @param size    二维码大小
     * @param base64  logo-base64字符串
     * @return
     */
    @Keep
    public static String createQrcodeFromBase64(@NonNull Context context, @NonNull String message, @IntRange(from = 100, to = 4000) int size, @NonNull String base64)
```

#
#### 7.生成带logo二维码图片 - url网络图片
```
    /**
     * @param context 上下文
     * @param message 二维码信息
     * @param size    二维码大小
     * @param url     logo-url网络图片地址
     * @return
     */
    @Keep
    public static String createQrcodeFromUrl(@NonNull Context context, @NonNull String message, @IntRange(from = 100, to = 4000) int size, @NonNull String url)
```