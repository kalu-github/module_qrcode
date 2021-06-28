cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${WARNING_FLAGS} -std=c++11 -pthread -Wno-error=deprecated-declarations -Wno-deprecated-declarations -Wsign-compare")
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_EXTENSIONS OFF)

######################################################

include_directories(zxing_1.2.0)
include_directories(zxing_1.2.0/src)
include_directories(zxing_1.2.0/src/aztec)
include_directories(zxing_1.2.0/src/datamatrix)
include_directories(zxing_1.2.0/src/maxicode)
include_directories(zxing_1.2.0/src/oned)
include_directories(zxing_1.2.0/src/pdf417)
include_directories(zxing_1.2.0/src/qrcode)
include_directories(zxing_1.2.0/src/textcodec)

if (NOT DEFINED BUILD_WRITERS)
    set(BUILD_WRITERS ON)
endif ()

if (NOT DEFINED BUILD_READERS)
    set(BUILD_READERS ON)
endif ()

set(ZXING_CORE_DEFINES)
if (WINRT)
    set(ZXING_CORE_DEFINES ${ZXING_CORE_DEFINES}
            -DWINRT
            )
endif ()

set(ZXING_CORE_LOCAL_DEFINES)
if (MSVC)
    set(ZXING_CORE_LOCAL_DEFINES ${ZXING_CORE_LOCAL_DEFINES}
            -D_SCL_SECURE_NO_WARNINGS
            -D_CRT_SECURE_NO_WARNINGS
            -D_CRT_NONSTDC_NO_WARNINGS
            -DNOMINMAX
            )
else ()
    set(ZXING_CORE_LOCAL_DEFINES ${ZXING_CORE_LOCAL_DEFINES}
            -Wall -Wextra -Wno-missing-braces -Werror=undef)
endif ()


################# Source files

set(COMMON_FILES
        zxing_1.2.0/src/BarcodeFormat.h
        zxing_1.2.0/src/BarcodeFormat.cpp
        zxing_1.2.0/src/BitArray.h
        zxing_1.2.0/src/BitArray.cpp
        zxing_1.2.0/src/BitHacks.h
        zxing_1.2.0/src/BitMatrix.h
        zxing_1.2.0/src/BitMatrix.cpp
        zxing_1.2.0/src/BitMatrixCursor.h
        zxing_1.2.0/src/BitMatrixIO.h
        zxing_1.2.0/src/BitMatrixIO.cpp
        zxing_1.2.0/src/ByteArray.h
        zxing_1.2.0/src/ByteMatrix.h
        zxing_1.2.0/src/CharacterSet.h
        zxing_1.2.0/src/CharacterSetECI.h
        zxing_1.2.0/src/CharacterSetECI.cpp
        zxing_1.2.0/src/ConcentricFinder.h
        zxing_1.2.0/src/ConcentricFinder.cpp
        zxing_1.2.0/src/CustomData.h
        zxing_1.2.0/src/Flags.h
        zxing_1.2.0/src/GenericGF.h
        zxing_1.2.0/src/GenericGF.cpp
        zxing_1.2.0/src/GenericGFPoly.h
        zxing_1.2.0/src/GenericGFPoly.cpp
        zxing_1.2.0/src/GTIN.h
        zxing_1.2.0/src/GTIN.cpp
        zxing_1.2.0/src/LogMatrix.h
        zxing_1.2.0/src/Matrix.h
        zxing_1.2.0/src/Pattern.h
        zxing_1.2.0/src/Point.h
        zxing_1.2.0/src/Quadrilateral.h
        zxing_1.2.0/src/RegressionLine.h
        zxing_1.2.0/src/Scope.h
        zxing_1.2.0/src/TextUtfEncoding.h
        zxing_1.2.0/src/TextUtfEncoding.cpp
        zxing_1.2.0/src/TritMatrix.h
        zxing_1.2.0/src/ZXBigInteger.h
        zxing_1.2.0/src/ZXBigInteger.cpp
        zxing_1.2.0/src/ZXConfig.h
        zxing_1.2.0/src/ZXNullable.h
        zxing_1.2.0/src/ZXContainerAlgorithms.h
        zxing_1.2.0/src/ZXTestSupport.h
        )
