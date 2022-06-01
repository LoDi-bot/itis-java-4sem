package ru.itis.stogram.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.itis.stogram.dto.SignInForm;
import ru.itis.stogram.models.Account;
import ru.itis.stogram.repositories.AccountsRepository;
import ru.itis.stogram.security.details.AccountUserDetails;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JwtTokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String TOKEN = "token";

    private final ObjectMapper objectMapper;
    private final String secretKey;

    public JwtTokenAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper, String secretKey) {
        super(authenticationManager);
        this.objectMapper = objectMapper;
        this.secretKey = secretKey;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            SignInForm signInForm = objectMapper.readValue(request.getReader(), SignInForm.class);

            log.info("Attempt authentication - email {}", signInForm.getEmail());

            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(signInForm.getEmail(), signInForm.getPassword());

            return super.getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException {
        AccountUserDetails userDetails = (AccountUserDetails) authentication.getPrincipal();
        Account account = userDetails.getAccount();

        String token = JWT.create()
                .withSubject(account.getId().toString())
                .withClaim("nick", account.getNick())
                .withClaim("email", account.getEmail())
                .withClaim("role", account.getRole().toString())
                .sign(Algorithm.HMAC256(secretKey));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), Collections.singletonMap(TOKEN, token));
    }

}
