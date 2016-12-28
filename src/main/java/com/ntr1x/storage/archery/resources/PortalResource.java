package com.ntr1x.storage.archery.resources;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ntr1x.storage.archery.model.Portal;
import com.ntr1x.storage.archery.services.IPortalService;
import com.ntr1x.storage.archery.services.IPortalService.PortalContent;
import com.ntr1x.storage.archery.services.IPortalService.PortalCreate;
import com.ntr1x.storage.archery.services.IPortalService.PortalPageResponse;
import com.ntr1x.storage.archery.services.IPortalService.PortalUpdate;
import com.ntr1x.storage.core.model.Resource.ResourceExtra;
import com.ntr1x.storage.core.transport.PageableQuery;

import io.swagger.annotations.Api;

@Api("Archery")
@Component
@Path("/portals")
@PermitAll
public class PortalResource {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private IPortalService portals;

    @GET
    @Path("/shared")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public PortalPageResponse shared(
		@BeanParam PageableQuery pageable
    ) {
    	
        Page<Portal> p = portals.query(
			true,
			null,
			pageable.toPageRequest()
		);
        
        return new PortalPageResponse(
    		p.getTotalElements(),
    		p.getNumber(),
    		p.getSize(),
    		p.getContent()
		);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///portals:admin" })
    public PortalPageResponse query(
		@QueryParam("shared") Boolean shared,
    	@QueryParam("user") Long user,
		@BeanParam PageableQuery pageable
    ) {
    	
        Page<Portal> p = portals.query(
			shared,
			user,
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
    @RolesAllowed({ "res:///portals:admin" })
    public Portal create(PortalCreate create) {

        return portals.create(create);
	}
    
    @GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///portals/i/{id}:admin" })
    public Portal select(@PathParam("id") long id) {
        
        return portals.select(id);
    }
    
    @GET
    @Path("/i/{id}/pull")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///portals/i/{id}:admin" })
    @ResourceExtra
    public PortalContent pull(@PathParam("id") long id) {
        
        return portals.pull(id);
    }

	@PUT
	@Path("/i/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({ "res:///portals/i/{id}:admin" })
	public Portal update(@PathParam("id") long id, PortalUpdate update) {
	    
	    return portals.update(id, update);
	}
	
	@PUT
	@Path("/i/{id}/push")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@ResourceExtra
	@RolesAllowed({ "res:///portals/i/{id}:admin" })
	public PortalContent push(@PathParam("id") long id, PortalContent content) {
	    
		return portals.push(id, content);
	}
	
	@DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///portals/i/{id}:admin" })
    public Portal remove(@PathParam("id") long id) {
        
	    return portals.remove(id);
    }
}
