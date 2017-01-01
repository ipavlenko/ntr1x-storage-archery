package com.ntr1x.storage.archery.resources;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ntr1x.storage.archery.model.Domain;
import com.ntr1x.storage.archery.services.IDomainService;
import com.ntr1x.storage.archery.services.IDomainService.DomainCreate;
import com.ntr1x.storage.archery.services.IDomainService.DomainPageResponse;
import com.ntr1x.storage.archery.services.IDomainService.DomainUpdate;
import com.ntr1x.storage.core.transport.PageableQuery;

import io.swagger.annotations.Api;

@Api("Archery")
@Component
@Path("/domains")
@PermitAll
public class DomainResource {
	
	@Inject
	private IDomainService domains;
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public DomainPageResponse shared(
		@QueryParam("portal") Long portal,
		@QueryParam("user") Long user,
		@BeanParam PageableQuery pageable
    ) {
    	
        Page<Domain> p = domains.query(
			user,
			portal,
			pageable.toPageRequest()
		);
        
        return new DomainPageResponse(
    		p.getTotalElements(),
    		p.getNumber(),
    		p.getSize(),
    		p.getContent()
		);
    }
	
	@GET
    @Path("/current")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Domain current(
		@HeaderParam("x-forwarded-host") String host,
		@Context HttpServletRequest request
	) {
    	
    	if (host == null) {
    		host = request.getServerName();
    	}
        
    	return domains.select(host);
    }
	
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///domains:admin" })
    public Domain create(DomainCreate create) {

        return domains.create(create);
	}
	
	@PUT
	@Path("/i/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({ "res:///domains/i/{id}:admin" })
	public Domain update(@PathParam("id") long id, DomainUpdate update) {
	    
	    return domains.update(id, update);
	}
	
	@GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///domains/i/{id}:admin" })
    public Domain select(@PathParam("id") long id) {
        
        return domains.select(id);
    }
	
	@DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///domains/i/{id}:admin" })
    public Domain remove(@PathParam("id") long id) {
        
	    return domains.remove(id);
    }
}
