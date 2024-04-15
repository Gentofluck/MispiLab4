package com.example;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class PointCount extends NotificationBroadcasterSupport implements PointCountMBean {
    private int totalPoints = 0;
    private int outsideCirclePoints = 0;
    private long totalInterval = 0;
    private long lastClickTime = 0;

    @Override
    public int getTotalPoints() {
        return totalPoints;
    }

    @Override
    public int getOutsideCirclePoints() {
        return outsideCirclePoints;
    }

    public void addPoint(boolean isInCircle, long interval) {
        totalPoints++;

        if (!isInCircle) {
            outsideCirclePoints++;
        }

        totalInterval += interval;

        if (totalPoints % 5 == 0) {
            sendNotification(new Notification("TotalPointsNotification", this, totalPoints,
                    "Total points count is now divisible by 5!"));
        }
    }

    public double getAverageInterval() {
        if (totalPoints == 0) {
            return 0;
        }
        return (double) totalInterval / totalPoints;
    }
}
