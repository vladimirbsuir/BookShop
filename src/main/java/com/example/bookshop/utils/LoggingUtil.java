package com.example.bookshop.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingUtil {

    private final Logger logger = LoggerFactory.getLogger(LoggingUtil.class);

    @Before("execution(* com.example.bookshop..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Executing: " + joinPoint.getSignature().toShortString());
    }

    @AfterReturning(pointcut = "execution(* com.example.bookshop..*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Executed: " + joinPoint.getSignature().toShortString() + " with result: " + result);
    }

    @AfterThrowing(pointcut = "execution(* com.example.bookshop..*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        logger.error("Exception in: " + joinPoint.getSignature().toShortString() + " with cause: " + error.getMessage());
    }
}
