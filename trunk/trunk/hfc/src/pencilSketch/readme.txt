http://www.barbato.us/2010/12/22/a-quick-algorithm-to-turn-an-image-or-video-into-pencil-sketch-using-opencv/

http://stackoverflow.com/questions/5675151/i-want-to-convert-an-image-into-pencil-sketch-in-opencv
http://www.showandtell-graphics.com/photosketch.html


blImagePencilSketch ¡ª A quick algorithm to turn an image or video into pencil sketch using opencv
December 22nd, 2010 Posted in Image Analysis Labs . Programming Labs Write comment
This entry is part 10 of 17 in the series blImageAPI -- BarbatoLabs Image API

blImageAPI -- BarbatoLabs Image API
blImage ¡ª An image data structure based on shared_ptr and IplImage*
blCaptureDevice ¡ª A simple data structure to handle video sources in OpenCV
blImageOperators ¡ª Overloaded operators and functions to use blImage as a generic matrix in c++
blImageAPI ¡ª separate an opencv image into its individual channels
blImageAPI ¡ª Take the negative of an opencv image, no matter what the depth is
blImageAPI ¡ª Convert an opencv image from RGB to HSV color space no matter what the depth is
blMemStorage ¡ª A simple data structure to wrap OpenCV¡¯s CvMemStorage with shared_ptr
blTexture ¡ª Load an IplImage into an opengl texture and create webcam and video textures using the blImageAPI
blImageBlending ¡ª Emulating photoshop¡¯s blending modes in opencv
blImagePencilSketch ¡ª A quick algorithm to turn an image or video into pencil sketch using opencv
blImageTiling ¡ª Algorithms to augment opencv images in various ways
blImageShifting ¡ª Shift images by any desired amount in any direction for any purporse
blSignalProcessing ¡ª Fast Fourier Transforms and signal processing using OpenCV
blVideoThread ¡ª A simple class to capture video using opencv in a parallel thread
blVideoThread2 ¡ª Another way to capture video using opencv in a parallel thread
blEncodeAndDecode ¡ª Simple functions to encode and decode images in memory using OpenCV without having to save/read to disk
blImageSerialization ¡ª Simple functions to serialize/unserialize an IplImage using opencv
Introduction

This is a very short post showing how we can turn an image or a video into a pencil sketch using the blImageAPI.

The algorithm

The idea is simple and looks as follows:

Read in an image or video frame
Copy the read image
Invert the copied image
Blur the inverted/copied image
Blend the two images together using the ¡°BlendLinearDodge¡± algorithm from my image blending post
The code

I¡¯ve defined a couple of functions in a file called blImagePencilSketch.hpp which follows:

blImagePencilSketch.hpp (Click to see code¡­)

Note: You have to be careful about the type of the image sent to the function. I haven¡¯t made the function work for all depths, so the images have to be ¡°unsigned char¡± or 32-bit ¡°float¡± but nothing else.

Usage

Note: The functions allow some flexibility in choosing the blur parameters, which have to be odd for the function not to fail

The following snippet shows a quick example where we read frames from a webcam and show them in pencil sketch.

int main(int argc, char *argv[])
{
    // Connect to the webcam
    blCaptureDevice Webcam;
    Webcam.ConnectToWebcam(0);
 
    // The captured frame
    blImage< blColor3<unsigned char> > Frame;
 
    // The pencil drawn frame
    blImage< blColor3<unsigned char> > PencilDrawnFrame;
 
    char key = 0;
 
    while(key != 'q')
    {
        // Query a frame
        Webcam.QueryFrame(Frame);
 
        // Turn the queried frame
        // into a pencil sketch
        ImageToPencilSketch(Frame,PencilDrawnFrame);
 
        // Show both the original and
        // pencil sketch frames
        cvShowImage("Original Frame",Frame);
        cvShowImage("Pencil Sketch Frame",PencilDrawnFrame);
 
        // Wait for a key input
        key = cvWaitKey(1);
    }
}
The following video shows the resulting output:


Downloads

I have put all the files into a zip file which can be downloaded here. All you have to do is extract it somewhere, let¡¯s say in a directory called blImageAPI, and then include the blImageAPI.hpp file as follows:

#include "blImageAPI/blImageAPI.hpp"
using namespace blImageAPI;
Note:  Everything is declared in a namespace blImageAPI, and such you would use it as: blImageAPI::blImage

blImageAPI.zip -- Ver Jun/06/2011 1:43pm (1626)
Updates

Check back because I¡¯ll be updating the functions to work with all image types

Dec/26/2010 ¡ª The function was using CV_GAUSSIAN no matter what SmoothType was being passed to the function. Now, I¡¯ve changed it to using the SmoothType parameter now.