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
#include <stdio.h>

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

void assetCopy2SDCard(const char* sourceFile,const char* sourceFilename){


	char targetFilename[255];
	sprintf(targetFilename,"/data/data/com.jila.number/files/OCR/%s",sourceFilename );
	packt::Log::info(targetFilename);

	const int BUFSIZE=100;
	char buf[BUFSIZE];
	int nb_read=0;
	FILE* targetFile=fopen(targetFilename,"w+");
	AAsset* one=AAssetManager_open(g_am,sourceFile,AASSET_MODE_RANDOM);
	while( (nb_read=AAsset_read(one,buf,BUFSIZE))>0 ){
		fwrite(buf,nb_read,1,targetFile);
	}
	fflush(targetFile);
	fclose(targetFile);

	AAsset_close(one);


}
void assetRemove2SDCard(const char* sourceFile,const char* sourceFilename){


	char targetFilename[255];
	sprintf(targetFilename,"/data/data/com.jila.number/files/%s",sourceFilename );
	packt::Log::info(targetFilename);
	remove( targetFilename );


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

	/*  just one file
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
	AAsset_close(readme_txt);
	*/

	/* all files under Root dir
	AAssetDir* ar=AAssetManager_openDir(g_am,"");
	const char* filename=(const char*)NULL;

	while( (filename=AAssetDir_getNextFileName(ar))!=NULL ){
		packt::Log::info(filename);
		AAsset* one=AAssetManager_open(g_am,filename,AASSET_MODE_STREAMING);

		char targetFilename[255];
		sprintf(targetFilename,"/sdcard/%s",filename );
		packt::Log::info(targetFilename);

		const int BUFSIZE=100;
		char buf[BUFSIZE];
		int nb_read=0;
		FILE* targetFile=fopen(targetFilename,"w+");
		while( (nb_read=AAsset_read(one,buf,BUFSIZE))>0 ){
			fwrite(buf,nb_read,1,targetFile);
		}
		fflush(targetFile);
		fclose(targetFile);

		AAsset_close(one);
		packt::Log::info("end...");
	}
	AAssetDir_close(ar);
*/

/* recursively all file under OCR  */
		char file_path[255];
		sprintf(file_path , "OCR/");
		char sourceFile[255];
		char sourceFilename[255];
		int i,j;
		int classes=10;
		int train_samples=50;
		for(i =0; i<classes; i++){
			for( j = 0; j< train_samples; j++){
				//Load file
				if(j<10){
					sprintf(sourceFilename,"%d0%d.pbm", i , j);
					sprintf(sourceFile,"%s%d/%d0%d.pbm",file_path, i, i , j);
				}else{
					sprintf(sourceFilename,"%d%d.pbm", i , j);
					sprintf(sourceFile,"%s%d/%d%d.pbm",file_path, i, i , j);
				}
				packt::Log::info( sourceFile );
				//deal with every file
				assetCopy2SDCard(sourceFile,sourceFilename);
				//assetRemove2SDCard(sourceFile,sourceFilename);
				packt::Log::info("assetRemove2SDCard one  end...");
			}
		}

		//assetRemove2SDCard("","ocr");
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