if (BUILD_READERS)
    set(COMMON_FILES ${COMMON_FILES}
            zxing_1.2.0/src/BinaryBitmap.h
            zxing_1.2.0/src/BinaryBitmap.cpp
            zxing_1.2.0/src/BitSource.h
            zxing_1.2.0/src/BitSource.cpp
            zxing_1.2.0/src/DecodeHints.h
            zxing_1.2.0/src/DecodeHints.cpp
            zxing_1.2.0/src/DecodeStatus.h
            zxing_1.2.0/src/DecodeStatus.cpp
            zxing_1.2.0/src/DecoderResult.h
            zxing_1.2.0/src/DetectorResult.h
            zxing_1.2.0/src/GenericLuminanceSource.h
            zxing_1.2.0/src/GenericLuminanceSource.cpp
            zxing_1.2.0/src/GlobalHistogramBinarizer.h
            zxing_1.2.0/src/GlobalHistogramBinarizer.cpp
            zxing_1.2.0/src/GridSampler.h
            zxing_1.2.0/src/GridSampler.cpp
            zxing_1.2.0/src/HybridBinarizer.h
            zxing_1.2.0/src/HybridBinarizer.cpp
            zxing_1.2.0/src/LuminanceSource.h
            zxing_1.2.0/src/LuminanceSource.cpp
            zxing_1.2.0/src/MultiFormatReader.h
            zxing_1.2.0/src/MultiFormatReader.cpp
            zxing_1.2.0/src/PerspectiveTransform.h
            zxing_1.2.0/src/PerspectiveTransform.cpp
            zxing_1.2.0/src/Reader.h
            zxing_1.2.0/src/ReadBarcode.h
            zxing_1.2.0/src/ReadBarcode.cpp
            zxing_1.2.0/src/ReedSolomonDecoder.h
            zxing_1.2.0/src/ReedSolomonDecoder.cpp
            zxing_1.2.0/src/Result.h
            zxing_1.2.0/src/Result.cpp
            zxing_1.2.0/src/ResultMetadata.h
            zxing_1.2.0/src/ResultMetadata.cpp
            zxing_1.2.0/src/ResultPoint.h
            zxing_1.2.0/src/ResultPoint.cpp
            zxing_1.2.0/src/TextDecoder.h
            zxing_1.2.0/src/TextDecoder.cpp
            zxing_1.2.0/src/ThresholdBinarizer.h
            zxing_1.2.0/src/WhiteRectDetector.h
            zxing_1.2.0/src/WhiteRectDetector.cpp
            )
endif ()
if (BUILD_WRITERS)
    set(COMMON_FILES ${COMMON_FILES}
            zxing_1.2.0/src/ByteMatrix.h
            zxing_1.2.0/src/ReedSolomonEncoder.h
            zxing_1.2.0/src/ReedSolomonEncoder.cpp
            zxing_1.2.0/src/TextEncoder.h
            zxing_1.2.0/src/TextEncoder.cpp
            zxing_1.2.0/src/MultiFormatWriter.h
            zxing_1.2.0/src/MultiFormatWriter.cpp
            )
endif ()


set(AZTEC_FILES
        )
if (BUILD_READERS)
    set(AZTEC_FILES ${AZTEC_FILES}
            zxing_1.2.0/src/aztec/AZDecoder.h
            zxing_1.2.0/src/aztec/AZDecoder.cpp
            zxing_1.2.0/src/aztec/AZDetector.h
            zxing_1.2.0/src/aztec/AZDetector.cpp
            zxing_1.2.0/src/aztec/AZDetectorResult.h
            zxing_1.2.0/src/aztec/AZReader.h
            zxing_1.2.0/src/aztec/AZReader.cpp
            )
endif ()
if (BUILD_WRITERS)
    set(AZTEC_FILES ${AZTEC_FILES}
            zxing_1.2.0/src/aztec/AZEncodingState.h
            zxing_1.2.0/src/aztec/AZEncoder.h
            zxing_1.2.0/src/aztec/AZEncoder.cpp
            zxing_1.2.0/src/aztec/AZHighLevelEncoder.h
            zxing_1.2.0/src/aztec/AZHighLevelEncoder.cpp
            zxing_1.2.0/src/aztec/AZToken.h
            zxing_1.2.0/src/aztec/AZToken.cpp
            zxing_1.2.0/src/aztec/AZWriter.h
            zxing_1.2.0/src/aztec/AZWriter.cpp
            )
endif ()


