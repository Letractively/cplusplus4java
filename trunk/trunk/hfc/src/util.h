/*
 * util.h
 *
 *  Created on: 2013-1-31
 *      Author: hepeng
 */

#ifndef UTIL_H_
#define UTIL_H_

#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>

//void alert_win(IplImage* image);
void alert_win(const CvArr* image);
void alert_win(cv::Mat image);
#endif /* UTIL_H_ */
