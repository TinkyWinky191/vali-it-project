package ee.valitit.project.exception;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class AspectExceptionHandler {

/*    @After()
    @Before()
    @AfterReturning
    @AfterThrowing
    @Around("execution(* ee.valitit.project.service.*.*(..))")
    public Object exceptionsHandlerAtServices(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        try {
            return joinPoint.proceed();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }*/
    //TODO Check and log all throwing exceptions!


}
