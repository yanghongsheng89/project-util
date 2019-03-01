package com.yang.util;

import junit.framework.TestCase;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class QuartzTest extends TestCase {


    public void test(){
        QuartzThread thread = new QuartzThread();
        thread.setDaemon(true);
        thread.start();
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
    public void test01(){

        Signal signal = new Signal("ABRT");
        SignalHandler handler = new SignalHandler() {
            public void handle(Signal signal) {
                int number = signal.getNumber();
                System.err.println(number);
                if (number == 2){
                    System.err.println(signal.getName());
                }
            }
        };

        handler.handle(signal);
    }
}