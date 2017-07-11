/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package org.android10.gintonic.aspect;

import android.util.Log;

import org.android10.gintonic.internal.DebugLog;
import org.android10.gintonic.internal.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Aspect representing the cross cutting-concern: Method and Constructor Tracing.
 */
@Aspect
public class TraceAspect {

  //ydc start
  private static final String TAG = "ydc";
  //截获任何包中以类名以Activity结尾的并且带on的所有方法
 /** @Before("execution(* android.app.Activity.on**(..))")
  public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
    String key = joinPoint.getSignature().toString();
    Log.d(TAG, "onActivityMethodBefore: " + key+"\n"+joinPoint.getThis());
  }**/

 //截获任何包中以类名以Activity结尾对象的所有方法
  /** @Before("execution(* *..Activity+.*(..))")
  public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
  String key = joinPoint.getSignature().toString();
  Log.d(TAG, "onActivityMethodBefore: " + key+"\n"+joinPoint.getThis());
  }**/

    //截获任何包中以类名以Activity、Fragment结尾的所有方法
    @Before("execution(* *..Activity+.*(..)) ||execution(* *..Fragment+.*(..))")
    public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
    String key = joinPoint.getSignature().toString();
    Log.d(TAG, "onActivityMethodBefore: " + key+"\n"+joinPoint.getThis());
    }

  //截获任何包中以类名以Activity、Layout结尾，并且该目标类和当前类是一个Object的对象的所有方法
    /**
 @Before("(execution(* *..Activity+.*(..)) || execution(* *..Layout+.*(..))) && target(Object) && this(Object)")
  public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
    String key = joinPoint.getSignature().toString();
    Log.d(TAG, "onActivityMethodBefore: " + key+"\n"+joinPoint.getThis());
  }**/

  //Before和After的用法
    /**
  @Before("execution(* android.app.Activity.on*(android.os.Bundle))")
  public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
    String key = joinPoint.getSignature().toString();
    Log.d(TAG, "onActivityMethodBefore: " + key);
  }

  @After("execution(* android.app.Activity.on*(android.os.Bundle))")
  public void onActivityMethodAfter(JoinPoint joinPoint) throws Throwable {
    String key = joinPoint.getSignature().toString();
    Log.d(TAG, "onActivityMethodAfter: " + key);
  }**/

    //Around的用法
    /**
  @Around("execution(* com.example.myaspectjapplication.activity.RelativeLayoutTestActivity.testAOP())")
  public void onActivityMethodAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    String key = proceedingJoinPoint.getSignature().toString();
    Log.d(TAG, "onActivityMethodAroundFirst: " + key);
    proceedingJoinPoint.proceed();
    Log.d(TAG, "onActivityMethodAroundSecond: " + key);
  }**/

  //execution用法
    /**
  @Before("execution(* com.example.myaspectjapplication.activity.RelativeLayoutTestActivity.testAOP(..))")
  public void methodAOPTest(JoinPoint joinPoint) throws Throwable {
    String key = joinPoint.getSignature().toString();
    Log.d(TAG, "methodAOPTest: " + key);
  }**/

  //call用法
    /**
  @Before("call(* com.example.myaspectjapplication.activity.RelativeLayoutTestActivity.testAOP(..))")
  public void methodAOPTest(JoinPoint joinPoint) throws Throwable {
    String key = joinPoint.getSignature().toString();
    Log.d(TAG, "methodAOPTest: " + key);
  }**/



    /**private static final String POINT_METHOD = "execution(* org.android10.viewgroupperformance.activity.RelativeLayoutTestActivity.*(..))";
    private static final String POINT_CALLMETHOD = "call(* org.android10.viewgroupperformance.activity.RelativeLayoutTestActivity.*(..))";
    @Pointcut(POINT_METHOD)
    public void methodAnnotated(){}
    @Pointcut(POINT_CALLMETHOD)
    public void methodCallAnnotated(){}
    @Around("methodAnnotated()")
    public Object aronudWeaverPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        joinPoint.proceed();

      MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
      String className = methodSignature.getDeclaringType().getSimpleName();
      String methodName = methodSignature.getName();
      String result = "aroundWeaverPoint";
        Log.e(TAG,"----------------------------->aroundWeaverPoint"+"|"+joinPoint.getThis()+"|"+joinPoint.getArgs()+"|"+joinPoint.getKind()+"|"+joinPoint.getArgs()+"|"+className+"|"+methodName);
        return  result;//替换原方法的返回值
    }
    @Before("methodCallAnnotated()")
    public void beforecall(JoinPoint joinPoint){
        Log.e(TAG,"beforecall");
    }**/



  //ydc end

  private static final String POINTCUT_METHOD =
      "execution(@org.android10.gintonic.annotation.DebugTrace * *(..))";

  private static final String POINTCUT_CONSTRUCTOR =
      "execution(@org.android10.gintonic.annotation.DebugTrace *.new(..))";

  @Pointcut(POINTCUT_METHOD)
  public void methodAnnotatedWithDebugTrace() {}

  @Pointcut(POINTCUT_CONSTRUCTOR)
  public void constructorAnnotatedDebugTrace() {}

  @Around("methodAnnotatedWithDebugTrace() || constructorAnnotatedDebugTrace()")
  public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
    //获取方法信息对象
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    //获取当前对象
    String className = methodSignature.getDeclaringType().getSimpleName();
    String methodName = methodSignature.getName();

    //获取当前对象，通过反射获取类别详细信息
    //String className2 = joinPoint.getThis().getClass().getName();

    //初始化计时器
    final StopWatch stopWatch = new StopWatch();
    //开始监听
    stopWatch.start();
    //调用原方法的执行。
    Object result = joinPoint.proceed();
    //监听结束
    stopWatch.stop();

    DebugLog.log(className, buildLogMessage(methodName, stopWatch.getTotalTimeMillis()));

    return result;
  }

  /**
   * Create a log message.
   *
   * @param methodName A string with the method name.
   * @param methodDuration Duration of the method in milliseconds.
   * @return A string representing message.
   */
  private static String buildLogMessage(String methodName, long methodDuration) {
    StringBuilder message = new StringBuilder();
    message.append("Gintonic --> ");
    message.append(methodName);
    message.append(" --> ");
    message.append("[");
    message.append(methodDuration);
    message.append("ms");
    message.append("]");

    return message.toString();
  }
}
