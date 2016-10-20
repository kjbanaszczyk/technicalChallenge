package com.gft.technicalchallenge;

import java.time.LocalDate;

/**
 * Created by klbk on 20/10/2016.
 */
public class TestThreads {

    public static void test(String... args){

        Calendar calendar = new Calendar(LocalDate.of(2016,10,19));

        CalendarThread thread = new CalendarThread(calendar.iterator(),500,"t1");
        CalendarThread thread1 = new CalendarThread(calendar.iterator(),1000,"t2");

        new Thread(thread).start();
        new Thread(thread1).start();
    }

}
