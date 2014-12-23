package com.example.marvel.fast_potato;

/**
 * Created by Sriduth_2 on 20-12-2014.
 */
public class UserBehaviousMetrics {
    private String startTimestamp = null;
    private String stopTimestamp = null;

    UserBehaviousMetrics(String start) {
        startTimestamp = start;
    }

    void finishQuiz(String stop) {
        stopTimestamp = stop;
    }
}
