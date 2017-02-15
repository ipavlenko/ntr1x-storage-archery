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
import com.ntr1x.storage.archery.model.Portal;
import com.ntr1x.storage.archery.model.Template;
import com.ntr1x.storage.archery.services.IDomainService;
import com.ntr1x.storage.archery.services.ITemplateService;
import com.ntr1x.storage.core.filters.IUserScope;
import com.ntr1x.storage.core.filters.UserScope;
import com.ntr1x.storage.core.services.IMailService;
import com.ntr1x.storage.core.services.IParamService;

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
	
	@Inject
	private ITemplateService templates;
	
	@Inject
	private IParamService params;
	
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
		String proto = rc.getHeaderString("X-Client-Proto");
		String referer = rc.getHeaderString("Referer");
		
		String host = client;
		
    	if ((host == null || host.isEmpty()) && referer != null) {
    		
    		URI uri = URI.create(referer);
    		String hostname = uri.getHost();
    		
    		int port = uri.getPort();
    		
    		host = port >= 0 ? String.format("%s:%d", hostname, port) : hostname;
    	}
    	
    	Domain domain = domains.select(null, host);
    	
    	if (domain == null) return null;
    	
    	long id = domain.getPortal().getId();
    	long scope = domain.getPortal().getId();
    	
        return new UserScope(scope)
        	.with(
    			IMailService.MailScope.class,
        		new IMailService.MailScope(
        			proto == null ? "http" : proto,
        			host,
        			domain.getPortal().getTitle(),
	    			(name) -> {
	    				Template t = templates.select(null, id, name);
	    				return new IMailService.Template(
    						t.getSender(),
							t.getSubject(),
							t.getContent()
						);
	    			},
	    			() -> params.load(null, id, Portal.ParamType.MAIL.name())
				)
    		);
	}
}