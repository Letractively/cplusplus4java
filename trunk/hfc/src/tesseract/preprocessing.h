/*
 *  preprocessing.h
 *  
 *
 *  Created by damiles on 18/11/08.
 *  Copyright 2008 __MyCompanyName__. All rights reserved.
 *
 */
#ifdef _CH_
#pragma package <opencv>
#endif

#ifndef _EiC
/*
#include <cv.h>
#include <highgui.h>
#include <stdio.h>
#include <ctype.h>
*/
#include <tesseract/baseapi.h>
#include <leptonica/allheaders.h>

#include <opencv2/opencv.hpp>

#endif

IplImage preprocessing(IplImage* imgSrc,int new_width, int new_height);
