package com.ntr1x.storage.archery.resources;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.transaction.Transactional;
import javax.validation.Valid;
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

import com.ntr1x.storage.archery.model.Template;
import com.ntr1x.storage.archery.services.ITemplateService;
import com.ntr1x.storage.archery.services.ITemplateService.TemplateCreate;
import com.ntr1x.storage.archery.services.ITemplateService.TemplatePageResponse;
import com.ntr1x.storage.archery.services.ITemplateService.TemplateUpdate;
import com.ntr1x.storage.core.filters.IUserScope;
import com.ntr1x.storage.core.transport.PageableQuery;

import io.swagger.annotations.Api;

@Api("Resources")
@Path("/archery/templates")
@Component
@PermitAll
public class TemplateResource {
    
    @Inject
    private ITemplateService templates;
    
    @Inject
    private Provider<IUserScope> scope;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public TemplatePageResponse list(
        @QueryParam("portal") Long portal,
        @QueryParam("user") Long user,
        @BeanParam PageableQuery pageable
    ) {
        
        Page<Template> p = templates.query(
            scope.get().getId(),
            user,
            portal,
            pageable.toPageRequest()
        );
        
        return new TemplatePageResponse(
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
    @RolesAllowed({ "res:///templates:admin" })
    public Template create(@Valid TemplateCreate create) {

        return templates.create(scope.get().getId(), create);
    }
    
    @PUT
    @Path("/i/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///templates/i/{id}:admin" })
    public Template update(@PathParam("id") long id, @Valid TemplateUpdate update) {
        
        return templates.update(scope.get().getId(), id, update);
    }
    
    @GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///templates/i/{id}:admin" })
    public Template select(@PathParam("id") long id) {
        
        return templates.select(scope.get().getId(), id);
    }
    
    @DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///templates/i/{id}:admin" })
    public Template remove(@PathParam("id") long id) {
        
        return templates.remove(scope.get().getId(), id);
    }
}
