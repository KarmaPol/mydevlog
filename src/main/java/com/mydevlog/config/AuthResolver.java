package com.mydevlog.config;

import com.mydevlog.config.data.UserSession;
import com.mydevlog.exception.Unauthorized;
import com.mydevlog.repository.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Slf4j
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;
    private final Appconfig appconfig;
    private final static String KEY = "VjScEchv0aitUOcPxmPMlKmGL6XtZMP4tQtsCHFtpPg=";

    // parameter에 특정 클래스 있다면 resolve Argument 실행
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String jws = webRequest.getHeader("Authorization");

        if(jws == null || jws.equals("")){
            throw new Unauthorized();
        }

        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(appconfig.getJwtKey())
                    .build()
                    .parseClaimsJws(jws);
            String userID = claims.getBody().getSubject();
            return new UserSession(Long.parseLong(userID));
        }

        catch (JwtException ex) {
            throw new Unauthorized();
        }
    }
}
