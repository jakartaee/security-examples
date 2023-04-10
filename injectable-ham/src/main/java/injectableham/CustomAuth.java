package injectableham;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;

@ApplicationScoped
public class CustomAuth implements HttpAuthenticationMechanism {

    @Inject
    @FormAuthenticationMechanismDefinition(loginToContinue = @LoginToContinue(loginPage = "/login.xhtml"))
    HttpAuthenticationMechanism formAuthentication;

    @Inject
    @BasicAuthenticationMechanismDefinition
    HttpAuthenticationMechanism basicAuthentication;

    private HttpAuthenticationMechanism getMechanism(HttpServletRequest request) {
        if ("authType".equals(request.getParameter("form")) ) {
            return formAuthentication;
        } else {
            return basicAuthentication;
        }
        
    }
    
    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {
        return getMechanism(request).validateRequest(request, response, httpMessageContext);
    }

    @Override
    public AuthenticationStatus secureResponse(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {
        return getMechanism(request).secureResponse(request, response, httpMessageContext);
    }

    @Override
    public void cleanSubject(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) {
        getMechanism(request).cleanSubject(request, response, httpMessageContext);
    }
    
}
