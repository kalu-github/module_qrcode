cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${WARNING_FLAGS} -std=c++11 -pthread")

######################################################

include_directories(zxing)
include_directories(zxing/src)
include_directories(zxing/zxing/src/aztec)
include_directories(zxing/zxing/src/datamatrix)
include_directories(zxing/zxing/src/maxicode)
include_directories(zxing/zxing/src/oned)
include_directories(zxing/zxing/src/pdf417)
include_directories(zxing/zxing/src/qrcode)
include_directories(zxing/zxing/src/textcodec)

if (NOT DEFINED ENABLE_ENCODERS)
    set (ENABLE_ENCODERS ON)
endif()

if (NOT DEFINED ENABLE_DECODERS)
    set (ENABLE_DECODERS ON)
endif()

set (ZXING_CORE_DEFINES)

if (WINRT)
    set (ZXING_CORE_DEFINES ${ZXING_CORE_DEFINES}
            -DWINRT
            )
endif()

set (ZXING_CORE_LOCAL_DEFINES)
if (MSVC)
    set (ZXING_CORE_LOCAL_DEFINES ${ZXING_CORE_LOCAL_DEFINES}
            -D_SCL_SECURE_NO_WARNINGS
            -D_CRT_SECURE_NO_WARNINGS
            -D_CRT_NONSTDC_NO_WARNINGS
            -DNOMINMAX
            )
endif()

################# Source files

set (COMMON_FILES
        zxing/src/BarcodeFormat.h
        zxing/src/BarcodeFormat.cpp
        zxing/src/BitArray.h
        zxing/src/BitArray.cpp
        zxing/src/BitHacks.h
        zxing/src/BitMatrix.h
        zxing/src/BitMatrix.cpp
        zxing/src/ByteArray.h
        zxing/src/CharacterSet.h
        zxing/src/CharacterSetECI.h
        zxing/src/CharacterSetECI.cpp
        zxing/src/CustomData.h
        zxing/src/GenericGF.h
        zxing/src/GenericGF.cpp
        zxing/src/GenericGFPoly.h
        zxing/src/GenericGFPoly.cpp
        zxing/src/TextUtfEncoding.h
        zxing/src/TextUtfEncoding.cpp
        zxing/src/ZXBigInteger.h
        zxing/src/ZXBigInteger.cpp
        zxing/src/ZXConfig.h
        zxing/src/ZXNullable.h
        zxing/src/ZXNumeric.h
        zxing/src/ZXContainerAlgorithms.h
        zxing/src/ZXStrConvWorkaround.h
        zxing/src/ZXTestSupport.h
        )
if (ENABLE_DECODERS)
    set (COMMON_FILES ${COMMON_FILES}
            zxing/src/BinaryBitmap.h
            zxing/src/BitSource.h
            zxing/src/BitSource.cpp
            zxing/src/DecodeHints.h
            zxing/src/DecodeHints.cpp
            zxing/src/DecodeStatus.h
            zxing/src/DecodeStatus.cpp
            zxing/src/DecoderResult.h
            zxing/src/DetectorResult.h
            zxing/src/GenericLuminanceSource.h
            zxing/src/GenericLuminanceSource.cpp
            zxing/src/GlobalHistogramBinarizer.h
            zxing/src/GlobalHistogramBinarizer.cpp
            zxing/src/GridSampler.h
            zxing/src/GridSampler.cpp
            zxing/src/HybridBinarizer.h
            zxing/src/HybridBinarizer.cpp
            zxing/src/LuminanceSource.h
            zxing/src/LuminanceSource.cpp
            zxing/src/MultiFormatReader.h
            zxing/src/MultiFormatReader.cpp
            zxing/src/PerspectiveTransform.h
            zxing/src/PerspectiveTransform.cpp
            zxing/src/Reader.h
            zxing/src/ReedSolomonDecoder.h
            zxing/src/ReedSolomonDecoder.cpp
            zxing/src/Result.h
            zxing/src/Result.cpp
            zxing/src/ResultMetadata.h
            zxing/src/ResultMetadata.cpp
            zxing/src/ResultPoint.h
            zxing/src/ResultPoint.cpp
            zxing/src/TextDecoder.h
            zxing/src/TextDecoder.cpp
            zxing/src/WhiteRectDetector.h
            zxing/src/WhiteRectDetector.cpp
            )
