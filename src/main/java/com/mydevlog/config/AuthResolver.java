package com.mydevlog.config;

import com.mydevlog.config.data.UserSession;
import com.mydevlog.exception.Unauthorized;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthResolver implements HandlerMethodArgumentResolver {

    // parameter에 특정 클래스 있다면 resolve Argument 실행
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String accessToken = webRequest.getHeader("Authorization");
        if (accessToken == null || accessToken.equals("")){
            throw new Unauthorized();
        }

        // DB 확인 작업
        // ...

        return new UserSession(1L);
    }
}
