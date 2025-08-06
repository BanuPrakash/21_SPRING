package com.adobe.rentalapp.api;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter("/api/**")
public class LogInterceptor implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // do logging
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
