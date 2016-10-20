package com.gft.technicalchallenge;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.concurrent.ThreadFactory;

/**
 * Created by klbk on 20/10/2016.
 */
class CalendarThread implements Runnable{

    private final String name;
    private Iterator<LocalDate> calendarIterator;
    private long sleep;

    CalendarThread(Iterator<LocalDate> calendarIterator, long sleep, String name){
        this.calendarIterator = calendarIterator;
        this.sleep=sleep;
        this.name=name;
    }

    @Override
    public void run() {
        while (calendarIterator.hasNext()){
            System.out.println(name +": "+calendarIterator.next());
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
