package com.bytes.ms_accounts.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // Obtener la petición HTTP actual (la que hizo el usuario a ms-accounts)
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        if (attributes != null) {
            // Extraer el header "Authorization" que contiene el Token JWT
            String authHeader = attributes.getRequest().getHeader("Authorization");
            
            // Si existe, inyectarlo en la llamada saliente de Feign hacia ms-customers
            if (authHeader != null)
                requestTemplate.header("Authorization", authHeader);
            
            // Inyectar automáticamente el X-Internal-Service
            requestTemplate.header("X-Internal-Service", "ms-accounts");
        }
    }
}