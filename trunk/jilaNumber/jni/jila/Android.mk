#/****************************************************************************
#*   number jila for Android.
#*****************************************************************************
#*   jila 
#*****************************************************************************


LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)


#*****************************************************************************
#*   opencv 
#*****************************************************************************

OPENCV_LIB_TYPE:=STATIC
OPENCV_INSTALL_MODULES:=on

# Path to OpenCV.mk file, which is generated when you build OpenCV for Android.
# include C:\OpenCV\android\build\OpenCV.mk
# include ~/OpenCV/android/build/OpenCV.mk


include ../sdk/native/jni/OpenCV.mk
#ifeq ("$(wildcard $(OPENCV_MK_PATH))","")
    #try to load OpenCV.mk from default install location
#    include $(TOOLCHAIN_PREBUILT_ROOT)/user/share/OpenCV/OpenCV.mk
#else
#    include $(OPENCV_MK_PATH)
#endif


#*****************************************************************************
#*   json 
#*****************************************************************************
#LOCAL_PATH := $(call my-dir)  
#subdirs :=$(LOCAL_PATH)/jsoncpp/Android.mk
#include $(subdirs)

#*****************************************************************************
#*   local 
#*****************************************************************************
LOCAL_MODULE    := jilanumber

LOCAL_C_INCLUDES += $(LOCAL_PATH)/../libjson/ 
LOCAL_STATIC_LIBRARIES += libjson

LOCAL_LDLIBS +=  -llog -ldl -lstdc++

# Since we have source + headers files in an external folder, we need to show where they are.
LOCAL_SRC_FILES := com_jila_number_Dylib.cpp
LOCAL_SRC_FILES += Log.cpp
#LOCAL_SRC_FILES += ImageUtils_0.7.cpp


#LOCAL_C_INCLUDES += $(LOCAL_PATH)/../../Cartoonifier_Desktop
LOCAL_C_INCLUDES += $(LOCAL_PATH)

include $(BUILD_SHARED_LIBRARY)

