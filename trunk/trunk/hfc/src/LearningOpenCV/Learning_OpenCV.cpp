/*
 * Learning_OpenCV.cpp
 *
 *  Created on: 2013-1-18
 *      Author: hepeng
 */

#include "Learning_OpenCV.h"
using namespace cv;
using namespace std;


void chp02exam1(){
	//Mat im = imread(argc == 2 ? argv[1] : "src/lena.jpg", 1);
		IplImage* img=cvLoadImage("src/lena.jpg");
		cvNamedWindow("Example1",CV_WINDOW_AUTOSIZE);
		cvShowImage("Example1",img);

		cvWaitKey(0);

		cvReleaseImage(&img);
		cvDestroyWindow("Example1");
}

void chp03exam3(){
	// Create an OpenCV Matrix containing some fixed data.
	  //
	  float vals[] = { 0.866025, -0.500000, 0.500000, 0.866025};

	  CvMat rotmat;

	  cvInitMatHeader(
	    &rotmat,
	    2,
	    2,
	    CV_32FC2,
	    vals
	  );
	  Mat m=Mat(&rotmat, false);
	  cout<<"M="<<endl<<"  "<<m<<endl<<endl;
	  printf("Ex 3_3 matrix initialized\n");
}

