/*
 * shape_model_exe.h
 *
 *  Created on: 2013-1-24
 *      Author: hepeng
 */

#ifndef SHAPE_MODEL_EXE_H_
#define SHAPE_MODEL_EXE_H_

#include "ft/ft.hpp"
#include <opencv2/highgui/highgui.hpp>
#include <iostream>
#include <fstream>

//#include <stdio.h>// For 'sprintf()'

//==============================================================================
bool
parse_help(int argc,char** argv)
{
  for(int i = 1; i < argc; i++){
    string str = argv[i];
    if(str.length() == 2){if(strcmp(str.c_str(),"-h") == 0)return true;}
    if(str.length() == 6){if(strcmp(str.c_str(),"--help") == 0)return true;}
  }return false;
}
//==============================================================================
float
parse_frac(int argc,char** argv)
{
  for(int i = 1; i < argc; i++){
    string str = argv[i];
    if(str.length() != 2)continue;
    if(strcmp(str.c_str(),"-f") == 0){
      if(argc > i+1)return atof(argv[i+1]);
    }
  }return 0.95;
}
//==============================================================================
float
parse_kmax(int argc,char** argv)
{
  for(int i = 1; i < argc; i++){
    string str = argv[i];
    if(str.length() != 2)continue;
    if(strcmp(str.c_str(),"-k") == 0){
      if(argc > i+1)return atoi(argv[i+1]);
    }
  }return 20;
}
//==============================================================================
bool
parse_mirror(int argc,char** argv)
{
  for(int i = 1; i < argc; i++){
    string str = argv[i];
    if(str.length() != 8)continue;
    if(strcmp(str.c_str(),"--mirror") == 0)return true;
  }return false;
}


const char* usage="usage: ./hfc.exe annotation_file shape_modell_file "
		"[-f fraction_of_variation] [-k maximum_modes] [--mirror]";
/*
 *arguments: outdir/annotations.yaml  outdir/train_model_file
 */
int train_shape_model_main(int argc,char** argv){
	//load data
	if( argc<3 ){
		cout<<usage<<endl;
		return 0;
	}
	float frac=parse_frac(argc,argv);
	int kmax=parse_kmax(argc,argv);
	bool mirror=parse_mirror(argc,argv);
	char* anno_file=argv[1];
	ft_data data=load_ft<ft_data>( anno_file );
	if( data.imnames.size()==0 ){
		cerr<<"Data file does not contain any annotations. "<<endl;
		return 0;
	}

	//remove unlabeled samples and get reflections as well
	data.rm_incomplete_samples();
	vector<vector<Point2f> > points;
	for( int i=0;i<int( data.points.size() );i++ ){
		points.push_back( data.get_points(i,false) );
		if( mirror ){
			points.push_back( data.get_points(i,true) );
		}
	}

	//train model and save to file
	cout<<"shape model training samples: "<<points.size()<<endl;
	shape_model smodel;
	smodel.train(points,data.connections,frac,kmax);
	cout<<" retained: "<<smodel.V.cols-4 <<" modes "<<endl;
	char* shape_model_file=argv[2];
	save_ft(shape_model_file,smodel);
	return 0;
}




//==============================================================================
void
draw_string(Mat img,                       //image to draw on
        const string text)             //text to draw
{
  Size size = getTextSize(text,FONT_HERSHEY_COMPLEX,0.6f,1,NULL);
  putText(img,text,Point(0,size.height),FONT_HERSHEY_COMPLEX,0.6f,
      Scalar::all(0),1,CV_AA);
  putText(img,text,Point(1,size.height+1),FONT_HERSHEY_COMPLEX,0.6f,
      Scalar::all(255),1,CV_AA);
}
//==============================================================================
void
draw_shape(Mat &img,
       const vector<Point2f> &q,
       const Mat &C)
{
  int n = q.size();
  for(int i = 0; i < C.rows; i++)
    line(img,q[C.at<int>(i,0)],q[C.at<int>(i,1)],CV_RGB(255,0,0),2);
  for(int i = 0; i < n; i++)
    circle(img,q[i],1,CV_RGB(0,0,0),2,CV_AA);
}
//==============================================================================
float                                      //scaling factor
calc_scale(const Mat &X,                   //scaling basis vector
       const float width)              //width of desired shape
{
  int n = X.rows/2; float xmin = X.at<float>(0),xmax = X.at<float>(0);
  for(int i = 0; i < n; i++){
    xmin = min(xmin,X.at<float>(2*i));
    xmax = max(xmax,X.at<float>(2*i));
  }return width/(xmax-xmin);
}
/*
 *visualize_shape_model: animate the modes of variation in a shape_model object
 *arguments:outdir/train_model_file
 */
int visualize_shape_model_main(int argc,char** argv){
	//load data
	if( argc<2 ){
		cout<<"usage: ./hfc shape_model "<<endl;
		return 0;
	}

	char* shape_model_file=argv[1];
	shape_model smodel=load_ft<shape_model>( shape_model_file );

	//compute rigid parameters
	int n=smodel.V.rows/2;
	float scale=calc_scale( smodel.V.col(0),200 );
	float tranx=n*150.0/smodel.V.col(2).dot( Mat::ones(2*n,1,CV_32F) );
	float trany=n*150.0/smodel.V.col(3).dot( Mat::ones(2*n,1,CV_32F) );

	//generate trajectory of parameters
	vector<float> val;
	for(int i=0;i<50;i++){val.push_back(float(i)/50);}
	for(int i=0;i<50;i++){val.push_back(float(50-i)/50);}
	for(int i=0;i<50;i++){val.push_back(-float(i)/50);}
	for(int i=0;i<50;i++){val.push_back(-float(50-i)/50);}

	//visualize
	Mat img(300,300,CV_8UC3);
	namedWindow("shape model");
	while(1){
		for(int k=4;k<smodel.V.cols;k++){
			for(int j=0;j<int(val.size());j++){
				Mat p=Mat::zeros(smodel.V.cols,1,CV_32F);
				p.at<float>(0)=scale;p.at<float>(2)=tranx;p.at<float>(3)=trany;
				p.at<float>(k)=scale*val[j]*3.0*sqrt(smodel.e.at<float>(k));

				p.copyTo(smodel.p);
				img=Scalar::all(255);
				char str[256];sprintf(str,"mode: %d ,val: %f sd ",k-3,val[j]/3.0);
				draw_string(img,str);

				vector<Point2f> q=smodel.calc_shape();
				draw_shape(img,q,smodel.C);
				imshow("shape mode",img);
				if( waitKey(10)=='q' )return 0;
			}
		}
	}return 0;
}

#endif /* SHAPE_MODEL_EXE_H_ */
