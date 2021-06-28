cmake_minimum_required(VERSION 3.10.2)

# 编译Release(Release Debug)
#set(CMAKE_BUILD_TYPE "Release")

# 编译警告
add_definitions(-Wno-format-security)
add_definitions(-Wno-multichar)
add_definitions(-D_ANDROID)
add_definitions(-DLIBDIR="c")
add_definitions(-DBUILDING_LIBICONV)
add_definitions(-DBUILDING_LIBCHARSET)
add_definitions(-DIN_LIBRARY)

# 源文件
set(ICONV_SRCS
        libiconv/lib/iconv.c
        libiconv/libcharset/lib/localcharset.c
        libiconv/lib/relocatable.c)

# 源文件
set(ZBAR_SRCS
        zbarjni.c
        zbar/img_scanner.c
        zbar/decoder.c
        zbar/image.c
        zbar/symbol.c
        zbar/convert.c
        zbar/config.c
        zbar/scanner.c
        zbar/error.c
        zbar/refcnt.c
        zbar/video.c
        zbar/video/null.c
        zbar/decoder/qr_finder.c
        zbar/qrcode/bch15_5.c
        zbar/qrcode/binarize.c
        zbar/qrcode/isaac.c
        zbar/qrcode/qrdec.c
        zbar/qrcode/qrdectxt.c
        zbar/qrcode/rs.c
        zbar/qrcode/util.c)

# 头文件
include_directories("./")
include_directories("libiconv/include")
include_directories("libiconv/libcharset")
include_directories("libiconv/libcharset/include")
include_directories("zbar")
include_directories("zbar/include")
include_directories("zbar/include/zbar")

# 链接
add_library(
        zbar
        SHARED
        ${ZBAR_SRCS}
        ${ICONV_SRCS})

# so
target_link_libraries(zbar)