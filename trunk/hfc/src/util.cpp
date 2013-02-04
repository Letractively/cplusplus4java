/*
 * util.cpp
 *
 *  Created on: 2013-1-31
 *      Author: hepeng
 */

#include "util.h"

using namespace cv;
using namespace std;

void alert_win(const CvArr* image){
		cvNamedWindow("alert_win",CV_WINDOW_AUTOSIZE);// Resizable window, might not work on Windows.
	    //cvResizeWindow(windowName,DESIRED_CAMERA_WIDTH,DESIRED_CAMERA_HEIGHT);
	    // Get OpenCV to automatically call my "onMouse()" function when the user clicks in the GUI window.
	    //setMouseCallback(windowName, onMouse, 0);
	    cvShowImage("alert_win",image);
	    //imshow(windowName,mat);
	    cvWaitKey(0);
	    cvDestroyWindow("alert_win");
}


void alert_win(cv::Mat image){
	IplImage temp=image;
	alert_win(&temp);
}
