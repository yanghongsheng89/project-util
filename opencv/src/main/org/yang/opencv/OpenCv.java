package org.yang.opencv;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import java.util.Random;

public class OpenCv {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public void test(){
        System.out.println("Welcome to OpenCV " + Core.VERSION);
        Mat m = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
        System.out.println("OpenCV Mat: " + m);
        Mat mr1 = m.row(1);
        mr1.setTo(new Scalar(1));
        Mat mc5 = m.col(5);
        mc5.setTo(new Scalar(5));
        System.out.println("OpenCV Mat data:\n" + m.dump());
    }

    /**
     * 面部识别
     */
    public void detectFace() {
        System.out.println("\nRunning DetectFaceDemo");
        // Create a face detector from the cascade file in the resources
        // directory.
        CascadeClassifier faceDetector = new CascadeClassifier(getClass().getResource("/lbpcascade_frontalface.xml").getPath());
        Mat image = Imgcodecs.imread(getClass().getResource("/lena.png").getPath());
        // Detect faces in the image.
        // MatOfRect is a special container class for Rect.
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);
        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
        // Draw a bounding box around each face.
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }
        // Save the visualized detection.
        String filename = "./imgs/faceDetection.png";
        System.out.println(String.format("Writing %s", filename));
        Imgcodecs.imwrite(filename, image);
    }

    /**
     *  **图片数据计算**
     */
    public void capture(){
        VideoCapture capture = new VideoCapture();
        capture.open(0);

        double width =capture.get(Videoio.CV_CAP_PROP_FRAME_WIDTH);
        double height = capture.get(Videoio.CV_CAP_PROP_FRAME_HEIGHT);

        if (capture.isOpened()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Mat mat = new Mat();
            capture.read(mat);
            System.out.println("width, height = "+mat.cols()+", "+mat.rows());
            System.err.println(" cwidth,cheight:"+width+","+height);

            Mat matB = mat.clone();// 2x+1
            for (int j = 0; j <= height*2; j++) {
                for (int i = 0; i < width; i++) {
                    Random random = new Random();
                    double[] data = mat.get(i, j);
                    double [] dataB = new double[]{0.0,0.0,0.0};
                    int z = random.nextInt(151);
                    int k = 2*i+ z;
                    if (data != null){
//                        System.err.println(data.length);
                        dataB[0]=(Math.multiplyExact((int)data[0], i)+z)&0xff;
                        dataB[1]=(Math.multiplyExact((int)data[1], j)+z)&0xff;
                        dataB[2]=(Math.multiplyExact((int)data[2], k)+z)&0xff;

                        data[0]=(~(int)data[0])&0xff;
                        data[1]=(~(int)data[1])&0xff;
                        data[2]=(~(int)data[2])&0xff;
                        mat.put(i, j, data);
                        matB.put(i,j,dataB);
                    }else{
                        data = new double[]{1,1,1};
                        data[0]=255;
                        data[1]=255;
                        data[2]=255;

                        dataB[0]=(z)&0xff;
                        dataB[1]=(z)&0xff;
                        dataB[2]=(k+z)&0xff;
                        mat.put(i, j, data);
                        matB.put(i, j, dataB);
                    }
                }
            }
            System.err.println(mat.empty());
            Imgcodecs.imwrite("./imgs/a.jpg", mat);
            Imgcodecs.imwrite("./imgs/b.jpg", matB);
            mat.release();
            System.err.println("open camera");
        }
        if (capture.isOpened()){
            capture.release();
        }
    }

    public void takeVideo(String videoName){
        VideoCapture capture = new VideoCapture();
        capture.open(0);

        Size size = new Size(capture.get(Videoio.CV_CAP_PROP_FRAME_WIDTH),capture.get(Videoio.CV_CAP_PROP_FRAME_HEIGHT));
        double fps = capture.get(Videoio.CV_CAP_PROP_FPS);

        VideoWriter writer = new VideoWriter(videoName,VideoWriter.fourcc('M', 'P', '4', '2'), fps,size);

        Mat mat = new Mat();
        boolean read = capture.read(mat);
        if (mat.empty()){
            return;
        }

        long start = System.currentTimeMillis();
        Long takeTime =10*1000l;
        while (read){
            mat = new Mat();
            read = capture.read(mat);
            writer.write(mat);
            HighGui.imshow("hello", mat);
            HighGui.waitKey((int) (1000 / fps));
            long end = System.currentTimeMillis();
            if ((end-start)>takeTime){
                break;
            }
        }
        //释放资源
        capture.release();
        writer.release();
        mat.release();
    }


    public void takePicFromVideo(String videoName,String picPaht){
        VideoCapture capture = new VideoCapture(videoName);
        Mat mat = new Mat();
        boolean read =capture.read(mat);
        double fps = capture.get(Videoio.CV_CAP_PROP_FPS);
        int i = 1;
        while (read){
            i ++;
            capture.set(Videoio.CAP_PROP_POS_FRAMES,i*fps);
            String fileName =System.nanoTime()+".png";
            Imgcodecs.imwrite(picPaht+"/"+fileName,mat);
            read = capture.read(mat);
        }
        capture.release();
        mat.release();
    }

}