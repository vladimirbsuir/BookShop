package com.example.bookshop.utils;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;

@ExtendWith(MockitoExtension.class)
class LoggingUtilTest {

    private LoggingUtil loggingUtil;

    @Mock
    private Logger logger;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private Signature signature;

    private final Throwable testError = new RuntimeException("Test error");

    @BeforeEach
    void setUp() {
        loggingUtil = new LoggingUtil();
        loggingUtil.logger = logger;
    }

    @Test
    void logAfterReturning_LogWhenInfoEnabled() {
        when(logger.isInfoEnabled()).thenReturn(true);
        Object result = "test result";

        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.toShortString()).thenReturn("TestClass.testMethod()");
        loggingUtil.logAfterReturning(joinPoint, result);

        verify(logger).info(
                "Executed: {} with result: {}",
                "TestClass.testMethod()",
                "test result"
        );
    }

    @Test
    void logBefore_NotLogWhenInfoDisabled() {
        when(logger.isInfoEnabled()).thenReturn(false);

        loggingUtil.logBefore(joinPoint);

        verify(logger, never()).info(anyString());
    }

    @Test
    void logAfterReturning_NotLogWhenInfoDisabled() {
        when(logger.isInfoEnabled()).thenReturn(false);

        loggingUtil.logAfterReturning(joinPoint, "result");

        verify(logger, never()).info(anyString());
    }

    @Test
    void logAfterThrowing_LogErrorWhenEnabled() {
        when(logger.isErrorEnabled()).thenReturn(true);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.toShortString()).thenReturn("TestClass.testMethod()");

        loggingUtil.logAfterThrowing(joinPoint, testError);

        verify(logger).error(
                "Exception in: {} with cause: {}",
                "TestClass.testMethod()",
                "Test error"
        );
    }

    @Test
    void logAfterThrowing_NotLogWhenErrorDisabled() {
        when(logger.isErrorEnabled()).thenReturn(false);

        loggingUtil.logAfterThrowing(joinPoint, testError);

        verify(logger, never()).error(anyString());
    }
}
