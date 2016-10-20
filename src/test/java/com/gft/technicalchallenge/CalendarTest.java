package com.gft.technicalchallenge;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertThat;

public class CalendarTest {

    private Calendar calendar;
    private Iterator<LocalDate> iterator;

    @Before
    public void setUp() throws Exception {
        calendar = new Calendar(LocalDate.of(2016,10,20));
        iterator = calendar.iterator();
    }

    @Test
    public void shouldReturnedValuesBeExpectedValuesFromThursday() {

        List<LocalDate> expected = new ArrayList<>(Arrays.asList(new LocalDate[]{LocalDate.of(2016,10,20),LocalDate.of(2016,10,25),LocalDate.of(2016,10,27),LocalDate.of(2016,11,1)}));

        List<LocalDate> list = new ArrayList<>();
        for(int i=0; i<4; i++)
            list.add(iterator.next());

        assertThat(list, Matchers.contains(expected.toArray()));
    }

    @Test
    public void shouldReturnedValuesBeExpectedValuesFromSunday () {
        calendar = new Calendar(LocalDate.of(2016,10,16));
        iterator = calendar.iterator();
        List<LocalDate> toCompare = new ArrayList<>(Arrays.asList(new LocalDate[]{LocalDate.of(2016,10,18),LocalDate.of(2016,10,20),LocalDate.of(2016,10,25),LocalDate.of(2016,10,27)}));
        List<LocalDate> list = new ArrayList<>();

        for(int i=0; i<4; i++)
            list.add(iterator.next());

        assertThat(list, Matchers.contains(toCompare.toArray()));
    }

    @Test
    public void testAgainstTwoIterators() {
        List<LocalDate> list1 = new ArrayList<>();
        List<LocalDate> list2 = new ArrayList<>();
        Iterator<LocalDate> iterator2 = calendar.iterator();
        for(int i=0; i<100; i++){
            if(iterator.hasNext())
            list1.add(iterator.next());
            if(iterator2.hasNext())
            list2.add(iterator2.next());
        }
        assertThat(list1, Matchers.equalTo(list2));
    }

}