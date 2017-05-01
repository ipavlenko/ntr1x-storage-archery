package com.ntr1x.storage.archery.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ntr1x.storage.archery.model.Portal;
import com.ntr1x.storage.archery.model.Store;
import com.ntr1x.storage.archery.repository.StoreRepository;
import com.ntr1x.storage.core.reflection.ResourceUtils;
import com.ntr1x.storage.core.services.IParamService;
import com.ntr1x.storage.security.services.ISecurityService;

@Service
public class StoreService implements IStoreService {

    @Inject
    private EntityManager em;

    @Inject
    private ISecurityService security;
    
    @Inject
    private StoreRepository stores;
    
    @Inject
    private IParamService params;
    
    @Override
    public Store create(long scope, StoreCreate create) {
        
        Store s = new Store(); {
            
            Portal portal = em.find(Portal.class, create.portal);
            
            s.setScope(scope);
            s.setName(create.name);
            s.setTitle(create.title);
            s.setPortal(portal);
            
            em.persist(s);
            em.flush();
            
            security.register(s, ResourceUtils.alias(null, "stores/i", s));
            security.grant(scope, portal.getUser(), s.getAlias(), "admin");
            
            params.createParams(s, create.params);
        }
        
        return s;
    }

    @Override
    public Store update(Long scope, long id, StoreUpdate update) {
        
        Store s = stores.select(scope, id); {
            
            s.setName(update.name);
            s.setTitle(update.title);
            
            em.merge(s);
            em.flush();
            
            params.updateParams(s, update.params);
        }
        
        return s;
    }

    @Override
    public Store remove(Long scope, long id) {
        
        Store s = stores.select(scope, id); {
            
            em.remove(s);
            em.flush();
        }
        
        return s;
    }

    @Override
    public Store select(Long scope, String name) {
        
        return stores.select(scope, name);
    }
    
    @Override
    public Store select(Long scope, long id) {
        
        return stores.select(scope, id);
    }

    @Override
    public Page<Store> query(Long scope, Long user, Long portal, Pageable pageable) {
        
        return stores.query(scope, user, portal, pageable);
    }
    
    @Override
    public void createStores(Portal portal, RelatedStore[] stores) {
        
        if (stores != null) {
            
            for (RelatedStore p : stores) {
                
                Store d = new Store(); {
                    
                    d.setPortal(portal);
                    d.setScope(portal.getScope());
                    d.setName(p.name);
                    d.setTitle(p.title);
                    
                    em.persist(d);
                    em.flush();
                    
                    security.register(d, ResourceUtils.alias(null, "stores/i", d));
                    security.grant(portal.getScope(), portal.getUser(), d.getAlias(), "admin");
                    
                    params.createParams(d, p.params);
                }
            }
            
            em.flush();
        }
    }
    
    @Override
    public void updateStores(Portal portal, RelatedStore[] stores) {
        
        if (stores != null) {
            
            for (RelatedStore p : stores) {
                
                switch (p.action) {
                
                    case CREATE: {
                        
                        Store d = new Store(); {
                            
                            d.setScope(portal.getScope());
                            d.setPortal(portal);
                            d.setName(p.name);
                            d.setTitle(p.title);
                            
                            em.persist(d);
                            em.flush();
                            
                            security.register(d, ResourceUtils.alias(null, "stores/i", d));
                            security.grant(portal.getScope(), portal.getUser(), d.getAlias(), "admin");
                            
                            params.createParams(d, p.params);
                        }
                        
                        break;
                    }
                    case UPDATE: {
                        
                        Store d = select(portal.getScope(), p.id); {
                            
                            d.setName(p.name);
                            
                            em.merge(d);
                            em.flush();
                            
                            params.updateParams(d, p.params);
                        }
                        
                        break;
                    }
                    case REMOVE: {
                        
                        Store d = select(portal.getScope(), p.id); {
                            
                            d.setName(p.name);
                            
                            em.remove(d);
                            em.flush();
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
