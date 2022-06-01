package ru.itis.stogram.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.stogram.security.config.JwtSecurityConfiguration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtTokenAuthorizationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final String secretKey;

    @SuppressWarnings("NullableProblems")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(JwtSecurityConfiguration.LOGIN_FILTER_PROCESSES_URL)
                || request.getRequestURI().equals("/signUp")
                || request.getRequestURI().startsWith("/confirmation")
                || request.getRequestURI().matches("/avatar/[0-9]+")
                || request.getRequestURI().matches("/post/[0-9]+/video")
                || request.getRequestURI().matches("/post/[0-9]+/thumbnail")) {
            filterChain.doFilter(request, response);
        } else {
            String tokenHeader = request.getHeader("Authorization");

            if (tokenHeader == null) {
                sendForbidden(response, "Authorization token is missing");
            } else if (!tokenHeader.startsWith("Bearer ")) {
                sendForbidden(response, "Wrong token format");
            } else {
                String token = tokenHeader.substring("Bearer ".length());
                try {
                    DecodedJWT decodedJWT = JWT
                            .require(Algorithm.HMAC256(secretKey))
                            .build()
                            .verify(token);

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(token, null,
                                    Collections.singletonList(
                                            new SimpleGrantedAuthority(decodedJWT.getClaim("role").asString())));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (JWTVerificationException e) {
                    sendForbidden(response, "Invalid token");
                }
            }
        }
    }

    private void sendForbidden(HttpServletResponse response, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        objectMapper.writeValue(response.getWriter(), Collections.singletonMap("error", message));
    }

}
