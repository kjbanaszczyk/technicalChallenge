package com.gft.technicalchallenge;

import java.time.LocalDate;
import java.util.Iterator;


final class Calendar implements Iterable<LocalDate> {

    private LocalDate date;

    Calendar(LocalDate date){
        this.date = date;
    }

    @Override
    public Iterator<LocalDate> iterator() {
        return new DateIterator(date);
    }

    private class DateIterator implements Iterator<LocalDate>{

        LocalDate date;

        DateIterator(LocalDate date){
            int dayOfWeek = date.getDayOfWeek().getValue();
            if(dayOfWeek<5)
                this.date=date.plusDays(dayOfWeek%2);
            else
                this.date=date.plusDays(9%dayOfWeek);
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public LocalDate next() {
            if(date.getDayOfWeek().getValue()==2) {
                date = date.plusDays(2);
                return date.minusDays(2);
            }
            else {
                date = date.plusDays(5);
                return date.minusDays(5);
            }
        }
    }
}