set(DATAMATRIX_FILES
        zxing_1.2.0/src/datamatrix/DMBitLayout.h
        zxing_1.2.0/src/datamatrix/DMBitLayout.cpp
        )
if (BUILD_READERS)
    set(DATAMATRIX_FILES ${DATAMATRIX_FILES}
            zxing_1.2.0/src/datamatrix/DMDataBlock.h
            zxing_1.2.0/src/datamatrix/DMDataBlock.cpp
            zxing_1.2.0/src/datamatrix/DMDecoder.h
            zxing_1.2.0/src/datamatrix/DMDecoder.cpp
            zxing_1.2.0/src/datamatrix/DMDetector.h
            zxing_1.2.0/src/datamatrix/DMDetector.cpp
            zxing_1.2.0/src/datamatrix/DMVersion.h
            zxing_1.2.0/src/datamatrix/DMVersion.cpp
            zxing_1.2.0/src/datamatrix/DMReader.h
            zxing_1.2.0/src/datamatrix/DMReader.cpp
            )
endif ()
if (BUILD_WRITERS)
    set(DATAMATRIX_FILES ${DATAMATRIX_FILES}
            zxing_1.2.0/src/datamatrix/DMECEncoder.h
            zxing_1.2.0/src/datamatrix/DMECEncoder.cpp
            zxing_1.2.0/src/datamatrix/DMEncoderContext.h
            zxing_1.2.0/src/datamatrix/DMHighLevelEncoder.h
            zxing_1.2.0/src/datamatrix/DMHighLevelEncoder.cpp
            zxing_1.2.0/src/datamatrix/DMSymbolInfo.h
            zxing_1.2.0/src/datamatrix/DMSymbolInfo.cpp
            zxing_1.2.0/src/datamatrix/DMSymbolShape.h
            zxing_1.2.0/src/datamatrix/DMWriter.h
            zxing_1.2.0/src/datamatrix/DMWriter.cpp
            )
endif ()


set(MAXICODE_FILES
        )
if (BUILD_READERS)
    set(MAXICODE_FILES ${MAXICODE_FILES}
            zxing_1.2.0/src/maxicode/MCBitMatrixParser.h
            zxing_1.2.0/src/maxicode/MCBitMatrixParser.cpp
            zxing_1.2.0/src/maxicode/MCDecoder.h
            zxing_1.2.0/src/maxicode/MCDecoder.cpp
            zxing_1.2.0/src/maxicode/MCReader.h
            zxing_1.2.0/src/maxicode/MCReader.cpp
            )
endif ()


set(ONED_FILES
        zxing_1.2.0/src/oned/ODUPCEANCommon.h
        zxing_1.2.0/src/oned/ODUPCEANCommon.cpp
        zxing_1.2.0/src/oned/ODCode128Patterns.h
        zxing_1.2.0/src/oned/ODCode128Patterns.cpp
        )
if (BUILD_READERS)
    set(ONED_FILES ${ONED_FILES}
            zxing_1.2.0/src/oned/ODCodabarReader.h
            zxing_1.2.0/src/oned/ODCodabarReader.cpp
            zxing_1.2.0/src/oned/ODCode39Reader.h
            zxing_1.2.0/src/oned/ODCode39Reader.cpp
            zxing_1.2.0/src/oned/ODCode93Reader.h
            zxing_1.2.0/src/oned/ODCode93Reader.cpp
            zxing_1.2.0/src/oned/ODCode128Reader.h
            zxing_1.2.0/src/oned/ODCode128Reader.cpp
            zxing_1.2.0/src/oned/ODDataBarCommon.h
            zxing_1.2.0/src/oned/ODDataBarCommon.cpp
            zxing_1.2.0/src/oned/ODDataBarReader.h
            zxing_1.2.0/src/oned/ODDataBarReader.cpp
            zxing_1.2.0/src/oned/ODDataBarExpandedReader.h
            zxing_1.2.0/src/oned/ODDataBarExpandedReader.cpp
            zxing_1.2.0/src/oned/ODITFReader.h
            zxing_1.2.0/src/oned/ODITFReader.cpp
            zxing_1.2.0/src/oned/ODMultiUPCEANReader.h
            zxing_1.2.0/src/oned/ODMultiUPCEANReader.cpp
            zxing_1.2.0/src/oned/ODReader.h
            zxing_1.2.0/src/oned/ODReader.cpp
            zxing_1.2.0/src/oned/ODRowReader.h
            zxing_1.2.0/src/oned/ODRowReader.cpp
            )
