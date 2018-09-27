package com.yang.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFParseTest {

    public static final String FILE_PATH = "/Users/jack/Downloads/1205435104.pdf";

    public static void parse(String fileName){
        File file= new File(fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
//            int len =0;
            byte[] buff = new byte[1024];
            List<Byte> bList = new ArrayList<>();
            List<byte[]> list = new ArrayList<>();
            while (fis.read(buff)>0){
                for (int i = 0; i < buff.length; i++)
                    if (buff[i] != '\n') {
                        bList.add(buff[i]);
                    } else {
                        int size = bList.size();
                        byte[] b = new byte[size];
                        for (int j = 0; j <size; j++) {
                            b[j]=bList.get(j);
                        }
                        System.err.println(new String(b,"utf-8"));
                        list.add(b);
                        bList = new ArrayList<>();
                    }
            }
            fis.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        parse(FILE_PATH);

//        System.err.println(Double.MAX_VALUE);
//        System.err.println(Float.MAX_VALUE);
//
//        System.err.println(Long.MAX_VALUE);
        System.err.println(1f==1f);
        System.err.println(1.0f);

//        System.err.println(Long.MAX_VALUE<Double.MAX_VALUE);
//        System.err.println(Long.MAX_VALUE<Float.MAX_VALUE);
    }
}