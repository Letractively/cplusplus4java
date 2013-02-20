/*
 * com_jila_number_Dylib.cpp
 *
 *  Created on: 2013-2-18
 *      Author: hepeng
 */

#include "com_jila_number_Dylib.h"
#include "../libjson/json.h"
#include "Log.hpp"
#include <string.h>


#include <android/asset_manager_jni.h>

/*/////////////////////////////////////////////////////////////////////
 *util method begin
 /////////////////////////////////////////////////////////////////////*/
static const char* encode="GB2312";
//将const char类型转换成jstring类型【this method cause error】
jstring CStr2Jstring( JNIEnv* env, const char* pat )
{
 //定义java String类 strClass
 jclass strClass = (env)->FindClass("Ljava/lang/String;");
 //获取java String类方法String(byte[],String)的构造器,用于将本地byte[]数组转换为一个新String
 jmethodID ctorID = (env)->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
 //建立byte数组
 jbyteArray bytes = (env)->NewByteArray((jsize)strlen(pat));
 //将char* 转换为byte数组
 (env)->SetByteArrayRegion(bytes, 0, (jsize)strlen(pat), (jbyte*)pat);
 //设置String, 保存语言类型,用于byte数组转换至String时的参数
 jstring encoding = (env)->NewStringUTF( encode );
 //将byte数组转换为java String,并输出
 return (jstring)(env)->NewObject(strClass, ctorID, bytes, encoding);

}

char*   Jstring2CStr(JNIEnv*   env,   jstring   jstr,int* charLen)
{
 char*   rtn   =   NULL;
 jclass   clsstring   =   env->FindClass("java/lang/String");
 jstring   strencode   =   env->NewStringUTF( encode );

 jmethodID   mid   =   env->GetMethodID(clsstring,   "getBytes",   "(Ljava/lang/String;)[B");
 jbyteArray   barr=   (jbyteArray)env->CallObjectMethod(jstr,mid,strencode);
 jsize   alen   =   env->GetArrayLength(barr);*charLen=alen+1;
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
 * Signature: (Ljava/lang/String;)Ljava/lag/String;
 */
JNIEXPORT jstring JNICALL Java_com_jila_number_Dylib_initializeDylib
  (JNIEnv *env, jobject, jstring paramstr,jobject param4am)
{
	const char* char4re="kankan";
	//jstring re=CStr2Jstring(env,char4re);//this method cause error
	jstring re=env->NewStringUTF(char4re);
	//part1:  parse param
	int chstrLen;
	char* chstr= Jstring2CStr( env,paramstr,&chstrLen );
	packt::Log::info( chstr );

	json_settings settings;
	memset( &settings,0,sizeof(json_settings) );
	char error[256];
	json_value* data= json_parse_ex(&settings, chstr,error );
	if( data!=0 ){
		switch(data->type){
		case json_object:
				for(int i=0;i<data->u.object.length;i++){
					packt::Log::info( data->u.object.values[i].name );
					_json_value* valueLevel1=data->u.object.values[i].value;
					packt::Log::info( valueLevel1->u.string.ptr );
				}
			break;
		default:
			break;
		}

	}else{//error parse json

	}

	//packt::Log::info(jsonValue->u.object.values[0].mailHost );

	json_value_free(data);
	free(chstr);


	//part 2 :parse assets directory
	g_am=AAssetManager_fromJava(env,param4am);
	if( g_am==NULL ){
		packt::Log::error("AAssetManager_fromJava return null ");
		return re;
	}

	const char* filename="readme.txt";
	AAsset* readme_txt=AAssetManager_open(g_am,filename,AASSET_MODE_UNKNOWN );
	if( readme_txt==NULL ){
		packt::Log::error("AAssetManager_open return null");
		return re;
	}
	off_t buffersize=AAsset_getLength( readme_txt );
	char* buffer=(char*)malloc( buffersize+1 );
	buffer[buffersize]=0;
	int numBytesRead=AAsset_read( readme_txt,buffer,buffersize );
	packt::Log::info( buffer );
	free(buffer);
	/*  */
	return re;
}

/*
 * Class:     com_jila_number_Dylib
 * Method:    finalizeDylib
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_jila_number_Dylib_finalizeDylib
  (JNIEnv *env, jobject, jstring){

}



