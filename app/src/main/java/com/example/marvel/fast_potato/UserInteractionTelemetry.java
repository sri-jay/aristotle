package com.example.marvel.fast_potato;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

/**
 * Created by Sriduth_2 on 23-12-2014.
 */
public class UserInteractionTelemetry {
   static class InformationTelemetry implements  UserTelemetrics {
        private DateTime startTime = null;
        private String interval = null;

        public void start() {
            startTime = new DateTime();
        }

        public void stop() {
           interval = Integer.toString(Seconds.secondsBetween(new DateTime(), startTime).getSeconds());
        }

        public String getDuration() {
            return interval;
        }
   }

    static class QuestionTelemetry implements  UserTelemetrics {
        private DateTime startTime = null;
        private String interval = null;

        public void start() {
            startTime = new DateTime();
        }

        public void stop() {
            interval = Integer.toString(Seconds.secondsBetween(new DateTime(), startTime).getSeconds());
        }

        public String getDuration() {
            return interval;
        }
    }

    static class ActivityTelemetry implements UserTelemetrics {
        private DateTime startTime = null;
        private String interval = null;

        public void start(){

        }

        public void stop() {

        }
        public  String getDuration() { return interval;}
    }
}
