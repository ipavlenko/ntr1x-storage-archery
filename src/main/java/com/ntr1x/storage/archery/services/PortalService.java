package com.ntr1x.storage.archery.services;

import java.io.StringWriter;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonWriter;
import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ntr1x.storage.archery.model.Portal;
import com.ntr1x.storage.archery.repository.PortalRepository;
import com.ntr1x.storage.core.model.Image;
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
	
	@Override
	public Portal create(PortalCreate create) {
		
		Portal p = new Portal(); {
			
			User user = em.find(User.class, create.user);
			Image thumbnail = create.thumbnail == null ? null : em.find(Image.class, create.thumbnail);
			
			p.setTitle(create.title);
			p.setShared(create.shared);
			p.setUser(user);
			p.setThumbnail(thumbnail);
			
			em.persist(p);
			em.flush();
			
			security.register(p, "portals/i");
			security.grant(user, p.getAlias(), "admin");
		}
		
		return p;
	}

	@Override
	public Portal update(long id, PortalUpdate update) {
		
		Portal p = em.find(Portal.class, id); {
			
			Image thumbnail = update.thumbnail == null ? null : em.find(Image.class, update.thumbnail);
			
			p.setTitle(update.title);
			p.setShared(update.shared);
			p.setThumbnail(thumbnail);
			
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
	public Portal push(long id, PortalPush push) {
		
		Portal p = em.find(Portal.class, id); {
			
			String content = null;
			
			if (push.content != null) {
				
				StringWriter stringWriter = new StringWriter();
				JsonWriter jsonWriter = Json.createWriter(stringWriter);
				
				jsonWriter.writeObject(push.content);
				jsonWriter.close();
				
				content = stringWriter.toString();
			}
			
			p.setContent(content);
			
			em.merge(p);
			em.flush();
		}
		
		return p;
	}
}
