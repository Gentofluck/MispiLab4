package com.example;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.NotCompliantMBeanException;

public class MBeanRegistrar {
    @Resource
    private MBeanServer mBeanServer;

    @PostConstruct
    public void registerMBeans() {
        try {
            ObjectName pointCountObjectName = new ObjectName("com.example:type=PointCount");
            PointCount pointCountMBean = new PointCount();
            mBeanServer.registerMBean(pointCountMBean, pointCountObjectName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
