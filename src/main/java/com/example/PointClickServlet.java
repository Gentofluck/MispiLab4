package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.Notification;
import javax.management.JMX;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import java.util.List;

@WebServlet("/pointclick")
public class PointClickServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private long lastClickTime = 0;

    private MBeanServer getMBeanServer() {
        return ManagementFactory.getPlatformMBeanServer();
    }    

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        boolean isInCircle = Boolean.parseBoolean(request.getParameter("isInCircle"));
        long currentTime = System.currentTimeMillis();
        long interval = 0;
        if (this.lastClickTime != 0) {
            interval = currentTime - this.lastClickTime;
        }
        this.lastClickTime = currentTime;
    
        try {
            MBeanServer mBeanServer = getMBeanServer();
            if (mBeanServer == null) {
                throw new IllegalStateException("MBeanServer not found");
            }

            ObjectName pointCountObjectName = new ObjectName("com.example:type=PointCount");
            if (!mBeanServer.isRegistered(pointCountObjectName)) {
                PointCount pointCountMBean = new PointCount();
                mBeanServer.registerMBean(pointCountMBean, pointCountObjectName);
            }

            PointCountMBean pointCountMBean = JMX.newMBeanProxy(mBeanServer, pointCountObjectName, PointCountMBean.class);
            pointCountMBean.addPoint(isInCircle, interval);
    
            ObjectNode jsonResponse = objectMapper.createObjectNode();
            jsonResponse.put("totalPoints", pointCountMBean.getTotalPoints());
            jsonResponse.put("outsideCirclePoints", pointCountMBean.getOutsideCirclePoints());
            jsonResponse.put("averageInterval", pointCountMBean.getAverageInterval());
    
            response.getWriter().write(jsonResponse.toString());
        } catch (IllegalStateException ise) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "MBeanServer not found: " + ise.getMessage());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request: " + e.getMessage());
        }
    }
    
}
