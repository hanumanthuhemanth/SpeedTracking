package com.hemanth.poc.speedtracking;

import android.location.Location;

public interface GPSCallback
{
        public abstract void onGPSUpdate(Location location);
}