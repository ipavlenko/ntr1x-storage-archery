package com.ntr1x.storage.archery.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ntr1x.storage.archery.model.Domain;
import com.ntr1x.storage.archery.model.Portal;
import com.ntr1x.storage.archery.repository.DomainRepository;
import com.ntr1x.storage.core.reflection.ResourceUtils;
import com.ntr1x.storage.security.services.ISecurityService;

@Service
public class DomainService implements IDomainService {

	@Inject
	private EntityManager em;

	@Inject
	private ISecurityService security;
	
	@Inject
	private DomainRepository domains;
	
	@Override
	public Page<Domain> query(Long scope, Long user, Long portal, Pageable pageable) {
		
		return domains.query(scope, user, portal, pageable);
	}
	
	@Override
	public Domain select(Long scope, long id) {
		
		return domains.select(scope, id);
	}
	
	@Override
	public Domain select(Long scope, String name) {
		
		return domains.select(scope, name);
	}
	
	@Override
	public Domain remove(Long scope, long id) {
		
		Domain d = domains.select(scope, id); {
			
			em.remove(d);
			em.flush();
		}
		
		return d;
	}
	
	@Override
	public Domain create(long scope, DomainCreate create) {
		
		Domain d = new Domain(); {
			
			Portal portal = em.find(Portal.class, create.portal);
			
			d.setScope(scope);
			d.setName(create.name);
			d.setPortal(portal);
			
			em.persist(d);
			em.flush();
			
			security.register(d, ResourceUtils.alias(null, "domains/i", d));
			security.grant(portal.getScope(), portal.getUser(), d.getAlias(), "admin");
		}
		
		return d;
	}

	@Override
	public Domain update(Long scope, long id, DomainUpdate update) {
		
		Domain d = domains.select(scope, id); {
			
			d.setName(update.name);
			
			em.merge(d);
			em.flush();
		}
		
		return d;
	}
	
	@Override
	public void createDomains(Portal portal, RelatedDomain[] domains) {
		
		if (domains != null) {
            
            for (RelatedDomain p : domains) {
                
                Domain d = new Domain(); {
                    
                	d.setPortal(portal);
                    d.setScope(portal.getScope());
                    d.setName(p.name);
                    
                    em.persist(d);
                    em.flush();
                    
                    security.register(d, ResourceUtils.alias(null, "domains/i", d));
        			security.grant(portal.getScope(), portal.getUser(), d.getAlias(), "admin");
                }
            }
            
            em.flush();
        }
	}
	
	@Override
	public void updateDomains(Portal portal, RelatedDomain[] domains) {
		
		if (domains != null) {
            
            for (RelatedDomain p : domains) {
                
                switch (p.action) {
                
                    case CREATE: {
                        
                    	Domain d = new Domain(); {
                            
                            d.setScope(portal.getScope());
                            d.setPortal(portal);
                            d.setName(p.name);
                            
                            em.persist(d);
                            em.flush();
                            
                            security.register(d, ResourceUtils.alias(null, "domains/i", d));
                			security.grant(portal.getScope(), portal.getUser(), d.getAlias(), "admin");
                        }
                    	
                        break;
                    }
                    case UPDATE: {
                        
                        Domain d = select(portal.getScope(), p.id); {
                            
                            d.setName(p.name);
                            
                            em.merge(d);
                        }
                        
                        break;
                    }
                    case REMOVE: {
                        
                    	Domain d = select(portal.getScope(), p.id); {
                            
                            d.setName(p.name);
                            
                            em.remove(d);
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
