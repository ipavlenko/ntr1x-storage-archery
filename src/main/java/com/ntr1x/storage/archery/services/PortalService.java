package com.ntr1x.storage.archery.services;

import java.io.IOException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAllowedException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntr1x.storage.archery.model.Portal;
import com.ntr1x.storage.archery.repository.PortalRepository;
import com.ntr1x.storage.core.model.Image;
import com.ntr1x.storage.core.reflection.ResourceUtils;
import com.ntr1x.storage.security.model.User;
import com.ntr1x.storage.security.services.ISecurityService;

@Service
public class PortalService implements IPortalService {

	@Inject
	private EntityManager em;
	
	@Inject
	private PortalRepository portals;
	
	@Inject
	private ISecurityService security;

	@Inject
	private ObjectMapper mapper;
	
	@Override
	public Portal create(PortalCreate create) {
		
		Portal p = new Portal(); {
			
			if (create.proto != null) {
				
				Portal proto = em.find(Portal.class, create.proto);
				if (proto == null) {
					throw new BadRequestException("Unknown proto portal");
				}
				
				if (!proto.isShared()) {
					throw new BadRequestException("Proto portal is not shared");
				}
				
				p.setContent(proto.getContent());
			}
			
			User user = em.find(User.class, create.user);
			Image thumbnail = create.thumbnail == null ? null : em.find(Image.class, create.thumbnail);
			
			p.setTitle(create.title);
			p.setShared(false);
			p.setUser(user);
			p.setThumbnail(thumbnail);
			
			em.persist(p);
			em.flush();
			
			security.register(p, ResourceUtils.alias(null, "portals/i", p));
			security.grant(user, p.getAlias(), "admin");
		}
		
		return p;
	}

	@Override
	public Portal update(long id, PortalUpdate update) {
		
		Portal p = em.find(Portal.class, id); {
			
			if (p.isShared()) {
				throw new NotAllowedException("Shared portal cannot be updated");
			}
			
			Image thumbnail = update.thumbnail == null ? null : em.find(Image.class, update.thumbnail);
			
			p.setTitle(update.title);
			p.setThumbnail(thumbnail);
			
			em.merge(p);
			em.flush();
		}
		
		return p;
	}
	
	@Override
	public Portal share(long id, boolean share) {
		
		Portal p = em.find(Portal.class, id); {
			
			p.setShared(share);
			
			
			
			em.merge(p);
			em.flush();
		}
		
		return p;
	}

	@Override
	public Portal remove(long id) {
		
		Portal p = em.find(Portal.class, id);
		
		em.remove(p);
		em.flush();
		
		return p;
	}

	@Override
	public Page<Portal> query(Boolean shared, Long user, Pageable pageable) {
		
		return portals.query(shared, user, pageable);
	}

	@Override
	public Portal select(long id) {
		
		return em.find(Portal.class, id);
	}

	@Override
	public PortalContent push(long id, PortalContent content) {
		
		Portal p = em.find(Portal.class, id); {
		
			try {
				p.setContent(mapper.writeValueAsString(content.content));
			} catch (JsonProcessingException e) {
				throw new BadRequestException("Invalid content");
			}
			
			em.merge(p);
			em.flush();
		}
		
		return content;
	}
	
	@Override
	public PortalContent pull(long id) {
		
		Portal p = em.find(Portal.class, id);
		try {
			return new PortalContent(mapper.readTree(p.getContent()));
		} catch (IOException e) {
			throw new InternalServerErrorException("Cannot parse portal content");
		}
	}
}
