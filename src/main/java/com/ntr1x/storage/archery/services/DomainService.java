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
	public Page<Domain> query(Long user, Long portal, Pageable pageable) {
		
		return domains.query(user, portal, pageable);
	}
	
	@Override
	public Domain select(long id) {
		
		return domains.findOne(id);
	}
	
	@Override
	public Domain select(String name) {
		
		return domains.findOneByName(name);
	}
	
	@Override
	public Domain remove(long id) {
		
		Domain d = em.find(Domain.class, id); {
			
			em.remove(d);
			em.flush();
		}
		
		return d;
	}
	
	@Override
	public Domain create(DomainCreate create) {
		
		Domain d = new Domain(); {
			
			Portal portal = em.find(Portal.class, create.portal);
			
			d.setName(create.name);
			d.setPortal(portal);
			
			em.persist(d);
			em.flush();
			
			security.register(d, ResourceUtils.alias(null, "domains/i", d));
			security.grant(portal.getUser(), d.getAlias(), "admin");
		}
		
		return d;
	}

	@Override
	public Domain update(long id, DomainUpdate update) {
		
		Domain d = em.find(Domain.class, id); {
			
			d.setName(update.name);
			
			em.merge(d);
			em.flush();
		}
		
		return d;
	}
}