endif ()
if (BUILD_WRITERS)
    set(ONED_FILES ${ONED_FILES}
            zxing_1.2.0/src/oned/ODCodabarWriter.h
            zxing_1.2.0/src/oned/ODCodabarWriter.cpp
            zxing_1.2.0/src/oned/ODCode39Writer.h
            zxing_1.2.0/src/oned/ODCode39Writer.cpp
            zxing_1.2.0/src/oned/ODCode93Writer.h
            zxing_1.2.0/src/oned/ODCode93Writer.cpp
            zxing_1.2.0/src/oned/ODCode128Writer.h
            zxing_1.2.0/src/oned/ODCode128Writer.cpp
            zxing_1.2.0/src/oned/ODEAN8Writer.h
            zxing_1.2.0/src/oned/ODEAN8Writer.cpp
            zxing_1.2.0/src/oned/ODEAN13Writer.h
            zxing_1.2.0/src/oned/ODEAN13Writer.cpp
            zxing_1.2.0/src/oned/ODITFWriter.h
            zxing_1.2.0/src/oned/ODITFWriter.cpp
            zxing_1.2.0/src/oned/ODUPCEWriter.h
            zxing_1.2.0/src/oned/ODUPCEWriter.cpp
            zxing_1.2.0/src/oned/ODUPCAWriter.h
            zxing_1.2.0/src/oned/ODUPCAWriter.cpp
            zxing_1.2.0/src/oned/ODWriterHelper.h
            zxing_1.2.0/src/oned/ODWriterHelper.cpp
            )
endif ()


set(ONED_RSS_FILES
        )
if (BUILD_READERS)
    set(ONED_RSS_FILES ${ONED_RSS_FILES}
            zxing_1.2.0/src/oned/rss/ODRSSExpandedBinaryDecoder.h
            zxing_1.2.0/src/oned/rss/ODRSSExpandedBinaryDecoder.cpp
            zxing_1.2.0/src/oned/rss/ODRSSFieldParser.h
            zxing_1.2.0/src/oned/rss/ODRSSFieldParser.cpp
            zxing_1.2.0/src/oned/rss/ODRSSGenericAppIdDecoder.h
            zxing_1.2.0/src/oned/rss/ODRSSGenericAppIdDecoder.cpp
            )
endif ()


set(PDF417_FILES
        )
if (BUILD_READERS)
    set(PDF417_FILES ${PDF417_FILES}
            zxing_1.2.0/src/pdf417/PDFBarcodeMetadata.h
            zxing_1.2.0/src/pdf417/PDFBarcodeValue.h
            zxing_1.2.0/src/pdf417/PDFBarcodeValue.cpp
            zxing_1.2.0/src/pdf417/PDFBoundingBox.h
            zxing_1.2.0/src/pdf417/PDFBoundingBox.cpp
            zxing_1.2.0/src/pdf417/PDFCodeword.h
            zxing_1.2.0/src/pdf417/PDFCodewordDecoder.h
            zxing_1.2.0/src/pdf417/PDFCodewordDecoder.cpp
            zxing_1.2.0/src/pdf417/PDFDecodedBitStreamParser.h
            zxing_1.2.0/src/pdf417/PDFDecodedBitStreamParser.cpp
            zxing_1.2.0/src/pdf417/PDFDecoderResultExtra.h
            zxing_1.2.0/src/pdf417/PDFDetectionResult.h
            zxing_1.2.0/src/pdf417/PDFDetectionResult.cpp
            zxing_1.2.0/src/pdf417/PDFDetectionResultColumn.h
            zxing_1.2.0/src/pdf417/PDFDetectionResultColumn.cpp
            zxing_1.2.0/src/pdf417/PDFDetector.h
            zxing_1.2.0/src/pdf417/PDFDetector.cpp
            zxing_1.2.0/src/pdf417/PDFModulusGF.h
            zxing_1.2.0/src/pdf417/PDFModulusGF.cpp
            zxing_1.2.0/src/pdf417/PDFModulusPoly.h
            zxing_1.2.0/src/pdf417/PDFModulusPoly.cpp
            zxing_1.2.0/src/pdf417/PDFReader.h
            zxing_1.2.0/src/pdf417/PDFReader.cpp
            zxing_1.2.0/src/pdf417/PDFScanningDecoder.h
            zxing_1.2.0/src/pdf417/PDFScanningDecoder.cpp
            )
