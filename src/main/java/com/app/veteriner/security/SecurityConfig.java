package com.app.veteriner.security;

import com.app.veteriner.model.ERole;
import com.app.veteriner.security.jwt.AuthJwt;
import com.app.veteriner.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Qualifier("userDetailsServiceImpl")
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthJwt unauthorizedHandler;

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}


	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(bCryptPasswordEncoder());

		return authProvider;
	}

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Set unauthorized requests exception handler
		http.cors().and().csrf().disable()
				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests()
				.antMatchers("/css/**","/**/*.js",
						"/**/*.css", "/images/**",
						"/js/**", "/registration", "/resources/static/login/**",
						"/resources/static/home/**","/auth/**", "/login/**","/registration/")
				.permitAll();
		// restrict access
		http.authorizeRequests().antMatchers( "/owner/**" ,"/home/**", "/pet","/newForm").hasAuthority(ERole.ROLE_USER.name())
				// ADMIN USER vs.
				.antMatchers(HttpMethod.DELETE, "/owners/**").hasAuthority(ERole.ROLE_ADMIN.name())
				.antMatchers(HttpMethod.DELETE, "/pets/**").hasAuthority(ERole.ROLE_ADMIN.name())
				.antMatchers(HttpMethod.PUT, "/owners/**").hasAuthority(ERole.ROLE_ADMIN.name())
				.antMatchers(HttpMethod.PUT, "/pets/**").hasAuthority(ERole.ROLE_ADMIN.name())
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login") // show my login page
				.defaultSuccessUrl("/home")
				.permitAll() // allow everyone to see login page
				.and()
				.logout().logoutUrl("/logout")
				.permitAll();

		http.authenticationProvider(authenticationProvider());

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());

	}

}
