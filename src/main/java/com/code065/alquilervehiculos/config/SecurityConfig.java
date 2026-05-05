package com.code065.alquilervehiculos.config;

import com.code065.alquilervehiculos.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                /*
                 * CSRF se mantiene activo para la parte web, pero se desactiva
                 * para la API REST, ya que las peticiones API se protegerán
                 * mediante token JWT.
                 */
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )
                /*
                 * La política STATELESS indica que la API no debe depender de
                 * sesiones HTTP para autenticar usuarios.
                 *
                 * La parte web puede seguir usando formulario de login.
                 */
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                /*
                 * Definición de permisos por ruta.
                 */
                .authorizeHttpRequests(auth -> auth
                        /*
                         * Rutas pública de la aplicación web.
                         */
                        .requestMatchers(
                                "/",
                                "/login",
                                "/registro"
                        ).permitAll()

                        /*
                         * Recurso estáticos públicos
                         */
                        .requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "webjars/**"
                        ).permitAll()

                        /*
                         * Swagger y OpenAPI.
                         * Deben estar accesibles para poder consultar
                         * la documentación de la API.
                         */
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        /*
                         * Endpoint de login de la API.
                         * Debe ser público, porque todavía no existe token.
                         */
                        .requestMatchers("/api/auth/**").permitAll()

                        /*
                         * Endpoints públicos de consulta de la API REST
                         */
                        .requestMatchers("/api/clientes/**").permitAll()
                        .requestMatchers("/api/vehiculos/**").permitAll()
                        .requestMatchers("/api/alquileres/**").permitAll()

                        /*
                         * Endpoints securizados de la API REST.
                         * Solo usaurios con rol ADMIN podrán acceder
                         */
                        .requestMatchers("/api/admin/**"). hasRole("ADMIN")

                        /*
                         * Rutas web protegidas del Producto 3
                         */
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")

                        /*
                         * Cualquier otra ruta requiere autenticación.
                         */
                        .anyRequest().authenticated()
                )

                /*
                 * Login web del Producto 3.
                 * Este login sirve para las vistas HTML con Thymeleaf.
                 */
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )

                /*
                 * Logout de la aplicación web.
                 */
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                /*
                 * Añadimos el filtro JWT antes del filtro estándar de
                 * usuario y contraseña.
                 */
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .exceptionHandling(exception -> exception
                       .accessDeniedPage("/error/403")
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
