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

import org.springframework.stereotype.Component;

import com.ntr1x.storage.archery.model.Portal;
import com.ntr1x.storage.archery.services.IPortalService;
import com.ntr1x.storage.archery.services.IPortalService.PortalCreate;
import com.ntr1x.storage.archery.services.IPortalService.PortalPush;
import com.ntr1x.storage.archery.services.IPortalService.PortalUpdate;
import com.ntr1x.storage.core.model.Resource.ResourceExtra;
import com.ntr1x.storage.core.transport.PageResponse;
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
    public PageResponse<Portal> shared(
		@BeanParam PageableQuery pageable
    ) {
        return new PageResponse<>(
    		portals.query(
				true,
				null,
				pageable.toPageRequest()
			)
		);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///portals:admin" })
    public PageResponse<Portal> query(
		@QueryParam("shared") Boolean shared,
    	@QueryParam("user") Long user,
		@BeanParam PageableQuery pageable
    ) {
        return new PageResponse<>(
    		portals.query(
				shared,
				user,
				pageable.toPageRequest()
			)
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
    public Portal pull(@PathParam("id") long id) {
        
        return portals.select(id);
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
	public Portal push(@PathParam("id") long id, PortalPush push) {
	    
	    return portals.push(id, push);
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