endif()
if (ENABLE_ENCODERS)
    set (COMMON_FILES ${COMMON_FILES}
            zxing/src/ByteMatrix.h
            zxing/src/ReedSolomonEncoder.h
            zxing/src/ReedSolomonEncoder.cpp
            zxing/src/TextEncoder.h
            zxing/src/TextEncoder.cpp
            zxing/src/MultiFormatWriter.h
            zxing/src/MultiFormatWriter.cpp
            )
endif()


set (AZTEC_FILES
        )
if (ENABLE_DECODERS)
    set (AZTEC_FILES ${AZTEC_FILES}
            zxing/src/aztec/AZDecoder.h
            zxing/src/aztec/AZDecoder.cpp
            zxing/src/aztec/AZDetector.h
            zxing/src/aztec/AZDetector.cpp
            zxing/src/aztec/AZDetectorResult.h
            zxing/src/aztec/AZReader.h
            zxing/src/aztec/AZReader.cpp
            )
endif()
if (ENABLE_ENCODERS)
    set (AZTEC_FILES ${AZTEC_FILES}
            zxing/src/aztec/AZEncodingState.h
            zxing/src/aztec/AZEncoder.h
            zxing/src/aztec/AZEncoder.cpp
            zxing/src/aztec/AZHighLevelEncoder.h
            zxing/src/aztec/AZHighLevelEncoder.cpp
            zxing/src/aztec/AZToken.h
            zxing/src/aztec/AZToken.cpp
            zxing/src/aztec/AZWriter.h
            zxing/src/aztec/AZWriter.cpp
            )
endif()


set (DATAMATRIX_FILES
        )
if (ENABLE_DECODERS)
    set (DATAMATRIX_FILES ${DATAMATRIX_FILES}
            zxing/src/datamatrix/DMBitMatrixParser.h
            zxing/src/datamatrix/DMBitMatrixParser.cpp
            zxing/src/datamatrix/DMECB.h
            zxing/src/datamatrix/DMDataBlock.h
            zxing/src/datamatrix/DMDataBlock.cpp
            zxing/src/datamatrix/DMDecoder.h
            zxing/src/datamatrix/DMDecoder.cpp
            zxing/src/datamatrix/DMDetector.h
            zxing/src/datamatrix/DMDetector.cpp
            zxing/src/datamatrix/DMVersion.h
            zxing/src/datamatrix/DMVersion.cpp
            zxing/src/datamatrix/DMReader.h
            zxing/src/datamatrix/DMReader.cpp
            )
endif()
if (ENABLE_ENCODERS)
    set (DATAMATRIX_FILES ${DATAMATRIX_FILES}
            zxing/src/datamatrix/DMECEncoder.h
            zxing/src/datamatrix/DMECEncoder.cpp
            zxing/src/datamatrix/DMEncoderContext.h
            zxing/src/datamatrix/DMDefaultPlacement.h
            zxing/src/datamatrix/DMDefaultPlacement.cpp
            zxing/src/datamatrix/DMHighLevelEncoder.h
            zxing/src/datamatrix/DMHighLevelEncoder.cpp
            zxing/src/datamatrix/DMSymbolInfo.h
            zxing/src/datamatrix/DMSymbolInfo.cpp
            zxing/src/datamatrix/DMSymbolShape.h
            zxing/src/datamatrix/DMWriter.h
            zxing/src/datamatrix/DMWriter.cpp
            )
endif()


set (MAXICODE_FILES
        )
if (ENABLE_DECODERS)
    set (MAXICODE_FILES ${MAXICODE_FILES}
            zxing/src/maxicode/MCBitMatrixParser.h
            zxing/src/maxicode/MCBitMatrixParser.cpp
            zxing/src/maxicode/MCDecoder.h
            zxing/src/maxicode/MCDecoder.cpp
            zxing/src/maxicode/MCReader.h
            zxing/src/maxicode/MCReader.cpp
            )
endif()


set (ONED_FILES
        zxing/src/oned/ODUPCEANCommon.h
        zxing/src/oned/ODUPCEANCommon.cpp
        zxing/src/oned/ODCode128Patterns.h
        zxing/src/oned/ODCode128Patterns.cpp
        )
