package org.miklosbarabas.demo.aspects;

import com.codahale.metrics.MetricRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Aspect to measure runtime of given methods.
 *
 * @author Miklos Barabas
 */
@Aspect
@Component
public class MethodRunMeasureAspect {
    private static Logger logger = LoggerFactory.getLogger(MethodRunMeasureAspect.class);

    @Autowired
    private MetricRegistry metricRegistry;


    @Around("execution(public * org.miklosbarabas.demo.repositories.PaymentRepository..*(..))")
    public Object profilePaymentRepository(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = "PaymentRepository." + pjp.getSignature().getName();
        return doProfiling(pjp, methodName);
    }

    @Around("execution(public * org.miklosbarabas.demo.repositories.PaymentAttributesRepository..*(..))")
    public Object profilePaymentAttributesRepository(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = "PaymentAttributesRepository." + pjp.getSignature().getName();
        return doProfiling(pjp, methodName);
    }

    private Object doProfiling(ProceedingJoinPoint pjp, String methodName) throws Throwable {
        long start = System.currentTimeMillis();

        Object value;

        try {
            value = pjp.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        } finally {
            long duration = System.currentTimeMillis() - start;
            metricRegistry.timer(methodName).update(duration, TimeUnit.MILLISECONDS);
            logger.debug("[{} RUN-TIME] {} ms", methodName, duration);
        }

        return value;
    }
}

