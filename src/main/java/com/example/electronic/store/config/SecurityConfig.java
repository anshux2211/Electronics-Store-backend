package com.example.electronic.store.config;

import com.example.electronic.store.security.JwtAuthenticationEntryPoint;
import com.example.electronic.store.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

import static com.example.electronic.store.config.AppConstants.ADMIN;
import static com.example.electronic.store.config.AppConstants.NORMAL;


@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwt_filer;
    @Autowired
    private JwtAuthenticationEntryPoint jwt_entryPoint;

    private final String[] public_urls={
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {

        security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());

        security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                CorsConfiguration corsConfiguration=new CorsConfiguration();
//                corsConfiguration.addAllowedOrigin("http://localhost:4280");
//                corsConfiguration.setAllowedOrigins(List.of("http://localhost:4280","http://localhost:8540"));  // Allow Multiple Origins
                corsConfiguration.setAllowedOriginPatterns(List.of("*"));  // Allow All origins

                corsConfiguration.setAllowedMethods(List.of("*"));
                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.setAllowedHeaders(List.of("*"));
                corsConfiguration.setMaxAge(3000L);

                return corsConfiguration;
            }
        }));

        security.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());




        security.authorizeHttpRequests(request->{
            request.requestMatchers(public_urls).permitAll()
                    .requestMatchers("/orders/update/**").hasRole(ADMIN)
                    .requestMatchers("/orders/create/**").hasRole(NORMAL)
                    .requestMatchers("/orders/users/**").hasAnyRole(NORMAL,ADMIN)
                    .requestMatchers("/orders/remove/**").hasAnyRole(NORMAL,ADMIN)
                    .requestMatchers("/orders/**").hasRole(ADMIN)

                    .requestMatchers(HttpMethod.DELETE,"/product/**").hasRole(ADMIN)
                    .requestMatchers(HttpMethod.PUT,"/product/**").hasRole(ADMIN)
                    .requestMatchers(HttpMethod.POST,"/product/**").hasRole(ADMIN)

                    .requestMatchers("/users/get-all/**").hasRole(ADMIN)
                    .requestMatchers("/users/search/**").hasRole(ADMIN)
                    .requestMatchers("/users/phone/**").hasRole(ADMIN)
                    .requestMatchers("/users/mail/**").hasRole(ADMIN)
                    .requestMatchers(HttpMethod.POST,"/users/image/**").hasAnyRole(NORMAL)
                    .requestMatchers(HttpMethod.DELETE,"/users/**").hasAnyRole(ADMIN,NORMAL)
                    .requestMatchers(HttpMethod.GET,"/users/**").hasAnyRole(ADMIN,NORMAL)
                    .requestMatchers(HttpMethod.PUT,"/users/**").hasAnyRole(ADMIN,NORMAL)

                    .requestMatchers(HttpMethod.GET,"/category/**").permitAll()
                    .requestMatchers("/category/**").hasRole(ADMIN)

                    .requestMatchers("/cart/**").hasRole(NORMAL)

                    .requestMatchers("/auth/verify/**").hasAnyRole(NORMAL,ADMIN)

                    .anyRequest().permitAll();
        });
//        security.httpBasic(Customizer.withDefaults());

        security.exceptionHandling(ex->ex.authenticationEntryPoint(jwt_entryPoint));
        security.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        security.addFilterBefore(jwt_filer, UsernamePasswordAuthenticationFilter.class);
        return security.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}
