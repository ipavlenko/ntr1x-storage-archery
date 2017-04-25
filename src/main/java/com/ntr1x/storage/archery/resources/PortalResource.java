package com.ntr1x.storage.archery.resources;

import java.util.List;

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
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
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

import com.ntr1x.storage.archery.model.Domain;
import com.ntr1x.storage.archery.model.Portal;
import com.ntr1x.storage.archery.services.IDomainService;
import com.ntr1x.storage.archery.services.IDomainService.DomainCreate;
import com.ntr1x.storage.archery.services.IPortalService;
import com.ntr1x.storage.archery.services.IPortalService.PortalCreate;
import com.ntr1x.storage.archery.services.IPortalService.PortalDetails;
import com.ntr1x.storage.archery.services.IPortalService.PortalPageResponse;
import com.ntr1x.storage.archery.services.IPortalService.PortalPull;
import com.ntr1x.storage.archery.services.IPortalService.PortalPush;
import com.ntr1x.storage.archery.services.IPortalService.PortalUpdate;
import com.ntr1x.storage.core.filters.IUserScope;
import com.ntr1x.storage.core.model.Resource.ResourceExtra;
import com.ntr1x.storage.core.transport.PageableQuery;

import io.swagger.annotations.Api;

@Api("Archery")
@Component
@Path("/archery/portals")
@PermitAll
public class PortalResource {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private IPortalService portals;
    
    @Inject
    private IDomainService domains;
    
    @Inject
    private Provider<IUserScope> scope;
    
    @GET
    @Path("/shared")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public PortalPageResponse shared(
        @BeanParam PageableQuery pageable
    ) {
        
        Page<Portal> p = portals.query(
            null,
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
    @Path("/shared/i/{id}/pull")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @ResourceExtra
    public PortalPull sharedPull(@PathParam("id") long id) {
        
        Portal p = portals.select(null, id);
        if (!p.isShared()) {
            throw new ForbiddenException();
        }
        
        return portals.pull(null, id);
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
            scope.get().getId(),
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
    public Portal create(@Valid PortalCreate create) {

        return portals.create(scope.get().getId(), create);
    }
    
    @GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Portal select(@PathParam("id") long id) {
        
        return portals.select(scope.get().getId(), id);
    }
    
    @GET
    @Path("/i/{id}/details")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///portals/i/{id}:admin" })
    public PortalDetails details(@PathParam("id") long id) {
        
        return portals.details(scope.get().getId(), id);
    }
    
    @GET
    @Path("/i/{id}/pull")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///portals/i/{id}:admin" })
    @ResourceExtra
    public PortalPull pull(@PathParam("id") long id) {
        
        return portals.pull(scope.get().getId(), id);
    }

    @PUT
    @Path("/i/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///portals/i/{id}:admin" })
    public Portal update(@PathParam("id") long id, @Valid PortalUpdate update) {
        
        return portals.update(scope.get().getId(), id, update);
    }
    
    @PUT
    @Path("/i/{id}/share")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///portals:admin" })
    public Portal share(@PathParam("id") long id) {
        
        return portals.share(scope.get().getId(), id, true);
    }
    
    @PUT
    @Path("/i/{id}/unshare")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///portals:admin" })
    public Portal unshare(@PathParam("id") long id) {
        
        return portals.share(scope.get().getId(), id, false);
    }
    
    @PUT
    @Path("/i/{id}/push")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @ResourceExtra
    @RolesAllowed({ "res:///portals/i/{id}:admin" })
    public PortalPush push(@PathParam("id") long id, PortalPush data) {
        
        return portals.push(scope.get().getId(), id, data);
    }
    
    @DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///portals/i/{id}:admin" })    
    public Portal remove(@PathParam("id") long id) {
        
        return portals.remove(scope.get().getId(), id);
    }
    
    @GET
    @Path("/i/{id}/domains")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///portals/i/{id}/domains:admin" })
    public List<Domain> domains(@PathParam("id") long id) {
        
        return domains.query(scope.get().getId(), null, id, null).getContent();
    }
    
    @POST
    @Path("/i/{id}/domains")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///portals/i/{id}/domains:admin" })
    public Domain domainsCreate(@PathParam("id") long id, @Valid DomainCreate create) {
        
        create.portal = id;
        
        return domains.create(scope.get().getId(), create);
    }
}
