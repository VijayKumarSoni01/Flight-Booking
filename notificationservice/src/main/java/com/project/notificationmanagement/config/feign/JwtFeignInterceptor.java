package com.project.notificationmanagement.config.feign;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtFeignInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void apply(RequestTemplate template) {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (!(requestAttributes instanceof ServletRequestAttributes servletRequestAttributes)) {
            return;
        }

        HttpServletRequest request = servletRequestAttributes.getRequest();

        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (!StringUtils.hasText(authorizationHeader)) {
            return;
        }

        if (!authorizationHeader.startsWith(BEARER_PREFIX)) {
            return;
        }

        template.header(AUTHORIZATION, authorizationHeader);
    }
}