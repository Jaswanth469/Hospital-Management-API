package com.java.security;


import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	SecurityUser securityUser;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String userEmail = null;
		
		String jwt = null;
		
		String header = request.getHeader("Authorization");
		
		if(header!=null && header.startsWith("Bearer ")) {
			
			jwt = header.substring(7);
			userEmail = jwtService.extractUsername(jwt);
		}
		
		try {
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			
			if(userEmail!=null && authentication == null) {
				UserDetails userDetails = securityUser.loadUserByUsername(userEmail);
				 if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {
	                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
	                            userDetails,
	                            null,
	                            userDetails.getAuthorities()
	                    );
	                    
	                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                    System.out.println(authToken);
	                    SecurityContextHolder.getContext().setAuthentication(authToken);
				 }
			}
			filterChain.doFilter(request, response);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}