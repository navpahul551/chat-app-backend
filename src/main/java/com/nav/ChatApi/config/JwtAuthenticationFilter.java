package com.nav.ChatApi.config;

import com.nav.ChatApi.repositories.UserRepository;
import com.nav.ChatApi.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            initBeans(request);
            String jwtToken = getJwtToken(request.getHeader("Authorization"));

            if(jwtToken != null && jwtTokenProvider.validateToken(jwtToken)){
                userService.authenticateUser(jwtToken);
            }
        } catch(Exception e){
            e.printStackTrace();
            LOGGER.error("Unable to authenticate user using jwt token");
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtToken(String header){
        if(header != null && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }

    private void initBeans(HttpServletRequest request){
        ServletContext servletContext = request.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        jwtTokenProvider = webApplicationContext.getBean(JwtTokenProvider.class);
        userRepository = webApplicationContext.getBean(UserRepository.class);
        userService = webApplicationContext.getBean(UserService.class);
    }
}
