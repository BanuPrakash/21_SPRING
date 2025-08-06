package com.adobe.rentalapp.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TransactionalAspect {

    @Around("@annotation(Tx)")
    public Object doTransaction(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("Begin Transaction");
        Object ret = null;
        try {
                ret = pjp.proceed();
            System.out.println("Commit");
        } catch (Exception ex) {
            System.out.println("Rollback");
            throw  ex;
        }
        return  ret;
    }
}
