/*
 * chp08.cpp
 *
 *  Created on: 2013-1-25
 *      Author: hepeng
 */

#include "chp08.h"

#include "../../util.h"


//////////////////////////////////////////////////////////////////////////////////////
// WebcamFaceRec.cpp, by Shervin Emami (www.shervinemami.info) on 30th May 2012.
// Face Detection & Face Recognition from a webcam using LBP and Eigenfaces or Fisherfaces.
//////////////////////////////////////////////////////////////////////////////////////
//
// Some parts are based on the tutorial & code by Robin Hewitt (2007) at:
// "http://www.cognotics.com/opencv/servo_2007_series/part_5/index.html"
//
// Some parts are based on the tutorial & code by Shervin Emami (2009) at:
// "http://www.shervinemami.info/faceRecognition.html"
//
// Requires OpenCV v2.4.1 or later (from June 2012), otherwise the FaceRecognizer will not compile or run.
//
//////////////////////////////////////////////////////////////////////////////////////


// The Face Recognition algorithm can be one of these and perhaps more, depending on your version of OpenCV, which must be atleast v2.4.1:
//    "FaceRecognizer.Eigenfaces":  Eigenfaces, also referred to as PCA (Turk and Pentland, 1991).
//    "FaceRecognizer.Fisherfaces": Fisherfaces, also referred to as LDA (Belhumeur et al, 1997).
//    "FaceRecognizer.LBPH":        Local Binary Pattern Histograms (Ahonen et al, 2006).
//const char *facerecAlgorithm = "FaceRecognizer.Fisherfaces";
//const char *facerecAlgorithm = "FaceRecognizer.Eigenfaces";
const char *facerecAlgorithm = "FaceRecognizer.LBPH";
const char *inputImagePath="src/MasteringOpenCVwithPractical/chp08/img/yangmi006_ko.jpg";//yangmi006_ko.jpg
// Cascade Classifier file:
//used for Face Detection.
//const char *faceCascadeFilename = "src/data/lbpcascade_frontalface.xml";     // LBP face detector.
//const char *faceCascadeFilename = "src/data/haarcascade_frontalface_alt_tree.xml";  // Haar face detector.
//const char *faceCascadeFilename = "src/data/haarcascades/haarcascade_frontalface_default.xml";
const char *faceCascadeFilename = "src/data/haarcascades/haarcascade_frontalface_alt2.xml";
//const char *faceCascadeFilename = "src/data/haarcascades/haarcascade_mcs_upperbody.xml";

//used for eye Detection.
//const char *eyeCascadeFilename1 = "src/data/haarcascade_lefteye_2splits.xml";   // Best eye detector for open-or-closed eyes.
//const char *eyeCascadeFilename2 = "src/data/haarcascade_righteye_2splits.xml";   // Best eye detector for open-or-closed eyes.
//const char *eyeCascadeFilename1 = "src/data/haarcascade_eye.xml";               // Basic eye detector for open eyes only.
//const char *eyeCascadeFilename2 = "src/data/haarcascade_eye_tree_eyeglasses.xml"; // Basic eye detector for open eyes if they might wear glasses.

const char *eyeCascadeFilename1_left = "src/data/haarcascades/haarcascade_mcs_lefteye.xml";       // Good eye detector for open-or-closed eyes.
const char *eyeCascadeFilename1_right = "src/data/haarcascades/haarcascade_mcs_righteye.xml";       // Good eye detector for open-or-closed eyes.

//const char *eyeCascadeFilename1_left = "src/data/haarcascades/haarcascade_mcs_mouth.xml";
//const char *eyeCascadeFilename1_right = "src/data/haarcascades/haarcascade_mcs_rightear.xml";

const char *mouthCascadeFilename = "src/data/haarcascades/haarcascade_mcs_mouth.xml";

// Sets how confident the Face Verification algorithm should be to decide if it is an unknown person or a known person.
// A value roughly around 0.5 seems OK for Eigenfaces or 0.7 for Fisherfaces, but you may want to adjust it for your
// conditions, and if you use a different Face Recognition algorithm.
// Note that a higher threshold value means accepting more faces as known people,
// whereas lower values mean more faces will be classified as "unknown".
const float UNKNOWN_PERSON_THRESHOLD = 0.7f;

// Set the desired face dimensions. Note that "getPreprocessedFace()" will return a square face.
const int faceWidth = 70;
const int faceHeight = faceWidth;

// Try to set the camera resolution. Note that this only works for some cameras on
// some computers and only for some drivers, so don't rely on it to work!
const int DESIRED_CAMERA_WIDTH = 640;
const int DESIRED_CAMERA_HEIGHT = 480;

// Parameters controlling how often to keep new faces when collecting them. Otherwise, the training set could look to similar to each other!
const double CHANGE_IN_IMAGE_FOR_COLLECTION = 0.3;      // How much the facial image should change before collecting a new face photo for training.
const double CHANGE_IN_SECONDS_FOR_COLLECTION = 1.0;       // How much time must pass before collecting a new face photo for training.

const char *windowName = "WebcamFaceRec";   // Name shown in the GUI window.
const int BORDER = 8;  // Border between GUI elements to the edge of the image.