if (ENABLE_DECODERS)
    set (ONED_FILES ${ONED_FILES}
            zxing/src/oned/ODCodabarReader.h
            zxing/src/oned/ODCodabarReader.cpp
            zxing/src/oned/ODCode39Reader.h
            zxing/src/oned/ODCode39Reader.cpp
            zxing/src/oned/ODCode93Reader.h
            zxing/src/oned/ODCode93Reader.cpp
            zxing/src/oned/ODCode128Reader.h
            zxing/src/oned/ODCode128Reader.cpp
            zxing/src/oned/ODEAN8Reader.h
            zxing/src/oned/ODEAN8Reader.cpp
            zxing/src/oned/ODEAN13Reader.h
            zxing/src/oned/ODEAN13Reader.cpp
            zxing/src/oned/ODEANManufacturerOrgSupport.h
            zxing/src/oned/ODEANManufacturerOrgSupport.cpp
            zxing/src/oned/ODITFReader.h
            zxing/src/oned/ODITFReader.cpp
            zxing/src/oned/ODMultiUPCEANReader.h
            zxing/src/oned/ODMultiUPCEANReader.cpp
            zxing/src/oned/ODReader.h
            zxing/src/oned/ODReader.cpp
            zxing/src/oned/ODRowReader.h
            zxing/src/oned/ODRowReader.cpp
            zxing/src/oned/ODRSS14Reader.h
            zxing/src/oned/ODRSS14Reader.cpp
            zxing/src/oned/ODRSSExpandedReader.h
            zxing/src/oned/ODRSSExpandedReader.cpp
            zxing/src/oned/ODUPCAReader.h
            zxing/src/oned/ODUPCAReader.cpp
            zxing/src/oned/ODUPCEANExtensionSupport.h
            zxing/src/oned/ODUPCEANExtensionSupport.cpp
            zxing/src/oned/ODUPCEANReader.h
            zxing/src/oned/ODUPCEANReader.cpp
            zxing/src/oned/ODUPCEReader.h
            zxing/src/oned/ODUPCEReader.cpp
            )
endif()
if (ENABLE_ENCODERS)
    set (ONED_FILES ${ONED_FILES}
            zxing/src/oned/ODCodabarWriter.h
            zxing/src/oned/ODCodabarWriter.cpp
            zxing/src/oned/ODCode39Writer.h
            zxing/src/oned/ODCode39Writer.cpp
            zxing/src/oned/ODCode93Writer.h
            zxing/src/oned/ODCode93Writer.cpp
            zxing/src/oned/ODCode128Writer.h
            zxing/src/oned/ODCode128Writer.cpp
            zxing/src/oned/ODEAN8Writer.h
            zxing/src/oned/ODEAN8Writer.cpp
            zxing/src/oned/ODEAN13Writer.h
            zxing/src/oned/ODEAN13Writer.cpp
            zxing/src/oned/ODITFWriter.h
            zxing/src/oned/ODITFWriter.cpp
            zxing/src/oned/ODUPCEWriter.h
            zxing/src/oned/ODUPCEWriter.cpp
            zxing/src/oned/ODUPCAWriter.h
            zxing/src/oned/ODUPCAWriter.cpp
            zxing/src/oned/ODWriterHelper.h
            zxing/src/oned/ODWriterHelper.cpp
            )
endif()


set (ONED_RSS_FILES
        )
if (ENABLE_DECODERS)
    set (ONED_RSS_FILES ${ONED_RSS_FILES}
            zxing/src/oned/rss/ODRSSDataCharacter.h
            zxing/src/oned/rss/ODRSSExpandedBinaryDecoder.h
            zxing/src/oned/rss/ODRSSExpandedBinaryDecoder.cpp
            zxing/src/oned/rss/ODRSSExpandedPair.h
            zxing/src/oned/rss/ODRSSExpandedRow.h
            zxing/src/oned/rss/ODRSSFieldParser.h
            zxing/src/oned/rss/ODRSSFieldParser.cpp
            zxing/src/oned/rss/ODRSSFinderPattern.h
            zxing/src/oned/rss/ODRSSGenericAppIdDecoder.h
            zxing/src/oned/rss/ODRSSGenericAppIdDecoder.cpp
            zxing/src/oned/rss/ODRSSPair.h
            zxing/src/oned/rss/ODRSSReaderHelper.h
            zxing/src/oned/rss/ODRSSReaderHelper.cpp
            )
endif()


set (PDF417_FILES
        )
