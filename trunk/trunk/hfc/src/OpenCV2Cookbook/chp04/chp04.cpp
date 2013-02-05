/*
 * chp03.cpp
 *
 *  Created on: 2013-2-4
 *      Author: hepeng
 */

#include "chp04.h"


#include "histogram.h"

#include <iostream>
using namespace std;
 const char* inputImagePath4case1histogram="src/OpenCV2Cookbook/images/group.jpg";

void case1histogram(){
	cv::Mat image=cv::imread( inputImagePath4case1histogram,cv::IMREAD_GRAYSCALE );//open in b&w
	Histogram1D h;
	cv::MatND histo=h.getHistogram(image);

	for(int i=0;i<256;i++){
		cout<<"Value "<< i << " = " << histo.at<float>( i )<<endl;
	}
}

void case2histogram(){
	cv::Mat image=cv::imread( inputImagePath4case1histogram,cv::IMREAD_GRAYSCALE );//open in b&w
	alert_win( image );

	Histogram1D h;
	cv::Mat re= h.getHistogramImage(image);
	alert_win( re );

}


void case3histogram(){
	cv::Mat image=cv::imread( inputImagePath4case1histogram,cv::IMREAD_GRAYSCALE );//open in b&w
	alert_win( image );

	cv::Mat threshold;
	cv::threshold(image,threshold,60,255,cv::THRESH_BINARY );
	alert_win( threshold );

}


void case4histogram(){
	cv::Mat image=cv::imread( inputImagePath4case1histogram,cv::IMREAD_GRAYSCALE );
	alert_win( image );

	Histogram1D h;
	cv::Mat eq=h.equalize( image );
	alert_win( eq );

}



