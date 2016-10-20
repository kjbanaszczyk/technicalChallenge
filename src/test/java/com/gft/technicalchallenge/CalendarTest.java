package com.gft.technicalchallenge;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by klbk on 20/10/2016.
 */
public class CalendarTest {

    Calendar calendar;
    Iterator<LocalDate> iterator;
    @Before
    public void setUp() throws Exception {
        calendar = new Calendar(LocalDate.of(2016,10,20));
        iterator = calendar.iterator();
    }

    @Test
    public void testInitValuesFromThursday () {
        List<LocalDate> toCompare = new ArrayList<>(Arrays.asList(new LocalDate[]{LocalDate.of(2016,10,20),LocalDate.of(2016,10,25),LocalDate.of(2016,10,27),LocalDate.of(2016,11,1)}));
        List<LocalDate> list = new ArrayList<>();
        for(int i=0; i<4; i++)
            list.add(iterator.next());

        assertThat(list, Matchers.contains(toCompare.toArray()));

    }



}