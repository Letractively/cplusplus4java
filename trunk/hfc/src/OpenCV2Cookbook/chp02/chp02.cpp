/*
 * chp02.cpp
 *
 *  Created on: 2013-2-2
 *      Author: hepeng
 */

#include "chp02.h"
#include "../../util.h"

const char* inputImagePath="src/OpenCV2Cookbook/images/church01.jpg";
const int addColor=255;

void salt(cv::Mat* image,int n){
	int i,j;
	for(int k=0;k<n;k++  ){
		i=rand()%image->cols;
		j=rand()%image->rows;

		if(image->channels()==1){
				image->at<uchar>(j,i)=addColor;
			}else if( image->channels()==3 ){
				image->at<cv::Vec3b>(j,i)[0]=addColor;
				image->at<cv::Vec3b>(j,i)[1]=addColor;
				image->at<cv::Vec3b>(j,i)[2]=addColor;
			}

	}

}

void test4saltImage(int argc,char** argv){
	srand( cv::getTickCount() );
	cv::Mat image=cv::imread( inputImagePath );
	salt(&image,3000);
	IplImage temp4image=image;
	alert_win( &temp4image );
}
/*
 ///////////////////////////////////////////////////////////////////////////
 */

//solution0
//  using  .ptr and []
void colorReduce0( cv::Mat& image,int div=64 ){
	int nl=image.rows;
	int nc=image.cols*image.channels();

	for(int j=0;j<nl;j++){
		uchar* data=image.ptr<uchar>(j);
		for(int i=0;i<nc;i++){
			data[i]=data[i]/div*div+div/2;
		}
	}
}

//solution1
//using .ptr and *++
void colorReduce1(cv::Mat& image,int div=64){
	int nl=image.rows;
	int nc=image.cols*image.channels();

	for(int j=0;j<nl;j++){
		uchar* data=image.ptr<uchar>(j);
		for(int i=0;i<nc;i++){
			*data=*data/div*div+div/2;
			data++;
		}
	}

}

//solution2
//using .ptr and *++ and modulo
void colorReduce2(cv::Mat& image,int div=64){
	int nl=image.rows;
		int nc=image.cols*image.channels();

		for(int j=0;j<nl;j++){
			uchar* data=image.ptr<uchar>(j);
			for(int i=0;i<nc;i++){
				int v=*data;
				*data=v-v%div+div/2;
				data++;
			}
		}
}

//solution3
//using .ptr and *++ and modulo
void colorReduce3(cv::Mat& image,int div=64){
		int nl=image.rows;
		int nc=image.cols*image.channels();

		int n=static_cast<int>(  log(static_cast<double>(div))/log(2.0)  );
		uchar mask=0xFF<<n;

		for(int j=0;j<nl;j++){
			uchar* data=image.ptr<uchar>(j);
			for(int i=0;i<nc;i++){
				*data= ((*data)&mask) + div/2;
				data++;
			}
		}
}


 int test4colorReduce(int argc,char** argv){
	 cv::Mat image=cv::imread( inputImagePath );
	 //show the original
	 IplImage ori4image=image;
	 alert_win( &ori4image );

	 colorReduce3(image);
	 //then show the result
	 IplImage rs4image=image;
	 alert_win( &rs4image );
  	return 0;
  }

 /*
  ///////////////////////////////////////////////////////////////////////////
  */
const char* inputImagePath4sharpenContrast="src/OpenCV2Cookbook/images/boldt.jpg";

void sharpen(const cv::Mat& image,cv::Mat& result){
	for(int j=1;j<image.rows-1;j++){
		const uchar* previous=image.ptr<const uchar>(j-1);
		const uchar* current=image.ptr<const uchar>(j);
		const uchar* next=image.ptr<const uchar>(j+1);
		      uchar* current4target=result.ptr<uchar>(j);
		for(int i=1;i<image.cols-1;i++){
			*current4target=cv::saturate_cast<uchar>( 5*current[i]-previous[i]-next[i]-current[j-1]-current[j+1] );
			current4target++;
		}
	}
	//first and last row and column
	result.row(0).setTo( cv::Scalar(0) );
	result.row( result.rows-1 ).setTo( cv::Scalar(0) );
	result.col(0).setTo( cv::Scalar(0) );
	result.col( result.cols-1 ).setTo( cv::Scalar(0) );
}

void sharpen4template(const cv::Mat& image,cv::Mat& result){
	cv::Mat_<uchar>::const_iterator it=image.begin<uchar>()+image.step;
	cv::Mat_<uchar>::const_iterator itend=image.end<uchar>()-image.step;

	cv::Mat_<uchar>::const_iterator itup=image.begin<uchar>();
	cv::Mat_<uchar>::const_iterator itdown=image.begin<uchar>()+2*image.step;

	cv::Mat_<uchar>::iterator itout=result.begin<uchar>()+result.step;
	for(;it!=itend;++it,++itup,++itdown){
		*itout=cv::saturate_cast<uchar>( *it*5-*(it-1)-*(it+1)-*itup-*itdown );
	}
}

