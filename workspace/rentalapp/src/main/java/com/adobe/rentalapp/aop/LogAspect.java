package com.adobe.rentalapp.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

    // Advice + Pointcut
    @Before("execution(* com.adobe.rentalapp.service.RentalService.*(..))")
    public void logBefore(JoinPoint jp) {
        Object[] args = jp.getArgs();
        log.info("Called :" + jp.getSignature());
        for(Object arg: args) {
            log.info("Argument : " + arg);
        }
    }

    @After("execution(* com.adobe.rentalapp.service.RentalService.*(..))")
    public void logAfter(JoinPoint jp) {
        log.info("*******");
    }

    @Around("execution(* com.adobe.rentalapp.service.RentalService.*(..))")
    public Object profile(ProceedingJoinPoint  jp) throws  Throwable{
        long startTime = System.currentTimeMillis();
            Object ret = jp.proceed(); // call actual method
        long endTime = System.currentTimeMillis();
        log.info("Time : " + (endTime - startTime) + " ms");
        return  ret;
    }

    @AfterThrowing(value = "execution(* com.adobe.rentalapp.service.RentalService.*(..))", throwing = "ex")
    public void logException(JoinPoint jp, Exception ex) {
        log.info("Exception : " + ex.getMessage()  + " in " + jp.getSignature());
    }
}
