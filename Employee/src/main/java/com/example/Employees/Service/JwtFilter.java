package com.example.Employees.Service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.Employees.Util.JwtUtils;

@Component
public class JwtFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUtils jwtUtil;
	
	@Autowired
	private UserService userservice;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authorizationHeader= request.getHeader("Authorization");
		
		String username=null;
		String jwtToken =null;
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwtToken= authorizationHeader.substring(7);
			username=jwtUtil.extractUsername(jwtToken);
		}
		
		if (username != null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails UserDetails=userservice.loadUserByUsername(username);
			
			if (jwtUtil.validateToken(jwtToken, UserDetails)) {
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
					new UsernamePasswordAuthenticationToken(UserDetails, null, UserDetails.getAuthorities());
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource()
					.buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}

