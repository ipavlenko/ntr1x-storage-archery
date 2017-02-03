package com.ntr1x.storage.archery.resources;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.ntr1x.storage.archery.services.IPortalService;
import com.ntr1x.storage.archery.services.IPortalService.PortalContext;
import com.ntr1x.storage.archery.services.IPortalService.PortalPull;
import com.ntr1x.storage.core.filters.IUserScope;
import com.ntr1x.storage.security.filters.IUserPrincipal;

import io.swagger.annotations.Api;

@Api("Archery")
@Component
@Path("/archery/context")
@PermitAll
public class ContextResource {
	
	@Inject
    private IPortalService portals;
    
    @Inject
    private Provider<IUserPrincipal> principal;
	
    @Inject
    private Provider<IUserScope> scope;
    
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public PortalContext context() {
    	
		// Portal produces scope
        PortalPull pull = portals.pull(null, scope.get().getId());
    	
        return new PortalContext(
    		principal.get().getUser(),
			pull.portal,
			pull.content
		);
    }
	
	@GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public PortalContext context(@PathParam("id") long id) {
        
    	PortalPull pull = portals.pull(null, id);
        
        return new PortalContext(
    		principal.get().getUser(),
			pull.portal,
			pull.content
		);
    }
}
