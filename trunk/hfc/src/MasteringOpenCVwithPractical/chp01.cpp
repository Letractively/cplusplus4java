/*
 * MasteringOpenCVwithPractical.cpp
 *
 *  Created on: 2013-1-20
 *      Author: Administrator
 */

#include "chp01.h"

#include "cartoon.h"

void test4cartoonifyImage(){
	IplImage* img=cvLoadImage("src/lena.jpg");
	Mat m(img,false);
	Mat displayFrame=Mat(m.size(),CV_8UC3);

			cvNamedWindow("Example1",CV_WINDOW_AUTOSIZE);
			cvShowImage("Example1",img);
			cvWaitKey(0);

			//do..
			int debugType=0;
			bool m_sketchMode=true;
			bool m_alienMode=false;
			bool m_evilMode=false;
			// Run the cartoonifier filter using the selected mode.
			cartoonifyImage(m, displayFrame, m_sketchMode, m_alienMode, m_evilMode, debugType);
			cvNamedWindow("Example2",CV_WINDOW_AUTOSIZE);
			imshow("Example2",displayFrame );
			cvWaitKey(0);

			cvReleaseImage(&img);


			cvDestroyWindow("Example1");
			cvDestroyWindow("Example2");
}