endif ()
if (BUILD_WRITERS)
    set(PDF417_FILES ${PDF417_FILES}
            zxing_1.2.0/src/pdf417/PDFCompaction.h
            zxing_1.2.0/src/pdf417/PDFEncoder.h
            zxing_1.2.0/src/pdf417/PDFEncoder.cpp
            zxing_1.2.0/src/pdf417/PDFHighLevelEncoder.h
            zxing_1.2.0/src/pdf417/PDFHighLevelEncoder.cpp
            zxing_1.2.0/src/pdf417/PDFWriter.h
            zxing_1.2.0/src/pdf417/PDFWriter.cpp
            )
endif ()


set(QRCODE_FILES
        zxing_1.2.0/src/qrcode/QRCodecMode.h
        zxing_1.2.0/src/qrcode/QRCodecMode.cpp
        zxing_1.2.0/src/qrcode/QRErrorCorrectionLevel.h
        zxing_1.2.0/src/qrcode/QRErrorCorrectionLevel.cpp
        zxing_1.2.0/src/qrcode/QRVersion.h
        zxing_1.2.0/src/qrcode/QRVersion.cpp
        )
if (BUILD_READERS)
    set(QRCODE_FILES ${QRCODE_FILES}
            zxing_1.2.0/src/qrcode/QRBitMatrixParser.h
            zxing_1.2.0/src/qrcode/QRBitMatrixParser.cpp
            zxing_1.2.0/src/qrcode/QRDataBlock.h
            zxing_1.2.0/src/qrcode/QRDataBlock.cpp
            zxing_1.2.0/src/qrcode/QRDataMask.h
            zxing_1.2.0/src/qrcode/QRDecoder.h
            zxing_1.2.0/src/qrcode/QRDecoder.cpp
            zxing_1.2.0/src/qrcode/QRDecoderMetadata.h
            zxing_1.2.0/src/qrcode/QRDetector.h
            zxing_1.2.0/src/qrcode/QRDetector.cpp
            zxing_1.2.0/src/qrcode/QRECB.h
            zxing_1.2.0/src/qrcode/QRFormatInformation.h
            zxing_1.2.0/src/qrcode/QRFormatInformation.cpp
            zxing_1.2.0/src/qrcode/QRReader.h
            zxing_1.2.0/src/qrcode/QRReader.cpp
            )
endif ()
if (BUILD_WRITERS)
    set(QRCODE_FILES ${QRCODE_FILES}
            zxing_1.2.0/src/qrcode/QREncoder.h
            zxing_1.2.0/src/qrcode/QREncoder.cpp
            zxing_1.2.0/src/qrcode/QREncodeResult.h
            zxing_1.2.0/src/qrcode/QRMaskUtil.h
            zxing_1.2.0/src/qrcode/QRMaskUtil.cpp
            zxing_1.2.0/src/qrcode/QRMatrixUtil.h
            zxing_1.2.0/src/qrcode/QRMatrixUtil.cpp
            zxing_1.2.0/src/qrcode/QRWriter.h
            zxing_1.2.0/src/qrcode/QRWriter.cpp
            )
endif ()


set(TEXT_CODEC_FILES
        zxing_1.2.0/src/textcodec/Big5MapTable.h
        zxing_1.2.0/src/textcodec/Big5MapTable.cpp
        zxing_1.2.0/src/textcodec/KRHangulMapping.h
        zxing_1.2.0/src/textcodec/KRHangulMapping.cpp
        )
if (BUILD_READERS)
    set(TEXT_CODEC_FILES ${TEXT_CODEC_FILES}
            zxing_1.2.0/src/textcodec/Big5TextDecoder.h
            zxing_1.2.0/src/textcodec/Big5TextDecoder.cpp
            zxing_1.2.0/src/textcodec/GBTextDecoder.h
            zxing_1.2.0/src/textcodec/GBTextDecoder.cpp
            zxing_1.2.0/src/textcodec/JPTextDecoder.h
            zxing_1.2.0/src/textcodec/JPTextDecoder.cpp
            zxing_1.2.0/src/textcodec/KRTextDecoder.h
            zxing_1.2.0/src/textcodec/KRTextDecoder.cpp
            )
