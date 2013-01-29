/*
 * pencil_sketch_main.cpp
 *
 *  Created on: 2013-1-26
 *      Author: Administrator
 */

#include <cv.h>
#include <highgui.h>

const char *inputImagePath="src/MasteringOpenCVwithPractical/chp08/img/andy_liu_hat_no.jpg";

const char *windowName = "WebcamFaceRec";   // Name shown in the GUI window.


void alert_win(IplImage* image){
		cvNamedWindow(windowName,CV_WINDOW_AUTOSIZE);// Resizable window, might not work on Windows.
	    //cvResizeWindow(windowName,DESIRED_CAMERA_WIDTH,DESIRED_CAMERA_HEIGHT);
	    // Get OpenCV to automatically call my "onMouse()" function when the user clicks in the GUI window.
	    //setMouseCallback(windowName, onMouse, 0);
	    cvShowImage(windowName,image);
	    //imshow(windowName,mat);
	    cvWaitKey(0);
	    cvDestroyWindow(windowName);
}






int test_pencil_sketch_main(int argc, char *argv[])
{

	/*
	 * You were not following the steps:

invert the image (make negative)
apply Gaussian Blur
blend the above images by linear dodge or color dodge
The negative image must be completely isolated from the Gaussian blur.
 These operations result in 2 different images,
  and they both need to be combined/blended by linear dodge.
  You were executing the Gaussian blur on the negative image,
   and that was your mistake. I believe it's fixed.
	 */

	int col_1, row_1;
	    uchar b_1, g_1, r_1, b_2, g_2, r_2, b_d, g_d, r_d;

	    IplImage* img = cvLoadImage( inputImagePath  );
	    alert_win(img);
	    IplImage* img1 = cvCreateImage( cvSize( img->width,img->height ), img->depth, img->nChannels);
	    IplImage* img2 = cvCreateImage( cvSize( img->width,img->height ), img->depth, img->nChannels);
	    IplImage* dst = cvCreateImage( cvSize( img->width,img->height ), img->depth, img->nChannels);
	    IplImage* gray= cvCreateImage(cvGetSize(img), img->depth, 1);
	    cvNot(img, img1);
	 //cvSmooth(img1, img2, CV_BLUR, 25,25,0,0);//just like a ghost
	    cvSmooth(img, img2, CV_GAUSSIAN, 7, 7, 0, 0); // (img, img2, CV_GAUSSIAN, 7, 7, 0, 0) last fix :)

	    for( row_1 = 0; row_1 < img1->height; row_1++ )
	    {
	        for ( col_1 = 0; col_1 < img1->width; col_1++ )
	        {
	            b_1 = CV_IMAGE_ELEM( img1, uchar, row_1, col_1 * 3 );
	            g_1 = CV_IMAGE_ELEM( img1, uchar, row_1, col_1 * 3 + 1 );
	            r_1 = CV_IMAGE_ELEM( img1, uchar, row_1, col_1 * 3 + 2 );

	            b_2 = CV_IMAGE_ELEM( img2, uchar, row_1, col_1 * 3 );
	            g_2 = CV_IMAGE_ELEM( img2, uchar, row_1, col_1 * 3 + 1 );
	            r_2 = CV_IMAGE_ELEM( img2, uchar, row_1, col_1 * 3 + 2 );

	//            b_d = b_1 + b_2;
	//            g_d = g_1 + g_2;
	//            r_d = r_1 + r_2;

	            b_d = std::min(255, b_1 + b_2);
	            g_d = std::min(255, g_1 + g_2);
	            r_d = std::min(255, r_1 + r_2);

	            dst->imageData[img1->widthStep * row_1 + col_1* 3] = b_d;
	            dst->imageData[img1->widthStep * row_1 + col_1 * 3 + 1] = g_d;
	            dst->imageData[img1->widthStep * row_1 + col_1 * 3 + 2] = r_d;
	        }
	    }
	   cvCvtColor(dst, gray, CV_BGR2GRAY);
	   alert_win( gray );

	   cvReleaseImage( &img );
	   cvReleaseImage( &img1 ); // Yes, you must release all the allocated memory.
	   cvReleaseImage( &img2 );
	   cvReleaseImage( &dst );
	   cvReleaseImage( &gray);

	   return 0;
}
