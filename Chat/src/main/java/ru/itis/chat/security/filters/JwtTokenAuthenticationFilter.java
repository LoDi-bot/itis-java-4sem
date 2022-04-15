package ru.itis.chat.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.itis.chat.dto.SignInForm;
import ru.itis.chat.exceptions.ValidationException;
import ru.itis.chat.models.Account;
import ru.itis.chat.security.details.AccountUserDetails;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@Slf4j
public class JwtTokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper;
    private final String secretKey;
    private final Validator validator;

    public JwtTokenAuthenticationFilter(AuthenticationManager manager, ObjectMapper objectMapper, String secretKey, Validator validator) {
        super(manager);
        this.objectMapper = objectMapper;
        this.secretKey = secretKey;
        this.validator = validator;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            SignInForm signInForm = objectMapper.readValue(request.getReader(), SignInForm.class);

            Set<ConstraintViolation<SignInForm>> violations = validator.validate(signInForm);
            if(!violations.isEmpty()) {
                throw new ValidationException(violations.stream().findFirst().get().getMessage());
            }

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
                                            Authentication authResult) throws IOException {
        AccountUserDetails userDetails = (AccountUserDetails) authResult.getPrincipal();
        Account account = userDetails.getAccount();

        String token = JWT.create()
                .withSubject(account.getId().toString())
                .withClaim("email", account.getEmail())
                .withClaim("role", account.getRole().toString())
                .withClaim("state", account.getState().toString())
                .sign(Algorithm.HMAC256(secretKey));

        objectMapper.writeValue(response.getWriter(), Collections.singletonMap("token", token));
    }

}
