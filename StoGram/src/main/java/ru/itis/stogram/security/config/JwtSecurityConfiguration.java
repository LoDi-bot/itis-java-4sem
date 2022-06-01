package ru.itis.stogram.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.itis.stogram.security.filters.JwtTokenAuthenticationFilter;
import ru.itis.stogram.security.filters.JwtTokenAuthorizationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JwtSecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String LOGIN_FILTER_PROCESSES_URL = "/login";

    @Value("${jwt.secretKey}")
    private String secretKey;

    private final UserDetailsService accountUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public JwtSecurityConfiguration(UserDetailsService accountUserDetailsService,
                                    PasswordEncoder passwordEncoder,
                                    ObjectMapper objectMapper) {
        this.accountUserDetailsService = accountUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
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
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter =
                new JwtTokenAuthenticationFilter(authenticationManagerBean(), objectMapper, secretKey);
        jwtTokenAuthenticationFilter.setFilterProcessesUrl(LOGIN_FILTER_PROCESSES_URL);

        JwtTokenAuthorizationFilter jwtTokenAuthorizationFilter =
                new JwtTokenAuthorizationFilter(objectMapper, secretKey);

        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilter(jwtTokenAuthenticationFilter);
        httpSecurity.addFilterBefore(jwtTokenAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.authorizeRequests()
                .antMatchers("/signUp").permitAll()
                .antMatchers("/confirmation/**").permitAll()
                .antMatchers(LOGIN_FILTER_PROCESSES_URL + "/**").permitAll();
    }

}
