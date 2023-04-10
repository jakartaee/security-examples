# Proposal to inject built-in HttpAuthenticationMechanism beans

This is to make it possible to choose programmatically which of the built-in authentication mechanisms should be used based on configuration or user input. It would be possible to inject one or more mechanisms configured using the existing definition annotations (e.g. `@BasicAuthenticationMechanismDefinition`) and delegate authentication to them from a custom HttpAuthenticationMechanism bean.

## Example

```
@ApplicationScoped
public class CustomAuth implements HttpAuthenticationMechanism {

    @Inject
    @FormAuthenticationMechanismDefinition(loginToContinue = @LoginToContinue(loginPage = "/login.xhtml"))
    HttpAuthenticationMechanism formAuthentication;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext context) throws AuthenticationException {
        return formAuthentication.validateRequest(request, response, context);
    }

}
```

## Use cases

### Select which OpenID provider to use

When an application wants to support multiple OpenID providers (Google, Auth0, a custom provider). The user should be able to select which provider they want to use to authenticate. E.g. if they already have a Google account, they would select Google as their provider. Their selection can be implemented for example as a cookie or a query parameter in the login action URL. A custom auth mechanism would detect this selection from the request object and delegate to one of the injected built-in OpenID mechanisms.

### Select between OpenID authentication and form-based (user/password) authentication

When an application wants to support OpenID on top of it's own form/based authentication mechanism. Users already have accounts in the system, can log in using user/password. Additionally, they have an option to connect their OpenID account with their existing account and log in using the OpenID acocunt besides using user/password.

### Multitenant applications

When the application supports multiple tenants (customers, user groups, etc.), it's desirable that each group of users using a different authentication mechanism. If at least one of the mechanisms uses a built-in mechanism (e.g. form-based or OpenID-Connect), these mechanisms can be simply injected into a custom mechanism and reused for the specific tenant. 

A simple multitenancy with a fixed number of tenants can be implemented simply by injecting an authentication mechanism per tenant.

Dynamic multitenancy (with configurable number of tenants) is not possible using this approach and is out of scope of this proposal. Dynamic multitenancy would require some API (e.g. a CDI bean) which would provide configuration based on the current tenant. It would also be required that the built-in mechanisms, which are application-scoped, support reconfiguration per session/request.