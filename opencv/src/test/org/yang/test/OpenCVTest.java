package org.yang.test;

import junit.framework.TestCase;

import org.junit.*;
import org.yang.opencv.OpenCv;

import java.io.File;

public class OpenCVTest extends TestCase{

    private static OpenCv openCv = new OpenCv();

    private static final String IMG_PATH="./imgs";

    private static final String VIDEO_NAME="test.mp4";

    @Override
    protected void setUp() throws Exception {
        //openCv = new OpenCv();
    }

    @Override
    protected void tearDown() throws Exception {
        //assertNotNull(openCv);
        //openCv = null;
    }

    @Test
    public void testOpenCv(){
        openCv.test();
    }

    @Test
    public void testDetectFace(){
        openCv.detectFace();
    }



    @Before
    public void testTakeVideo(){
        System.err.println("before....");
        openCv.takeVideo(VIDEO_NAME);
    }
    @Test
    public void testCamera(){
        openCv.capture();
    }
    @After
    public void testTakePicFromVideo(){
        System.err.println("after...");
        openCv.takePicFromVideo(VIDEO_NAME,IMG_PATH);
        delFile(IMG_PATH);
        delFile(VIDEO_NAME);
    }

    private void delFile(String path){
        File file = new File(path);
        if (file.isDirectory()){
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                delFile(files[i].getAbsolutePath());
            }
        }else{
            System.err.printf("delete file:%s\n",file.getAbsolutePath());
            file.delete();
        }
    }


}