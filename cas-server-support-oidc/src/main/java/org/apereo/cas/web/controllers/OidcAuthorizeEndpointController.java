package org.apereo.cas.web.controllers;

import org.apereo.cas.OidcConstants;
import org.apereo.cas.support.oauth.OAuthConstants;
import org.apereo.cas.support.oauth.web.OAuth20AuthorizeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is {@link OidcAuthorizeEndpointController}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
public class OidcAuthorizeEndpointController extends OAuth20AuthorizeController {
    
    @RequestMapping(value = '/' + OidcConstants.BASE_OIDC_URL + '/' + OAuthConstants.AUTHORIZE_URL, method = RequestMethod.GET)
    @Override
    public ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return super.handleRequestInternal(request, response);
    }
}
