package com.ntr1x.storage.archery.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.ForbiddenException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ntr1x.storage.archery.model.Portal;
import com.ntr1x.storage.archery.model.Template;
import com.ntr1x.storage.archery.repository.TemplateRepository;
import com.ntr1x.storage.core.reflection.ResourceUtils;
import com.ntr1x.storage.security.services.ISecurityService;

@Service
public class TemplateService implements ITemplateService {

	@Inject
	private EntityManager em;
	
	@Inject
	private TemplateRepository templates;
	
	@Inject
	private IPortalService portals;
	
	@Inject
	private ISecurityService security;
	
	
	@Override
	public Page<Template> query(Long scope, Long user, Long portal, Pageable pageable) {
		
		return templates.query(scope, user, portal, pageable);
	}
	
	@Override
	public Template select(Long scope, long id) {
		
		return templates.select(scope, id);
	}
	
	@Override
	public Template select(Long scope, long resource, String name) {

		return templates.select(scope, resource, name);
	}
	
	@Override
	public Template remove(Long scope, long id) {
		
		Template t = templates.select(scope, id); {
			
			em.remove(t);
			em.flush();
		}
		
		return t;
	}
	
	@Override
	public Template create(long scope, TemplateCreate create) {
		
		Template t = new Template(); {
			
			Portal portal = portals.select(scope, create.portal);
			
			t.setScope(scope);
			t.setName(create.name);
			t.setPortal(portal);
			t.setSender(create.sender);
			t.setSubject(create.subject);
			t.setContent(create.content);
			
			em.persist(t);
			em.flush();
			
			security.register(t, ResourceUtils.alias(null, "templates/i", t));
			security.grant(portal.getScope(), portal.getUser(), t.getAlias(), "admin");
		}
		
		return t;
	}

	@Override
	public Template update(Long scope, long id, TemplateUpdate update) {
		
		Template t = templates.select(scope, id); {
			
			t.setName(update.name);
			t.setSender(update.sender);
			t.setSubject(update.subject);
			t.setContent(update.content);
			
			em.merge(t);
			em.flush();
		}
		
		return t;
	}
	
	@Override
	public void createTemplates(Portal portal, RelatedTemplate[] related) {
		
		if (related != null) {
            
            for (RelatedTemplate t : related) {
                
                Template v = new Template(); {
                    
                	v.setPortal(portal);
                    v.setScope(portal.getScope());
                    v.setName(t.name);
                    v.setSender(t.sender);
                    v.setSubject(t.subject);
                    v.setContent(t.content);
                    
                    em.persist(v);
                    em.flush();
                    
                    security.register(v, ResourceUtils.alias(null, "templates/i", v));
        			security.grant(portal.getScope(), portal.getUser(), v.getAlias(), "admin");
                }
            }
        }
	}

	@Override
	public void updateTemplates(Portal portal, RelatedTemplate[] related) {
		
		if (related != null) {
            
            for (RelatedTemplate t : related) {
                
                switch (t.action) {
                
                    case CREATE: {
                        
                        Template v = new Template(); {
                            
                        	v.setPortal(portal);
                        	v.setScope(portal.getScope());
                        	v.setName(t.name);
                            v.setSender(t.sender);
                            v.setSubject(t.subject);
                            v.setContent(t.content);
                            
                            em.persist(v);
                            em.flush();
                            
                            security.register(v, ResourceUtils.alias(null, "templates/i", v));
                			security.grant(portal.getScope(), portal.getUser(), v.getAlias(), "admin");
                            
                        }
                        break;
                    }
                    case UPDATE: {
                    
                    	Template v = templates.select(portal.getScope(), t.id); {
                    		
                            v.setSender(t.sender);
                            v.setSubject(t.subject);
                            v.setContent(t.content);
                            
                            em.merge(v);
                            em.flush();
                    	}
                        
                    	break;
                    }
                    case REMOVE: {
                        
                        Template v = templates.select(portal.getScope(), t.id); {
                        	
                        	if (v.getPortal().getId() != portal.getId() || v.getPortal().getScope() != portal.getScope()) {
                        		throw new ForbiddenException("Template relates to another scope or portal");
                        	}
                        	em.remove(v);
                        }
                        break;
                    }
                default:
                    break;
                }
            }
            
            em.flush();
        }
	}
}
