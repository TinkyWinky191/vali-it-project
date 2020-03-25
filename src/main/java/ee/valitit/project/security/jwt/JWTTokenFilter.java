package ee.valitit.project.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.valitit.project.exception.ExceptionsResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class JWTTokenFilter extends GenericFilterBean {

    private JWTTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch(UsernameNotFoundException | JWTAuthenticationException exc) {
            servletResponse = handleResponseIfExceptionAppears(servletResponse, exc);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private ServletResponse handleResponseIfExceptionAppears(ServletResponse servletResponse, Exception exc) throws IOException {
        HttpServletResponse resp = ((HttpServletResponse)servletResponse);
        resp.setStatus(HttpStatus.FORBIDDEN.value());
        resp.setContentType( "application/json" );
        resp.setCharacterEncoding( "UTF-8" );
        String excJson = new ObjectMapper().writeValueAsString(new ExceptionsResponse(
                HttpStatus.FORBIDDEN.value(),
                exc.getMessage() + (exc instanceof UsernameNotFoundException ? " user not found!" : ""),
                System.currentTimeMillis()));
        resp.getWriter().write(excJson);
        resp.flushBuffer();
        return resp;
    }

}
