package com.example.hasee.materialtest.Weather.gson;

/**
 * Created by hasee on 2017/2/16.
 */

public class AQI {

    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
