package com.multidestination.activitiesevents_server.exceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * @author Bappy
 */
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(CustomAsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        LOGGER.info("Execution message: " + ex.getMessage());
        LOGGER.info("Method name: " + method.getName());
        for (Object obj : params) {
            LOGGER.info("Parameter value: " + obj);
        }
    }
}
