package com.atguigu.aop;

import com.atguigu.util.IpUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author rzcl
 * @description
 */
@Aspect
@Component
public class ControllerAspect {

    private Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    @Pointcut("execution(* com.atguigu.controller..*.*(..))")
    public void pointcut() {
    }


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if(!(authentication.getPrincipal() instanceof SecurityUser)) return pjp.proceed();
//        SecurityUser securityUser = (SecurityUser)authentication.getPrincipal();
        //后续登录实现了，补充
        Long userId = 1L;//securityUser.getId();
        String userName = "admin";//securityUser.getUsername();
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String ip = IpUtil.getIpAddress(request);
        String classMethod = pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();

        String param = "";
        Object[] args = pjp.getArgs();
        for (int i = 0; i < args.length; i++) {
            try {
                param += "param" + (i + 1) + ":" + args[i] + ",";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long startTime = System.currentTimeMillis();
        Object value=null;
        try {
            value = pjp.proceed();
        } catch (Throwable throwable) {
            throwable.getStackTrace();
        } finally {
            long endTime = System.currentTimeMillis();
            long time = endTime - startTime;
            String logs = userId + "|" + userName + "|" + url + "|" + method + "|" + ip + "|" + classMethod + "|" + param + "|" + time;
            logger.info("request log:" + logs);
        }
        return value;
    }
}