const bool preprocessLeftAndRightSeparately = true;   // Preprocess left & right sides of the face separately, in case there is stronger light on one side.

// Set to true if you want to see many windows created, showing various debug info. Set to 0 otherwise.
bool m_debug = true;


#include <stdio.h>
#include <vector>
#include <string>
#include <iostream>

// Include OpenCV's C++ Interface
#include <opencv2/opencv.hpp>

// Include the rest of our code!
#include "detectObject.h"       // Easily detect faces or eyes (using LBP or Haar Cascades).
#include "preprocessFace.h"     // Easily preprocess face images, for face recognition.
#include "recognition.h"     // Train the face recognition system and recognize a person from an image.

#include "ImageUtils.h"      // Shervin's handy OpenCV utility functions.

using namespace cv;
using namespace std;


#if !defined VK_ESCAPE
    #define VK_ESCAPE 0x1B      // Escape character (27)
#endif


int m_selectedPerson = -1;
int m_numPersons = 0;
vector<int> m_latestFaces;

// Position of GUI buttons:
Rect m_rcBtnAdd;
Rect m_rcBtnDel;
Rect m_rcBtnDebug;
int m_gui_faces_left = -1;
int m_gui_faces_top = -1;


// Load the face and 1 or 2 eye detection XML classifiers.
void roc_initDetectors(CascadeClassifier &faceCascade, CascadeClassifier &eyeCascade1_left, CascadeClassifier &eyeCascade1_right
		,CascadeClassifier &mouthCascade)
{
    // Load the Face Detection cascade classifier xml file.
    try {   // Surround the OpenCV call by a try/catch block so we can give a useful error message!
        faceCascade.load(faceCascadeFilename);
    } catch (cv::Exception &e) {}
    if ( faceCascade.empty() ) {
        cerr << "ERROR: Could not load Face Detection cascade classifier [" << faceCascadeFilename << "]!" << endl;
        cerr << "Copy the file from your OpenCV data folder (eg: 'C:\\OpenCV\\data\\lbpcascades') into this WebcamFaceRec folder." << endl;
        exit(1);
    }
    cout << "Loaded the Face Detection cascade classifier [" << faceCascadeFilename << "]." << endl;

    // Load the Eye Detection cascade classifier xml file.
    try {   // Surround the OpenCV call by a try/catch block so we can give a useful error message!
        eyeCascade1_left.load(eyeCascadeFilename1_left);
    } catch (cv::Exception &e) {}
    if ( eyeCascade1_left.empty() ) {
        cerr << "ERROR: Could not load 1st Eye Detection cascade classifier [" << eyeCascadeFilename1_left << "]!" << endl;
        cerr << "Copy the file from your OpenCV data folder (eg: 'C:\\OpenCV\\data\\haarcascades') into this WebcamFaceRec folder." << endl;
        exit(1);
    }
    cout << "Loaded the 1st Eye Detection cascade classifier [" << eyeCascadeFilename1_left << "]." << endl;

    // Load the Eye Detection cascade classifier xml file.
    try {   // Surround the OpenCV call by a try/catch block so we can give a useful error message!
        eyeCascade1_right.load(eyeCascadeFilename1_right);
    } catch (cv::Exception &e) {}
    if ( eyeCascade1_right.empty() ) {
        cerr << "Could not load 2nd Eye Detection cascade classifier [" << eyeCascadeFilename1_right << "]." << endl;
        // Dont exit if the 2nd eye detector did not load, because we have the 1st eye detector at least.
        //exit(1);
    }
    else
        cout << "Loaded the 2nd Eye Detection cascade classifier [" << eyeCascadeFilename1_right << "]." << endl;

    // Load the Eye Detection cascade classifier xml file.
        try {   // Surround the OpenCV call by a try/catch block so we can give a useful error message!
            eyeCascade1_right.load(eyeCascadeFilename1_right);
        } catch (cv::Exception &e) {}
        if ( eyeCascade1_right.empty() ) {
            cerr << "Could not load 2nd Eye Detection cascade classifier [" << eyeCascadeFilename1_right << "]." << endl;
            // Dont exit if the 2nd eye detector did not load, because we have the 1st eye detector at least.
            //exit(1);
        }
        else
            cout << "Loaded the 2nd Eye Detection cascade classifier [" << eyeCascadeFilename1_right << "]." << endl;


        // Load the Mouth Detection cascade classifier xml file.
            try {   // Surround the OpenCV call by a try/catch block so we can give a useful error message!
                mouthCascade.load( mouthCascadeFilename );
            } catch (cv::Exception &e) {}
            if ( mouthCascade.empty() ) {
                cerr << "Could not load 2nd Mouth Detection cascade classifier [" << mouthCascadeFilename << "]." << endl;
                // Dont exit if the 2nd eye detector did not load, because we have the 1st eye detector at least.
                //exit(1);
            }
            else
                cout << "Loaded the Mouth Detection cascade classifier [" << mouthCascadeFilename << "]." << endl;


}


