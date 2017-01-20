package com.ntr1x.storage.archery.services;

import java.io.IOException;
import java.util.Properties;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntr1x.storage.archery.model.Portal;
import com.ntr1x.storage.archery.repository.PortalRepository;
import com.ntr1x.storage.core.model.Image;
import com.ntr1x.storage.core.model.Param;
import com.ntr1x.storage.core.reflection.ResourceUtils;
import com.ntr1x.storage.core.services.IParamService;
import com.ntr1x.storage.security.model.User;
import com.ntr1x.storage.security.services.ISecurityService;
import com.ntr1x.storage.security.services.IUserService;
import com.ntr1x.storage.uploads.services.IImageService;

@Service
public class PortalService implements IPortalService {

	@Inject
	private EntityManager em;
	
	@Inject
	private PortalRepository portals;
	
	@Inject
	private IUserService users;
	
	@Inject
	private ISecurityService security;

	@Inject
	private IImageService images;
	
	@Inject
	private IDomainService domains;
	
	@Inject
	private IParamService params;
	
	@Inject
	private ITemplateService templates;
	
	@Inject
	private ObjectMapper mapper;
	
	@Override
	public Portal create(long scope, PortalCreate create) {
		
		Portal p = new Portal(); {
			
			if (create.proto != null) {
				
				Portal proto = portals.select(null, create.proto);
				if (proto == null) {
					throw new BadRequestException("Unknown proto portal");
				}
				
				if (!proto.isShared()) {
					throw new BadRequestException("Proto portal is not shared");
				}
				
				p.setContent(proto.getContent());
			}
			
			User user = users.select(scope, create.user);
			
			Image thumbnail = create.thumbnail == null ? null : images.select(scope, create.thumbnail);
			
			p.setScope(scope);
			p.setTitle(create.title);
			p.setShared(false);
			p.setUser(user);
			p.setThumbnail(thumbnail);
			
			em.persist(p);
			em.flush();
			
			security.register(p, ResourceUtils.alias(null, "portals/i", p));
			security.grant(scope, user, p.getAlias(), "admin");
			security.grant(p.getId(), user, "/", "admin");
			
			domains.createDomains(p, create.domains);
			
			params.createParams(p, create.params);
			
			templates.createTemplates(p, create.templates);
		}
		
		return p;
	}

	@Override
	public Portal update(Long scope, long id, PortalUpdate update) {
		
		Portal p = portals.select(scope, id); {
			
			Image thumbnail = update.thumbnail == null ? null : images.select(scope, update.thumbnail);
			
			p.setTitle(update.title);
			p.setThumbnail(thumbnail);
			
			if (update.shared != null) p.setShared(update.shared);
			
			em.merge(p);
			em.flush();
			
			domains.updateDomains(p, update.domains);
			
			params.updateParams(p, update.params);
			
			templates.updateTemplates(p, update.templates);
		}
		
		return p;
	}
	
	@Override
	public Portal share(Long scope, long id, boolean share) {
		
		Portal p = portals.select(scope, id); {
			
			p.setShared(share);
			
			em.merge(p);
			em.flush();
		}
		
		return p;
	}

	@Override
	public Portal remove(Long scope, long id) {
		
		Portal p = portals.select(scope, id); {
		
			em.remove(p);
			em.flush();
		}
		
		return p;
	}

	@Override
	public Page<Portal> query(Long scope, Boolean shared, Long user, Pageable pageable) {
		
		return portals.query(scope, shared, user, pageable);
	}

	@Override
	public Portal select(Long scope, long id) {
		
		return portals.select(scope, id);
	}
	
	@Override
	public Properties properties(Long scope, long id, String type) {
		
		Properties properties = new Properties();
		for (Param p : params.list(scope, id, Portal.ParamType.MAIL.name())) {
			properties.put(p.getName(), p.getValue());
		}
		return properties;
	}
	
	@Override
	public PortalDetails details(Long scope, long id) {
		
		Portal portal = portals.select(scope, id);
		
		return new PortalDetails(
			portal,
			params.list(scope, id, Portal.ParamType.META.name()),
			params.list(scope, id, Portal.ParamType.MAIL.name()),
			params.list(scope, id, Portal.ParamType.MAIL.name()),
			templates.query(scope, id, null).getContent()
		);
	}

	@Override
	public PortalPush push(Long scope, long id, PortalPush content) {
		
		Portal p = portals.select(scope, id); {
		
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
	public PortalPull pull(Long scope, long id) {
		
		Portal p = portals.select(scope, id);
		try {
			String content = p.getContent();
			return new PortalPull(
				p,
				content == null ? null : mapper.readTree(content)
			);
		} catch (IOException e) {
			throw new InternalServerErrorException("Cannot parse portal content");
		}
	}
}
