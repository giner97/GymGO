package com.example.giner.gymgo.Objetos;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;


public class EventDecorator implements DayViewDecorator {
    private final int color;
    private HashSet<CalendarDay> dates = new HashSet<>();

    public EventDecorator(int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates.clear();
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, color));
    }
}
