package com.ntr1x.storage.archery.resources;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ntr1x.storage.archery.model.Portal;
import com.ntr1x.storage.archery.services.IPortalService;
import com.ntr1x.storage.archery.services.IPortalService.PortalCreate;
import com.ntr1x.storage.archery.services.IPortalService.PortalPageResponse;
import com.ntr1x.storage.core.filters.IUserScope;
import com.ntr1x.storage.core.transport.PageableQuery;
import com.ntr1x.storage.security.filters.IUserPrincipal;

import io.swagger.annotations.Api;

@Api("Me")
@Component
@Path("/me/archery/portals")
@PermitAll
public class PortalMe {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private IPortalService portals;
    
    @Inject
    private Provider<IUserPrincipal> principal;
    
    @Inject
    private Provider<IUserScope> scope;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "auth" })
    public PortalPageResponse query(
		@QueryParam("shared") Boolean shared,
    	@BeanParam PageableQuery pageable
    ) {
    	
    	Page<Portal> p = portals.query(
			scope.get().getId(),
			shared,
			principal.get().getUser().getId(),
			pageable.toPageRequest()
		);
    	
        return new PortalPageResponse(
    		p.getTotalElements(),
    		p.getNumber(),
    		p.getSize(),
    		p.getContent()
		);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "auth" })
    public Portal create(@Valid PortalCreate create) {
    	
    	create.user = principal.get().getUser().getId();
    	
        return portals.create(scope.get().getId(), create);
	}
}
