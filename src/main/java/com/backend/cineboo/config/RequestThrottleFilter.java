package com.backend.cineboo.config;



import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RequestThrottleFilter implements Filter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>(); // Store buckets per IP
    private final  int MAX_REQUESTS_PER_SECOND = 10; // Max requests allowed per second
    //Since frontend has several operation that involves a load of api calls at once

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        if(httpRequest.getMethod().equalsIgnoreCase("GET")){
            filterChain.doFilter(servletRequest, servletResponse);//Allow GET Request, for now
            return;
        }
        String clientIp = getClientIP(httpRequest); // Identify the client IP
        Bucket bucket = buckets.computeIfAbsent(clientIp, this::createNewBucket);

        if (bucket.tryConsume(1)) { // Consume a token if available
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            // Reject the request
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpResponse.getWriter().write("Too many requests. Try again later.");
        }
    }

    private Bucket createNewBucket(String key) {
        // Define a bucket with refill rate: 5 tokens per second
        return Bucket.builder()
                .addLimit(Bandwidth.classic(MAX_REQUESTS_PER_SECOND, Refill.intervally(MAX_REQUESTS_PER_SECOND, Duration.ofSeconds(1))))
                .build();
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0]; // Handle proxies
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}