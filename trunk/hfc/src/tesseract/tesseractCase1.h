/*
 * tesseractCase1.h
 *
 *  Created on: 2013-1-28
 *      Author: hepeng
 */

#ifndef TESSERACTCASE1_H_
#define TESSERACTCASE1_H_



#include <tesseract/baseapi.h>
#include <leptonica/allheaders.h>


int test4tesseract_api_example(int argc,char** argv){

		const char *language = "eng";//  chi_tra       chi_sim
	    const char *datapath = "./src/tesseract/";
	    const char* filename = "./src/tesseract/tif/phototest.tif";

	    PIX  *pix;
	    tesseract::TessBaseAPI *tAPI = new tesseract::TessBaseAPI();

	    printf("Tesseract-ocr version: %s\n",tesseract::TessBaseAPI::Version());
	    printf("Leptonica version: %s\n", getLeptonicaVersion());

	    // Create pix
	    FILE *fp = fopen(filename, "r");
	    if (fp) {
	        fclose(fp);
	        pix = pixRead(filename);
	        if (pix == NULL) {
	            printf("Unsupported image type.\n");
	            exit(3);
	        }
	        else {
	            printf("Pix created\n");
	        }
	    } else {
	        printf("file '%s' doesn't exist.\n", filename);
	        exit(3);
	    }

	    // Initalize tesseract
	    int rc = tAPI->Init(datapath, language, tesseract::OEM_DEFAULT, NULL, 0, NULL, NULL, false);
	    if (rc) {
	      fprintf(stderr, "Could not initialize tesseract.\n");
	      exit(1);
	    }

	    // Set PIX for OCR
	    tAPI->SetImage(pix);

	    // Optional: set rectangle to OCR
	    //tAPI->SetRectangle(30, 90, 600, 173);

	    // run ocr
	    char* outText = tAPI->GetUTF8Text();
	    printf("OCR output:\n\n");
	    printf(outText);

	    tAPI->Clear();
	    tAPI->End();
	    delete [] outText;
	    pixDestroy(&pix);
	    return 0;

}



#endif /* TESSERACTCASE1_H_ */
