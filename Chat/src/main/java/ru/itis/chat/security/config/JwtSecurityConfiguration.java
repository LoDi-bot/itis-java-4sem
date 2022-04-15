package ru.itis.chat.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.itis.chat.security.filters.JwtTokenAuthenticationFilter;
import ru.itis.chat.security.filters.JwtTokenAuthorizationFilter;

import javax.validation.Validator;

@EnableWebSecurity
public class JwtSecurityConfiguration extends WebSecurityConfigurerAdapter {
    public static final String LOGIN_FILTER_PROCESSES_URL = "/login";

    @Value("${jwt.secretKey}")
    private String secretKey;

    private final UserDetailsService accountUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Autowired
    public JwtSecurityConfiguration(PasswordEncoder passwordEncoder,
                                    UserDetailsService accountUserDetailsService,
                                    ObjectMapper objectMapper,
                                    Validator validator) {
        this.passwordEncoder = passwordEncoder;
        this.accountUserDetailsService = accountUserDetailsService;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(accountUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtTokenAuthenticationFilter authenticationFilter =
                new JwtTokenAuthenticationFilter(authenticationManagerBean(), objectMapper, secretKey, validator);

        JwtTokenAuthorizationFilter authorizationFilter = new JwtTokenAuthorizationFilter(objectMapper, secretKey);

        authenticationFilter.setFilterProcessesUrl(LOGIN_FILTER_PROCESSES_URL);

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilter(authenticationFilter);
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .antMatchers(LOGIN_FILTER_PROCESSES_URL + "/**").permitAll();
    }

}
