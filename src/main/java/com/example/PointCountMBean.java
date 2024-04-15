package com.example;

import javax.management.NotificationBroadcaster;

public interface PointCountMBean extends NotificationBroadcaster {
    int getTotalPoints();
    int getOutsideCirclePoints();
    void addPoint(boolean isInCircle, long interval);
    double getAverageInterval();
}