void test4face_detect() {
	//step 1 face detection
	CascadeClassifier faceCascade;
	CascadeClassifier eyeCascade1_left;
	CascadeClassifier eyeCascade1_right;
	CascadeClassifier mouthCascade;

	// Load the face and 1 or 2 eye detection XML classifiers.
	roc_initDetectors(faceCascade, eyeCascade1_left, eyeCascade1_right,mouthCascade);

	Ptr<FaceRecognizer> model;
	vector<Mat> preprocessedFaces;
	vector<int> faceLabels;
	Mat old_prepreprocessedFace;

	//load image from disk
	IplImage* img = cvLoadImage(inputImagePath);
	//Mat gray;
	Mat cameraFrame(img, true);
	alert_win(cameraFrame);
	cvReleaseImage(&img);

	// Get a copy of the camera frame that we can draw onto.
	Mat displayedFrame;
	cameraFrame.copyTo(displayedFrame);

	// Find a face and preprocess it to have a standard size and contrast & brightness.
	Rect faceRect; // Position of detected face.
	Rect searchedLeftEye, searchedRightEye; // top-left and top-right regions of the face, where eyes were searched.
	Point leftEye, rightEye; // Position of the detected eyes.
	Mat preprocessedFace = roc_getPreprocessedFace(displayedFrame, faceWidth,
			faceCascade, eyeCascade1_left, eyeCascade1_right,
			mouthCascade,
			preprocessLeftAndRightSeparately, &faceRect, &leftEye, &rightEye,
			&searchedLeftEye, &searchedRightEye);

	bool gotFaceAndEyes = false;
	if (preprocessedFace.data) {
		gotFaceAndEyes = true;
	}
	// Draw an anti-aliased rectangle around the detected face.
	if (faceRect.width > 0) {
		rectangle(displayedFrame, faceRect, CV_RGB(255, 255, 0), 2, CV_AA);

		// Draw light-blue anti-aliased circles for the 2 eyes.
		Scalar eyeColor = CV_RGB(0,255,255);
		if (leftEye.x >= 0) { // Check if the eye was detected
			circle(displayedFrame,
					Point(faceRect.x + leftEye.x, faceRect.y + leftEye.y), 6,
					eyeColor, 1, CV_AA);
		}
		if (rightEye.x >= 0) { // Check if the eye was detected
			circle(displayedFrame,
					Point(faceRect.x + rightEye.x, faceRect.y + rightEye.y), 6,
					eyeColor, 1, CV_AA);
		}
	}

	// Show the current preprocessed face in the top-center of the display.
	int cx = (displayedFrame.cols - faceWidth) / 2;
	if (preprocessedFace.data) {
		// Get a BGR version of the face, since the output is BGR color.
		Mat srcBGR = Mat(preprocessedFace.size(), CV_8UC3);
		cvtColor(preprocessedFace, srcBGR, CV_GRAY2BGR);
		// Get the destination ROI (and make sure it is within the image!).
		//min(m_gui_faces_top + i * faceHeight, displayedFrame.rows - faceHeight);
		Rect dstRC = Rect(cx, BORDER, faceWidth, faceHeight);
		Mat dstROI = displayedFrame(dstRC);
		// Copy the pixels from src to dst.
		srcBGR.copyTo(dstROI);
	}
	// Draw an anti-aliased border around the face, even if it is not shown.
	rectangle(displayedFrame,
			Rect(cx - 1, BORDER - 1, faceWidth + 2, faceHeight + 2),
			CV_RGB(200,200,200), 1, CV_AA);


	// Show the most recent face for each of the collected people, on the right side of the display.
	m_gui_faces_left = displayedFrame.cols - BORDER - faceWidth;
	m_gui_faces_top = BORDER;
	for (int i = 0; i < m_numPersons; i++) {
		int index = m_latestFaces[i];
		if (index >= 0 && index < (int) preprocessedFaces.size()) {
			Mat srcGray = preprocessedFaces[index];
			if (srcGray.data) {
				// Get a BGR version of the face, since the output is BGR color.
				Mat srcBGR = Mat(srcGray.size(), CV_8UC3);
				cvtColor(srcGray, srcBGR, CV_GRAY2BGR);
				// Get the destination ROI (and make sure it is within the image!).
				int y = min(m_gui_faces_top + i * faceHeight,
						displayedFrame.rows - faceHeight);
				Rect dstRC = Rect(m_gui_faces_left, y, faceWidth, faceHeight);
				Mat dstROI = displayedFrame(dstRC);
				// Copy the pixels from src to dst.
				srcBGR.copyTo(dstROI);
			}
		}
	}
	// Show the camera frame on the screen.
	alert_win( displayedFrame );

	// If the user wants all the debug data, show it to them!
		Mat face;
		if (faceRect.width > 0) {
			face = cameraFrame(faceRect);
			//alert_win( face );
			if (searchedLeftEye.width > 0 && searchedRightEye.width > 0) {
				Mat topLeftOfFace = face(searchedLeftEye);
				Mat topRightOfFace = face(searchedRightEye);
				//imshow("topLeftOfFace", topLeftOfFace);
				//imshow("topRightOfFace", topRightOfFace);
				alert_win( topLeftOfFace );
				alert_win( topRightOfFace );
			}
		}

}


