/*
 * com_jila_number_Dylib.cpp
 *
 *  Created on: 2013-2-18
 *      Author: hepeng
 */

#include "com_jila_number_Dylib.h"
#include "../libjson/json.h"
#include "Log.hpp"


/*/////////////////////////////////////////////////////////////////////
 *util method begin
 /////////////////////////////////////////////////////////////////////*/
//��const char����ת����jstring����
jstring CStr2Jstring( JNIEnv* env, const char* pat )
{
 //����java String�� strClass
 jclass strClass = (env)->FindClass("Ljava/lang/String;");
 //��ȡjava String�෽��String(byte[],String)�Ĺ�����,���ڽ�����byte[]����ת��Ϊһ����String
 jmethodID ctorID = (env)->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
 //����byte����
 jbyteArray bytes = (env)->NewByteArray((jsize)strlen(pat));
 //��char* ת��Ϊbyte����
 (env)->SetByteArrayRegion(bytes, 0, (jsize)strlen(pat), (jbyte*)pat);
 //����String, ������������,����byte����ת����Stringʱ�Ĳ���
 jstring encoding = (env)->NewStringUTF("GB2312");
 //��byte����ת��Ϊjava String,�����
 return (jstring)(env)->NewObject(strClass, ctorID, bytes, encoding);

}

char*   Jstring2CStr(JNIEnv*   env,   jstring   jstr)
{
 char*   rtn   =   NULL;
 jclass   clsstring   =   env->FindClass("java/lang/String");
 jstring   strencode   =   env->NewStringUTF("GB2312");
 jmethodID   mid   =   env->GetMethodID(clsstring,   "getBytes",   "(Ljava/lang/String;)[B");
 jbyteArray   barr=   (jbyteArray)env->CallObjectMethod(jstr,mid,strencode);
 jsize   alen   =   env->GetArrayLength(barr);
 jbyte*   ba   =   env->GetByteArrayElements(barr,JNI_FALSE);
 if(alen   >   0)
 {
  rtn   =   (char*)malloc(alen+1);         //new   char[alen+1];
  memcpy(rtn,ba,alen);
  rtn[alen]=0;
 }
 env->ReleaseByteArrayElements(barr,ba,0);

 return rtn;
}
/*/////////////////////////////////////////////////////////////////////
 *util method end
 /////////////////////////////////////////////////////////////////////*/



/*
 * Class:     com_jila_number_Dylib
 * Method:    initializeDylib
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_jila_number_Dylib_initializeDylib
  (JNIEnv *env, jobject, jstring paramstr){

	char* chstr= Jstring2CStr( env,paramstr );


	//packt::Log::info( *chstr );

	free(chstr);
}

/*
 * Class:     com_jila_number_Dylib
 * Method:    finalizeDylib
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_jila_number_Dylib_finalizeDylib
  (JNIEnv *env, jobject, jstring){

}



