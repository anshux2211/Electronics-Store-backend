package com.lcwd.electronic.store.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private Logger logger=LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Autowired
    private JwtHelper jwt_helper;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeader=request.getHeader("Authorization");
        logger.info("RequestHeader: "+requestHeader);

        String token=null;
        String username=null;
        if(requestHeader!=null && requestHeader.startsWith("Bearer")){
            token=requestHeader.substring(7);

            try{
                username= jwt_helper.getUsernameFromToken(token);
                logger.info("Username: "+username);
            }catch(IllegalArgumentException ex){
                logger.info("IllegalArgumentException occurred while fetching username!! "+ex.getMessage());
            }
            catch (ExpiredJwtException ex){
                logger.info("Token is expired!! "+ex.getMessage());
            }
            catch(MalformedJwtException ex){
                logger.info("Some changes has been done to the token !! "+ex.getMessage());
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
        else{
            logger.info("Invalid requestHeader or it doesn't starts with Bearer");
        }

        logger.info("UserName: {}",username);

        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails= userDetailsService.loadUserByUsername(username);
            if(username.equals(userDetails.getUsername()) && !jwt_helper.isTokenExpired(token)){
                UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request,response);
    }
}
