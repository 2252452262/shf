package com.atguigu.interceptor;

import com.alibaba.fastjson.JSON;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("USER");

        if (user == null) {
            Result<Object> result = Result.build(null, ResultCodeEnum.LOGIN_AUTH);
            JSON.writeJSONString(response.getWriter(), result);
            return false;
        }
        return true;
    }
}
