package com.duylv.learning.springbootaspect.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class CheckAspect {

  @Before("@annotation(com.duylv.learning.springbootaspect.aspects.FileProperty)")
  public void checkSomethingBefore(JoinPoint joinPoint) {
    System.out.println("Before execution - Roles: " + Arrays.toString(chk(joinPoint)));
  }

  @AfterReturning(pointcut = "@annotation(com.duylv.learning.springbootaspect.aspects.FileProperty)")
  public void checkSomethingAfter(JoinPoint joinPoint) {
    System.out.println("After execution - Roles: " + Arrays.toString(chk(joinPoint)));
  }

  @AfterThrowing(pointcut = "@annotation(com.duylv.learning.springbootaspect.aspects.FileProperty)", throwing = "ex")
  public void checkSomethingAfterThrowingAnException(JoinPoint joinPoint, Exception ex) {
    System.out.println("After throwing an exception - Roles:" + Arrays.toString(chk(joinPoint)) + ex);
  }

  @Around("within(com.duylv.learning.springbootaspect.*)")
  public Object checkSomethingAround(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    Parameter[] parameters = methodSignature.getMethod().getParameters();
    Object[] paramValue = joinPoint.getArgs();
    int numOfParam = paramValue.length;
    for (int index = 0; index < numOfParam; index++) {
      Parameter parameter = parameters[index];
      if (parameter.isAnnotationPresent(FileProperty.class)) {
        FileProperty fileProperty = parameter.getAnnotation(FileProperty.class);
        List<MultipartFile> paramVal = (List<MultipartFile>) paramValue[index];
        boolean hasInvalidFile = paramVal
                .stream()
                .anyMatch(file -> !Arrays.asList(fileProperty.contentTypes()).contains(file.getContentType()));
        if (hasInvalidFile) {
          throw new UnsupportedMediaTypeStatusException("InvalidFile");
        }
      }
    }

    Object result = joinPoint.proceed();
    System.out.println("After in arround execution");
    return result;
  }


  public String[] chk(JoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    CheckSomething myAnnotation = method.getAnnotation(CheckSomething.class);
    return myAnnotation.roles();
  }
}