void sharpen4tfilter2D(const cv::Mat& image,cv::Mat& result){
	cv::Mat kernal(3,3,CV_32F,cv::Scalar(0));
	kernal.at<float>(1,1)=5.0;
	kernal.at<float>(0,1)=-1.0;
	kernal.at<float>(1,0)=-1.0;
	kernal.at<float>(1,2)=-1.0;
	kernal.at<float>(2,1)=-1.0;

	cv::filter2D(image,result,image.depth(),kernal);
}

 int test4sharpenContrast(){
	 cv::Mat image=cv::imread( inputImagePath4sharpenContrast );
	 cv::Mat result;
	 result.create( image.size() ,image.type() );

	 	 //show the original
	 	 IplImage ori4image=image;
	 	 alert_win( &ori4image );


	 	 //sharpen(image,result);
	 	 //sharpen4template(image,result);
	 	sharpen4tfilter2D(image,result);
	 	 //then show the result
	 	 IplImage rs4image=image;
	 	 alert_win( &rs4image );
	   	return 0;
 }


 /*
  ///////////////////////////////////////////////////////////////////////////
  */
 const char* inputImagePath4addLogoImage1="src/OpenCV2Cookbook/images/boldt.jpg";
 const char* inputImagePath4addLogoImage2="src/OpenCV2Cookbook/images/rain.jpg";
 const char* inputImagePath4addLogoImage3="src/OpenCV2Cookbook/images/logo.bmp";

void test4addLogoImageCase1(){
		 cv::Mat image1;
		 cv::Mat image2;
		 image1=cv::imread(inputImagePath4addLogoImage1);
		 image2=cv::imread(inputImagePath4addLogoImage2);

		 alert_win(image1);
		 alert_win(image2);

		 //case1
		 cv::Mat result;
		 cv::addWeighted(image1,0.7,image2,0.9,0.,result);
		 alert_win(result);
}
void test4addLogoImageCase2(){
		 cv::Mat image1;
		 cv::Mat image2;
		 image1=cv::imread(inputImagePath4addLogoImage1);
		 image2=cv::imread(inputImagePath4addLogoImage2);

		 alert_win(image1);
		 alert_win(image2);

		 //case2
		 //Result on blue channel
		 //create vector of 3 images
		 std::vector<cv::Mat> planes;
		 cv::split(image1,planes);
		 //planes[0]=planes[0]+image2;
		 planes[0]+= image2;

		 cv::Mat result;
		 result.create( image1.size(),image1.type() );
		 cv::merge(planes,result);
		 alert_win(result);
}

void test4addLogoImageCase3(){
	cv::Mat image=cv::imread( inputImagePath4addLogoImage1 );
	cv::Mat logo=cv::imread( inputImagePath4addLogoImage3 );

	//define image ROI
	cv::Mat imageROI;
	imageROI=image( cv::Rect(385,270,logo.cols,logo.rows) );

	//add logo to image
	cv::addWeighted( imageROI,1.0,logo,0.3,0.,imageROI );
	alert_win( image );

}
void test4addLogoImageCase4(){
	cv::Mat image=cv::imread( inputImagePath4addLogoImage1 );
	cv::Mat logo=cv::imread( inputImagePath4addLogoImage3 );

	//define image ROI
	cv::Mat imageROI;
	imageROI=image( cv::Rect(385,270,logo.cols,logo.rows) );

	//load the mask(must be gray-level)
	cv::Mat mask=cv::imread( inputImagePath4addLogoImage3 );
	//copy to ROI with mask
	logo.copyTo( imageROI,mask );

	alert_win( image );
}

void test4addLogoImageCase5(){
	cv::Mat image1=cv::imread( inputImagePath4addLogoImage1 );
	cv::Mat logo=cv::imread( inputImagePath4addLogoImage3 );

	//define image ROI
	cv::Mat imageROI;

	//split 3-channel image into 3 1-channel images
	std::vector<cv::Mat> channels;
	cv::split( image1,channels );

	imageROI=channels.at(1);

	cv::addWeighted( imageROI(cv::Rect(385,270,logo.cols,logo.rows)),1.0,
			logo,0.5,0.,imageROI(cv::Rect(385,270,logo.cols,logo.rows )) );



	cv::merge(channels,image1);

	alert_win( image1 );
}

 int test4addLogoImage(){

	 test4addLogoImageCase4();

	 return 0;
 }


