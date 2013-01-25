/*
 * opencv_tutorials.cpp
 *
 *  Created on: 2013-1-18
 *      Author: hepeng
 */

#include "opencv_tutorials.h"

void chp02exam01(){
	//Mat m= imread("src/lena.jpg");
	//Mat m( 4,2,CV_8UC3,Scalar(0,0,255) );
	//Mat m( 5,3,CV_8UC3,Scalar::all(255) );
	//Mat m(30,40,DataType<float>::type );
	Mat m=cvCreateMat(2,4,CV_8SC2);
	cout<<"M="<<endl<<"  "<<m<<endl<<endl;
	cout<< m.depth()<<","<<m.channels()<<endl;
}