if (ENABLE_DECODERS)
    set (PDF417_FILES ${PDF417_FILES}
            zxing/src/pdf417/PDFBarcodeMetadata.h
            zxing/src/pdf417/PDFBarcodeValue.h
            zxing/src/pdf417/PDFBarcodeValue.cpp
            zxing/src/pdf417/PDFBoundingBox.h
            zxing/src/pdf417/PDFBoundingBox.cpp
            zxing/src/pdf417/PDFCodeword.h
            zxing/src/pdf417/PDFCodewordDecoder.h
            zxing/src/pdf417/PDFCodewordDecoder.cpp
            zxing/src/pdf417/PDFDecodedBitStreamParser.h
            zxing/src/pdf417/PDFDecodedBitStreamParser.cpp
            zxing/src/pdf417/PDFDecoderResultExtra.h
            zxing/src/pdf417/PDFDetectionResult.h
            zxing/src/pdf417/PDFDetectionResult.cpp
            zxing/src/pdf417/PDFDetectionResultColumn.h
            zxing/src/pdf417/PDFDetectionResultColumn.cpp
            zxing/src/pdf417/PDFDetector.h
            zxing/src/pdf417/PDFDetector.cpp
            zxing/src/pdf417/PDFModulusGF.h
            zxing/src/pdf417/PDFModulusGF.cpp
            zxing/src/pdf417/PDFModulusPoly.h
            zxing/src/pdf417/PDFModulusPoly.cpp
            zxing/src/pdf417/PDFReader.h
            zxing/src/pdf417/PDFReader.cpp
            zxing/src/pdf417/PDFScanningDecoder.h
            zxing/src/pdf417/PDFScanningDecoder.cpp
            )
endif()
if (ENABLE_ENCODERS)
    set (PDF417_FILES ${PDF417_FILES}
            zxing/src/pdf417/PDFCompaction.h
            zxing/src/pdf417/PDFEncoder.h
            zxing/src/pdf417/PDFEncoder.cpp
            zxing/src/pdf417/PDFHighLevelEncoder.h
            zxing/src/pdf417/PDFHighLevelEncoder.cpp
            zxing/src/pdf417/PDFWriter.h
            zxing/src/pdf417/PDFWriter.cpp
            )
endif()


set (QRCODE_FILES
        zxing/src/qrcode/QRCodecMode.h
        zxing/src/qrcode/QRCodecMode.cpp
        zxing/src/qrcode/QRErrorCorrectionLevel.h
        zxing/src/qrcode/QRErrorCorrectionLevel.cpp
        zxing/src/qrcode/QRVersion.h
        zxing/src/qrcode/QRVersion.cpp
        )
if (ENABLE_DECODERS)
    set (QRCODE_FILES ${QRCODE_FILES}
            zxing/src/qrcode/QRAlignmentPattern.h
            zxing/src/qrcode/QRAlignmentPattern.cpp
            zxing/src/qrcode/QRAlignmentPatternFinder.h
            zxing/src/qrcode/QRAlignmentPatternFinder.cpp
            zxing/src/qrcode/QRBitMatrixParser.h
            zxing/src/qrcode/QRBitMatrixParser.cpp
            zxing/src/qrcode/QRDataBlock.h
            zxing/src/qrcode/QRDataBlock.cpp
            zxing/src/qrcode/QRDataMask.h
            zxing/src/qrcode/QRDataMask.cpp
            zxing/src/qrcode/QRDecoder.h
            zxing/src/qrcode/QRDecoder.cpp
            zxing/src/qrcode/QRDecoderMetadata.h
            zxing/src/qrcode/QRDetector.h
            zxing/src/qrcode/QRDetector.cpp
            zxing/src/qrcode/QRECB.h
            zxing/src/qrcode/QRFinderPattern.h
            zxing/src/qrcode/QRFinderPattern.cpp
            zxing/src/qrcode/QRFinderPatternFinder.h
            zxing/src/qrcode/QRFinderPatternFinder.cpp
            zxing/src/qrcode/QRFinderPatternInfo.h
            zxing/src/qrcode/QRFormatInformation.h
            zxing/src/qrcode/QRFormatInformation.cpp
            zxing/src/qrcode/QRReader.h
            zxing/src/qrcode/QRReader.cpp
            )
endif()
if (ENABLE_ENCODERS)
    set (QRCODE_FILES ${QRCODE_FILES}
            zxing/src/qrcode/QREncoder.h
            zxing/src/qrcode/QREncoder.cpp
            zxing/src/qrcode/QREncodeResult.h
            zxing/src/qrcode/QRMaskUtil.h
            zxing/src/qrcode/QRMaskUtil.cpp
            zxing/src/qrcode/QRMatrixUtil.h
            zxing/src/qrcode/QRMatrixUtil.cpp
            zxing/src/qrcode/QRWriter.h
            zxing/src/qrcode/QRWriter.cpp
            )