endif ()
if (BUILD_WRITERS)
    set(TEXT_CODEC_FILES ${TEXT_CODEC_FILES}
            zxing_1.2.0/src/textcodec/Big5TextEncoder.h
            zxing_1.2.0/src/textcodec/Big5TextEncoder.cpp
            zxing_1.2.0/src/textcodec/GBTextEncoder.h
            zxing_1.2.0/src/textcodec/GBTextEncoder.cpp
            zxing_1.2.0/src/textcodec/JPTextEncoder.h
            zxing_1.2.0/src/textcodec/JPTextEncoder.cpp
            zxing_1.2.0/src/textcodec/KRTextEncoder.h
            zxing_1.2.0/src/textcodec/KRTextEncoder.cpp
            )
endif ()

source_group(Sources FILES ${COMMON_FILES})
source_group(Sources\\aztec FILES ${AZTEC_FILES})
source_group(Sources\\datamatrix FILES ${DATAMATRIX_FILES})
source_group(Sources\\maxicode FILES ${MAXICODE_FILES})
source_group(Sources\\oned FILES ${ONED_FILES})
source_group(Sources\\oned\\rss FILES ${ONED_RSS_FILES})
source_group(Sources\\pdf417 FILES ${PDF417_FILES})
source_group(Sources\\qrcode FILES ${QRCODE_FILES})
source_group(Sources\\textcodec FILES ${TEXT_CODEC_FILES})

set(CMAKE_THREAD_PREFER_PTHREAD TRUE)
set(THREADS_PREFER_PTHREAD_FLAG TRUE)
find_package(Threads REQUIRED)

add_library(ZXing
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

target_include_directories(ZXing
        PUBLIC "$<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}/src>"
        INTERFACE "$<INSTALL_INTERFACE:include>"
        )

target_compile_options(ZXing
        PUBLIC ${ZXING_CORE_DEFINES}
        PRIVATE ${ZXING_CORE_LOCAL_DEFINES}
        )

include(CheckCXXCompilerFlag)

CHECK_CXX_COMPILER_FLAG("-ffloat-store" COMPILER_NEEDS_FLOAT_STORE)
if (COMPILER_NEEDS_FLOAT_STORE)
    target_compile_options(ZXing PRIVATE
            -ffloat-store   # same floating point precision in all optimization levels
            )
endif ()

# the lib needs a c++-17 compiler but can be used with a c++-11 compiler (see examples)
target_compile_features(ZXing PRIVATE cxx_std_17 INTERFACE cxx_std_11)

target_link_libraries(ZXing PRIVATE Threads::Threads)

add_library(ZXing::ZXing ALIAS ZXing)
# add the old alias as well, to keep old clients compiling
# note: this only affects client code that includes ZXing via sub_directory.
#       for clients using the exported target, see ZXingConfig.cmake.in
add_library(ZXing::Core ALIAS ZXing)

set_target_properties(ZXing PROPERTIES EXPORT_NAME ZXing)
# force position indepent code to be able to link it as static lib into a DLL (e.g. the python module)
set_target_properties(ZXing PROPERTIES POSITION_INDEPENDENT_CODE ON)
if (PROJECT_VERSION)
    set_target_properties(ZXing PROPERTIES VERSION ${PROJECT_VERSION})
    set_target_properties(ZXing PROPERTIES SOVERSION ${PROJECT_VERSION_MAJOR})
endif ()

include(GNUInstallDirs)

install(
        TARGETS ZXing EXPORT ZXingTargets
        LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR}
        RUNTIME DESTINATION ${CMAKE_INSTALL_BINDIR}
        ARCHIVE DESTINATION ${CMAKE_INSTALL_LIBDIR}
        INCLUDES DESTINATION include
)

if (MSVC)
    set_target_properties(ZXing PROPERTIES
            COMPILE_PDB_NAME ZXing
            COMPILE_PDB_OUTPUT_DIR ${CMAKE_CURRENT_BINARY_DIR})
    install(FILES ${CMAKE_CURRENT_BINARY_DIR}/ZXing.pdb
            DESTINATION ${CMAKE_INSTALL_LIBDIR}
            CONFIGURATIONS Debug RelWithDebInfo)
endif ()

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
        ZXing

        jnigraphics
        android
        z
        ${log-lib})