package com.bytes.ms_accounts.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
/**
 * Intercepts outgoing Feign requests to propagate internal headers.
 *
 * <p>Forwards the user Authorization token and marks calls as internal
 * for downstream customer-service validation endpoints.</p>
 */
public class FeignClientInterceptor implements RequestInterceptor {

    /**
     * Adds headers required by internal Feign calls.
     *
     * @param requestTemplate outgoing Feign request template
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // Get the current HTTP request (the one sent by the user to ms-accounts)
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        if (attributes != null) {
            // Extract the Authorization header that contains the JWT token
            String authHeader = attributes.getRequest().getHeader("Authorization");
            
            // If present, inject it into the outgoing Feign call to ms-customers
            if (authHeader != null)
                requestTemplate.header("Authorization", authHeader);
            
            // Automatically inject the X-Internal-Service header
            requestTemplate.header("X-Internal-Service", "ms-accounts");
        }
    }
}