endif()


set (TEXT_CODEC_FILES
        zxing/src/textcodec/Big5MapTable.h
        zxing/src/textcodec/Big5MapTable.cpp
        zxing/src/textcodec/KRHangulMapping.h
        zxing/src/textcodec/KRHangulMapping.cpp
        )
if (ENABLE_DECODERS)
    set (TEXT_CODEC_FILES ${TEXT_CODEC_FILES}
            zxing/src/textcodec/Big5TextDecoder.h
            zxing/src/textcodec/Big5TextDecoder.cpp
            zxing/src/textcodec/GBTextDecoder.h
            zxing/src/textcodec/GBTextDecoder.cpp
            zxing/src/textcodec/JPTextDecoder.h
            zxing/src/textcodec/JPTextDecoder.cpp
            zxing/src/textcodec/KRTextDecoder.h
            zxing/src/textcodec/KRTextDecoder.cpp
            )
endif()
if (ENABLE_ENCODERS)
    set (TEXT_CODEC_FILES ${TEXT_CODEC_FILES}
            zxing/src/textcodec/Big5TextEncoder.h
            zxing/src/textcodec/Big5TextEncoder.cpp
            zxing/src/textcodec/GBTextEncoder.h
            zxing/src/textcodec/GBTextEncoder.cpp
            zxing/src/textcodec/JPTextEncoder.h
            zxing/src/textcodec/JPTextEncoder.cpp
            zxing/src/textcodec/KRTextEncoder.h
            zxing/src/textcodec/KRTextEncoder.cpp
            )
endif()

source_group (Sources FILES ${COMMON_FILES})
source_group (Sources\\aztec FILES ${AZTEC_FILES})
source_group (Sources\\datamatrix FILES ${DATAMATRIX_FILES})
source_group (Sources\\maxicode FILES ${MAXICODE_FILES})
source_group (Sources\\oned FILES ${ONED_FILES})
source_group (Sources\\oned\\rss FILES ${ONED_RSS_FILES})
source_group (Sources\\pdf417 FILES ${PDF417_FILES})
source_group (Sources\\qrcode FILES ${QRCODE_FILES})
source_group (Sources\\textcodec FILES ${TEXT_CODEC_FILES})

find_package (Threads)

add_library (ZXingCore
        ${COMMON_FILES}
        ${AZTEC_FILES}
        ${DATAMATRIX_FILES}
        ${MAXICODE_FILES}
        ${ONED_FILES}
        ${ONED_RSS_FILES}
        ${PDF417_FILES}
        ${QRCODE_FILES}
        ${TEXT_CODEC_FILES}
        )

target_include_directories (ZXingCore
        PUBLIC "$<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}/src>"
        INTERFACE "$<INSTALL_INTERFACE:${CMAKE_INSTALL_PREFIX}/include>"
        )

target_compile_options (ZXingCore
        PUBLIC ${ZXING_CORE_DEFINES}
        PRIVATE ${ZXING_CORE_LOCAL_DEFINES}
        )

include (CheckCXXCompilerFlag)

CHECK_CXX_COMPILER_FLAG ("-std=c++11" COMPILER_SUPPORTS_CXX11)
if (COMPILER_SUPPORTS_CXX11)
    target_compile_options(ZXingCore PRIVATE
            -std=c++11
            )
endif()

CHECK_CXX_COMPILER_FLAG ("-ffloat-store" COMPILER_NEEDS_FLOAT_STORE)
if (COMPILER_NEEDS_FLOAT_STORE)
    target_compile_options(ZXingCore PRIVATE
            -ffloat-store   # same floating point precision in all optimization levels
            )
endif()

if (APPLE)
    target_compile_options (ZXingCore PRIVATE
            -stdlib=libc++
            )
endif()

target_link_libraries (ZXingCore PUBLIC ${CMAKE_THREAD_LIBS_INIT})

add_library(ZXing::Core ALIAS ZXingCore)
set_target_properties(ZXingCore PROPERTIES EXPORT_NAME Core)

######################################################

file(GLOB CZXING_SOURCE ${CMAKE_SOURCE_DIR}/*.cpp)

add_library(
        czxing
        SHARED
        ${CZXING_SOURCE})

######################################################

find_library(
        log-lib
        log)

target_link_libraries(
        czxing
        ZXingCore

        jnigraphics
        android
        z
        ${log-lib})