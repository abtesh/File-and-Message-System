package com.LIB.MessagingSystem.Config;

import com.LIB.MessagingSystem.Service.Impl.JwtService;
import com.LIB.MessagingSystem.Service.Impl.UsersService;
import com.LIB.MessagingSystem.filters.AuthenticationFilter;
import com.LIB.MessagingSystem.filters.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${ldap.username}")
    private String username;
    @Value("${ldap.password}")
    private String password;
    @Value("${ldap.url}")
    private String url;
    @Value("${ldap.search-base}")
    private String baseSearch;
    private final UsersService usersService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public JwtService getJwtService() {
        return new JwtService();
    }
    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setUrl(url);
        ldapContextSource.setUserDn(username);
        ldapContextSource.setPassword(password);


        final Map<String, Object> envProps = new HashMap<>();
        envProps.put("java.naming.ldap.attributes.binary","objectGUID");
        ldapContextSource.setBaseEnvironmentProperties(envProps);

        return ldapContextSource;
    }

    @Bean
    AuthenticationManager authManager(BaseLdapPathContextSource source) {
        LdapBindAuthenticationManagerFactory factory = new LdapBindAuthenticationManagerFactory(source);
        factory.setUserSearchBase(baseSearch);
        factory.setUserSearchFilter("userPrincipalName={0}");
        return factory.createAuthenticationManager();
    }

    private static final String[] WHITE_LIST_URL = {
            "/api/login",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = authManager(contextSource());

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .addFilterBefore(authenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager){
        return new AuthenticationFilter(authenticationManager,ldapTemplate(), getJwtService(),usersService,baseSearch,objectMapper());
    }
}
