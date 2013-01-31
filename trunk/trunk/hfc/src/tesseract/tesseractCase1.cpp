/*
 * tesseractCase1.cpp
 *
 *  Created on: 2013-1-30
 *      Author: hepeng
 */

#include "tesseractCase1.h"

#include "preprocessing.h"

int test4tesseract_api_example(int argc,char** argv){

		const char *language = "chi_sim";//  chi_tra    chi_sim   eng
	    const char *datapath = "./src/tesseract/";
	    const char* filename = "./src/tesseract/tif/chi_sim_text4.TIF";

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
	    //tAPI->SetRectangle(1, 1, 500, 80);//tAPI->SetRectangle(30, 90, 600, 173);

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

/*
���Tesseract-OCR ʶ�����ĳ�����������
2012-04-29 22:49:23|  ���ࣺ ͼ��ʶ��������ʶ |�ֺ� ����
����matlab ��ͼƬת����ѹ��tif ��ʽ
I1=imread('chi.jpg');
imwrite(I1,'chi.tif', 'Compression', 'none');   --
 */

int test4tesseract_chi_tra(int argc,char** argv){
		printf("Tesseract-ocr: %s\n","test4tesseract_chi_tra" );
		const char *language = "eng";//  chi_tra    chi_sim   eng
	    const char *datapath = "./src/tesseract/";
	    const char* filename = "./src/tesseract/tif/phototest.tif";

	    PIX  *pix;
	    tesseract::TessBaseAPI *tAPI = new tesseract::TessBaseAPI();

	    printf("Tesseract-ocr version: %s\n",tesseract::TessBaseAPI::Version());
	    printf("Leptonica version: %s\n", getLeptonicaVersion());

///////////////////////////////////////

	    tAPI->Init(datapath, language, tesseract::OEM_DEFAULT);
	    cv::Mat image = cv::imread(filename);

	    //cv::imwrite('chi_roc.tif',image, 'Compression', 'none');

	    image.convertTo(image,CV_16UC3,255,255);


	    tAPI->SetImage((uchar*)image .data, image.size().width, image.size().height, image.channels(),     image.step1());
	    tAPI->Recognize(0);
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


int test4tesseract_opencv_preprocess(int argc,char** argv){
		printf("Tesseract-ocr: %s\n","test4tesseract_opencv_preprocess" );
		const char *language = "eng";//  chi_tra    chi_sim   eng
	    const char *datapath = "./src/tesseract/";
	    const char* filename = "./src/tesseract/tif/phototest.tif";

	    PIX  *pix;
	    tesseract::TessBaseAPI *tAPI = new tesseract::TessBaseAPI();

	    printf("Tesseract-ocr version: %s\n",tesseract::TessBaseAPI::Version());
	    printf("Leptonica version: %s\n", getLeptonicaVersion());

///////////////////////////////////////

	    tAPI->Init(datapath, language, tesseract::OEM_DEFAULT);
/*
	    cv::Mat image = cv::imread(filename);
	    //cv::imwrite('chi_roc.tif',image, 'Compression', 'none');
	    image.convertTo(image,CV_16UC3,255,255);
*/
	    IplImage* img_old=cvLoadImage( filename,CV_LOAD_IMAGE_GRAYSCALE );
	    int width;
	    int height;
	    IplImage img=preprocessing(img_old,width,height);
	    cv::Mat image= cv::Mat(&img,false);

	    tAPI->SetImage((uchar*)image.data, image.size().width, image.size().height, image.channels(),     image.step1());
	    tAPI->Recognize(0);
	    // run ocr
	    char* outText = tAPI->GetUTF8Text();
	    printf("OCR output:\n\n");
	    printf(outText);

	    tAPI->Clear();
	    tAPI->End();
	    delete [] outText;
	    pixDestroy(&pix);

	    cvReleaseImage(&img_old);

	    return 0;
}
