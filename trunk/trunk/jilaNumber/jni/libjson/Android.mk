LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)


LOCAL_MODULE    := libjson

LOCAL_CPPFLAGS := -DJSON_IS_AMALGAMATION -fexceptions  

LOCAL_SRC_FILES :=\
	json.c \
	
	
LOCAL_LDLIBS := -lz -lstdc++

#include $(BUILD_SHARED_LIBRARY)
include $(BUILD_STATIC_LIBRARY)



