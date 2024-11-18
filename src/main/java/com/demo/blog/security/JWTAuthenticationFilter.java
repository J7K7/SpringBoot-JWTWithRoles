package com.demo.blog.security;

<<<<<<< HEAD
import com.demo.blog.users.UserEntity;
import com.demo.blog.users.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTAuthenticationManager jwtAuthenticationManager;
    private final JWTService jwtService;
    private final UsersService usersService;

    public JWTAuthenticationFilter(JWTAuthenticationManager jwtAuthenticationManager, JWTService jwtService, UsersService usersService) {
        this.jwtAuthenticationManager = jwtAuthenticationManager;
        this.jwtService = jwtService;
        this.usersService = usersService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {

            final String authHeader = request.getHeader("Authorization");

            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                filterChain.doFilter(request, response);
                return;
            }

            final String jwt = authHeader.substring(7);
            final Long userId = jwtService.retriveUserId(jwt);
            final UserEntity user = usersService.getUser(userId);

            // Convert the request into an Authentication object using the converter
            var authentication = jwtAuthenticationManager.authenticate(
                    new JWTAuthenticationConverter().convert(request)
            );

            // Set the authentication in the SecurityContext if successful
            if (authentication != null && authentication.isAuthenticated()) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        usersService.loadUserByUsername(user.getUsername()),
                        null,
                        usersService.getAuthorities(userId)
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        } catch (Exception ex) {
            // Optionally, handle exceptions (e.g., log or return unauthorized response)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: " + ex.getMessage());
            return;
        }

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
=======
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JWTAuthenticationFilter extends AuthenticationFilter {
    private JWTAuthenticationManager jwtAuthenticationManager;

    public JWTAuthenticationFilter(JWTAuthenticationManager jwtAuthenticationManager) {
        super(jwtAuthenticationManager, new JWTAuthenticationConverter());
        this.setSuccessHandler(((request, response, authentication) -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }));
>>>>>>> 5f1df2d0576ba6683e8c0efc4dff5fb4abde8dbd
    }
}
