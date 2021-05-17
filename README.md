#
#### demo

![image](https://github.com/153437803/moudle_zxing/blob/master/image20210513162026.gif )
![image](https://github.com/153437803/moudle_zxing/blob/master/image20210513162824.gif )
![image](https://github.com/153437803/moudle_zxing/blob/master/image20210513163052.gif )

#
#### todolist
- [x] 相机预览界面手势相机放大缩小
- [x] 迁移项目依赖到cameraX

#
#### 说明
```
zxing源码
lib_zxing_source_all

zxing源码-仅保留解析二维码
lib_zxing_source_qrcode

zxing-ui-moudle
lib_zxing_ui

cameraX
https://developer.android.google.cn/jetpack/androidx/releases/camera
https://android.googlesource.com/platform/frameworks/support/+/refs/heads/androidx-main/camera/

zxing-3.4.1
https://github.com/zxing/zxing/releases
https://mvnrepository.com/artifact/com.google.zxing/core
https://mvnrepository.com/artifact/com.google.zxing/android-core
```

#
#### 1.生成二维码 => createQrcode
```
    /**
     * @param context      上下文context
     * @param text         二维码信息
     * @param multiple     二维码放大倍数(from = 3, to = 100)
     * @param marginLeft   二维码白边左边距
     * @param marginTop    二维码白边上边距
     * @param marginRight  二维码白边右边距
     * @param marginBottom 二维码白边下边距
     * @param logo         二维码中间logo
     * @return
     */
    @Keep
    public static String createQrcode(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginLeft,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginTop,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginRight,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginBottom,
            @Nullable Bitmap logo)
```

#
#### 2.生成二维码 => createQrcodeFromUrl
```
    /**
     * @param context      上下文context
     * @param text         二维码信息
     * @param multiple     二维码放大倍数(from = 3, to = 100)
     * @param marginLeft   二维码白边左边距
     * @param marginTop    二维码白边上边距
     * @param marginRight  二维码白边右边距
     * @param marginBottom 二维码白边下边距
     * @param url          二维码中间logo
     * @return
     */
    @Keep
    public static String createQrcodeFromUrl(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginLeft,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginTop,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginRight,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginBottom,
            @Nullable String url)
```

#
#### 3.生成二维码 => createQrcodeFromRaw
```
    /**
     * @param context      上下文context
     * @param text         二维码信息
     * @param multiple     二维码放大倍数(from = 3, to = 100)
     * @param marginLeft   二维码白边左边距
     * @param marginTop    二维码白边上边距
     * @param marginRight  二维码白边右边距
     * @param marginBottom 二维码白边下边距
     * @param raw          二维码中间logo
     * @return
     */
    @Keep
    public static String createQrcodeFromRaw(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginLeft,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginTop,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginRight,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginBottom,
            @RawRes int raw)
```

#
#### 4.生成二维码 => createQrcodeFromFile
```
    /**
     * @param context      上下文context
     * @param text         二维码信息
     * @param multiple     二维码放大倍数(from = 3, to = 100)
     * @param marginLeft   二维码白边左边距
     * @param marginTop    二维码白边上边距
     * @param marginRight  二维码白边右边距
     * @param marginBottom 二维码白边下边距
     * @param filePath     二维码中间logo
     * @return
     */
    @Keep
    public static String createQrcodeFromFile(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginLeft,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginTop,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginRight,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginBottom,
            @Nullable String filePath)
```

#
#### 5.生成二维码 => createQrcodeFromAssets
```
    /**
     * @param context      上下文context
     * @param text         二维码信息
     * @param multiple     二维码放大倍数(from = 3, to = 100)
     * @param marginLeft   二维码白边左边距
     * @param marginTop    二维码白边上边距
     * @param marginRight  二维码白边右边距
     * @param marginBottom 二维码白边下边距
     * @param assets       二维码中间logo
     * @return
     */
    @Keep
    public static String createQrcodeFromAssets(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginLeft,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginTop,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginRight,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginBottom,
            @Nullable String assets)
```

#
#### 6.生成二维码 => createQrcodeFromBase64
```
    /**
     * @param context      上下文context
     * @param text         二维码信息
     * @param multiple     二维码放大倍数(from = 3, to = 100)
     * @param marginLeft   二维码白边左边距
     * @param marginTop    二维码白边上边距
     * @param marginRight  二维码白边右边距
     * @param marginBottom 二维码白边下边距
     * @param base64       二维码中间logo
     * @return
     */
    @Keep
    public static String createQrcodeFromBase64(
            @NonNull Context context,
            @NonNull String text,
            @IntRange(from = 3, to = 100) int multiple,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginLeft,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginTop,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginRight,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int marginBottom,
            @Nullable String base64)
```

#
#### 7.解析二维码 => decodeQrcodeFromUri
```
    /**
     * @param context 上下文
     * @param uri     二维码本地文件uri
     * @return
     */
    @Keep
    public static final String decodeQrcodeFromUrl(
            @NonNull Context context,
            @NonNull Uri uri) {

        return DecodeTool.decodeQrcodeFromUri(context, uri);
    }
```

#
#### 8.解析二维码 => decodeQrcodeFromFile
```
    /**
     * @param filePath 二维码本地文件路径
     * @return
     */
    @Keep
    public static final String decodeQrcodeFromFile(@NonNull String filePath) {

        return DecodeTool.decodeQrcodeFromFile(filePath);
    }
```
