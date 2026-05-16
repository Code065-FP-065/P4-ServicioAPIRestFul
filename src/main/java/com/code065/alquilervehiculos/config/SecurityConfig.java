package com.code065.alquilervehiculos.config;

import com.code065.alquilervehiculos.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Configuración principal de seguridad de la aplicación.
 *
 * En este proyecto conviven dos tipos de acceso:
 *
 * 1. Aplicación web con Thymeleaf:
 *    - Usa login por formulario.
 *    - Usa sesión HTTP.
 *    - Si el usuario no está autenticado, redirige a /login.
 *
 * 2. API REST:
 *    - Usa rutas /api/**.
 *    - Usa token JWT.
 *    - No usa sesión HTTP.
 *    - Si el usuario no está autenticado, devuelve 401 Unauthorized.
 *
 * Por este motivo se configuran dos SecurityFilterChain separadas.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Constructor con inyección del filtro JWT.
     *
     * @param jwtAuthenticationFilter filtro encargado de leer y validar el token JWT.
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Matcher propio para identificar todas las rutas de la API REST.
     *
     * Usamos request.getRequestURI() porque nos permite comprobar la URL real
     * recibida por Tomcat.
     *
     * Ejemplo:
     * - /api/clientes          -> API
     * - /api/admin/clientes    -> API
     * - /login                 -> No API
     * - /clientes              -> No API
     *
     * @return matcher que devuelve true cuando la petición pertenece a /api/**
     */
    private RequestMatcher apiRequestMatcher() {
        return request -> {
            String contextPath = request.getContextPath();
            String requestUri = request.getRequestURI();

            return requestUri.startsWith(contextPath + "/api/");
        };
    }

    /**
     * Cadena de seguridad específica para la API REST.
     *
     * Esta cadena solo se aplica a las rutas /api/**.
     *
     * Características:
     * - No usa sesión.
     * - No usa login por formulario.
     * - No redirige a /login.
     * - Usa JWT.
     * - Devuelve 401 si no hay autenticación.
     * - Devuelve 403 si hay autenticación, pero no permisos suficientes.
     *
     * @param http configuración HTTP de Spring Security.
     * @return cadena de seguridad para la API REST.
     * @throws Exception si se produce un error de configuración.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                /*
                 * Esta cadena solo debe aplicarse a rutas /api/**.
                 */
                .securityMatcher(apiRequestMatcher())

                /*
                 * En una API REST con JWT no se utiliza CSRF.
                 *
                 * CSRF tiene sentido principalmente en formularios web con sesión.
                 * En una API con token Bearer, cada petición debe autenticarse
                 * mediante el token enviado en la cabecera Authorization.
                 */
                .csrf(csrf -> csrf.disable())

                /*
                 * La API debe ser stateless.
                 *
                 * Esto significa que Spring Security no debe crear ni usar sesiones
                 * HTTP para autenticar peticiones REST.
                 *
                 * Así evitamos que una cookie JSESSIONID permita acceder a la API.
                 */
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                /*
                 * Desactivamos mecanismos propios de una aplicación web.
                 *
                 * En la API no queremos:
                 * - Guardar peticiones para redirigir después del login.
                 * - Mostrar formulario de login.
                 * - Autenticación Basic.
                 * - Logout web basado en sesión.
                 */
                .requestCache(requestCache -> requestCache.disable())
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .logout(logout -> logout.disable())

                /*
                 * Tratamiento de errores específico para API REST.
                 *
                 * Importante:
                 * No usamos response.sendError(), porque sendError() puede provocar
                 * un dispatch interno a /error. Como /error no empieza por /api/,
                 * podría acabar entrando en la cadena web y redirigir a /login.
                 *
                 * Por eso escribimos directamente el estado HTTP y el JSON.
                 */
                .exceptionHandling(exception -> exception

                        /*
                         * Caso 401:
                         * El usuario intenta acceder a un endpoint protegido sin
                         * enviar un token JWT válido.
                         */
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("""
                                    {
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "No autorizado. Debes enviar un token JWT válido."
                                    }
                                    """);
                        })

                        /*
                         * Caso 403:
                         * El usuario está autenticado, pero no tiene el rol necesario
                         * para acceder al recurso.
                         */
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("""
                                    {
                                      "status": 403,
                                      "error": "Forbidden",
                                      "message": "Acceso denegado. No tienes permisos suficientes."
                                    }
                                    """);
                        })
                )

                /*
                 * Reglas de autorización de la API REST.
                 */
                .authorizeHttpRequests(auth -> auth

                        /*
                         * Endpoint público para autenticarse y obtener el token JWT.
                         */
                        .requestMatchers("/api/auth/**").permitAll()

                        /*
                         * Endpoints públicos de consulta.
                         *
                         * Estos endpoints pueden consultarse sin token.
                         */
                        .requestMatchers("/api/clientes/**").permitAll()
                        .requestMatchers("/api/vehiculos/**").permitAll()
                        .requestMatchers("/api/alquileres/**").permitAll()

                        /*
                         * Endpoints protegidos de administración.
                         *
                         * hasRole("ADMIN") comprueba internamente ROLE_ADMIN.
                         */
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        /*
                         * Cualquier otra ruta API requiere autenticación.
                         */
                        .anyRequest().authenticated()
                )

                /*
                 * Añadimos el filtro JWT antes del filtro estándar de usuario/contraseña.
                 *
                 * Este filtro leerá la cabecera:
                 *
                 * Authorization: Bearer TOKEN
                 *
                 * Si el token es válido, Spring Security considerará autenticado
                 * al usuario para esa petición.
                 */
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    /**
     * Cadena de seguridad para la aplicación web con Thymeleaf.
     *
     * Esta cadena se aplica solamente a las rutas que NO pertenecen a /api/**.
     *
     * Características:
     * - Usa login por formulario.
     * - Usa sesión HTTP.
     * - Redirige a /login cuando el usuario no está autenticado.
     *
     * @param http configuración HTTP de Spring Security.
     * @return cadena de seguridad para la aplicación web.
     * @throws Exception si se produce un error de configuración.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                /*
                 * Esta cadena solo debe aplicarse a rutas que NO son API.
                 *
                 * Así evitamos que una petición /api/** termine usando el login web
                 * y devuelva 302 hacia /login.
                 */
                .securityMatcher(request -> !apiRequestMatcher().matches(request))

                /*
                 * Reglas de autorización para la parte web.
                 */
                .authorizeHttpRequests(auth -> auth

                        /*
                         * Rutas públicas web.
                         */
                        .requestMatchers(
                                "/",
                                "/login",
                                "/registro"
                        ).permitAll()

                        /*
                         * Recursos estáticos públicos.
                         */
                        .requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/img/**",
                                "/webjars/**"
                        ).permitAll()

                        /*
                         * Swagger y OpenAPI.
                         *
                         * Se dejan públicos para consultar y probar la documentación.
                         */
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        /*
                         * Rutas web protegidas por roles.
                         */
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")

                        /*
                         * Cualquier otra ruta web requiere autenticación.
                         */
                        .anyRequest().authenticated()
                )

                /*
                 * Login web mediante formulario.
                 *
                 * Este comportamiento solo se aplica a la parte web,
                 * no a la API REST.
                 */
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )

                /*
                 * Logout web basado en sesión.
                 */
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                /*
                 * Página de error para accesos denegados en la parte web.
                 *
                 * Si no tienes una vista /error/403, puedes eliminar este bloque.
                 */
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/error/403")
                );

        return http.build();
    }

    /**
     * AuthenticationManager utilizado por el endpoint /api/auth/login.
     *
     * Este bean permite validar las credenciales recibidas desde la API.
     *
     * @param authenticationConfiguration configuración de autenticación.
     * @return AuthenticationManager de Spring Security.
     * @throws Exception si se produce un error al obtener el AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Codificador de contraseñas BCrypt.
     *
     * Se usa para comparar la contraseña introducida con el hash guardado
     * en la base de datos.
     *
     * @return codificador BCrypt.
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}