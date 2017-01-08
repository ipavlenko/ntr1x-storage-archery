package com.ntr1x.storage.archery.filters;

import java.net.URI;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import com.ntr1x.storage.archery.model.Domain;
import com.ntr1x.storage.archery.services.IDomainService;
import com.ntr1x.storage.security.filters.IUserScope;
import com.ntr1x.storage.security.filters.UserScope;

import io.swagger.models.HttpMethod;

@Component
@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION - 1)
public class PortalScopeFilter implements ContainerRequestFilter {

	@Inject
	private HttpServletRequest request;
	
	@Inject
	private IDomainService domains;
	
	@Override
	@Transactional
	public void filter(ContainerRequestContext rc) { 

		if (HttpMethod.OPTIONS.equals(rc.getMethod())) {
			return;
		}
		
		UserScope scope = setupScope(rc);
		
		request.setAttribute(IUserScope.class.getName(), scope);
	}
	
	private UserScope setupScope(ContainerRequestContext rc) {
		
		String client = rc.getHeaderString("X-Client-Host");
		String referer = rc.getHeaderString("Referer");
		
		String host = client;
		
    	if ((host == null || host.isEmpty()) && referer != null) {
    		
    		URI uri = URI.create(referer);
    		String hostname = uri.getHost();
    		
    		int port = uri.getPort();
    		
    		host = port >= 0 ? String.format("%s:%d", hostname, port) : hostname;
    	}
    	
    	Domain domain = domains.select(null, host);
    	
        return new UserScope(domain == null ? -1 : domain.getPortal().getId());
	}
}