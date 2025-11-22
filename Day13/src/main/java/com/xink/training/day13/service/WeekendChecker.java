package com.xink.training.day13.service;

import java.time.DayOfWeek;

public class WeekendChecker {
    
    public boolean isWeekend(DayOfWeek day) {
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
}

