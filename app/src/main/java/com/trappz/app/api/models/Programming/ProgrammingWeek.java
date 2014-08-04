package com.trappz.app.api.models.Programming;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filipe Oliveira on 07-07-2014.
 */
public class ProgrammingWeek {


    private List<Day> days = new ArrayList<Day>();

    private Boolean containsMatch;

    private NextProgBlock nextProgBlock;

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public Boolean getContainsMatch() {
        return containsMatch;
    }

    public void setContainsMatch(Boolean containsMatch) {
        this.containsMatch = containsMatch;
    }

    public NextProgBlock getNextProgBlock() {
        return nextProgBlock;
    }

    public void setNextProgBlock(NextProgBlock nextProgBlock) {
        this.nextProgBlock = nextProgBlock;
    }

}
