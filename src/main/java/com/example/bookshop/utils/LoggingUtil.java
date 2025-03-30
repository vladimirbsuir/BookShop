package com.example.bookshop.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** Class to create logs. */
@Aspect
@Component
public class LoggingUtil {

    Logger logger = LoggerFactory.getLogger(LoggingUtil.class);

    /** Function to create logs before method execution. */
    @Before("execution(* com.example.bookshop..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        if (logger.isInfoEnabled()) {
            logger.info("Executing: {}", joinPoint.getSignature().toShortString());
        }
    }

    /** Function to create logs after method execution. */
    @AfterReturning(pointcut = "execution(* com.example.bookshop..*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        if (logger.isInfoEnabled()) {
            logger.info("Executed: {} with result: {}",
                    joinPoint.getSignature().toShortString(), result);
        }
    }

    /** Function to create logs after exception throwing. */
    @AfterThrowing(pointcut = "execution(* com.example.bookshop..*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        if (logger.isErrorEnabled()) {
            logger.error("Exception in: {} with cause: {}",
                    joinPoint.getSignature().toShortString(), error.getMessage());
        }
    }
}
