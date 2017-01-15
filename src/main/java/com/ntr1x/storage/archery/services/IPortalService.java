package com.ntr1x.storage.archery.services;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.JsonNode;
import com.ntr1x.storage.archery.model.Portal;
import com.ntr1x.storage.security.model.User;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IPortalService {

	Portal create(long scope, PortalCreate create);
	Portal update(Long scope, long id, PortalUpdate update);
	Portal share(Long scope, long id, boolean share);
	Portal remove(Long scope, long id);

    Page<Portal> query(Long scope, Boolean shared, Long user, Pageable pageable);
    
    Portal select(Long scope, long id);

    PortalPull pull(Long scope, long id);
    PortalPush push(Long scope, long id, PortalPush content);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortalPageResponse {

    	public long count;
        public int page;
        public int size;

        @XmlElement
        public List<Portal> content;
	}
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortalPush {
        
		@XmlElement
		@ApiModelProperty(dataType = "Object")
    	public JsonNode content;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortalContext {
        
    	@XmlElement
		public User user;
    	
    	@XmlElement
		public Portal portal;
    	
		@XmlElement
		@ApiModelProperty(dataType = "Object")
    	public JsonNode content;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortalPull {
        
    	@XmlElement
		public Portal portal;
    	
		@XmlElement
		@ApiModelProperty(dataType = "Object")
    	public JsonNode content;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortalCreate {
        
    	@NotBlank
    	public String title;
    	
    	public Long proto;
        public long user;
        public Long thumbnail;
        
        @XmlElement
        public IDomainService.RelatedDomain[] domains;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortalUpdate {
        
    	@NotBlank
    	public String title;
    	
    	public Long thumbnail;
    	public Boolean shared;
    	
    	@XmlElement
        public IDomainService.RelatedDomain[] domains;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortalRootAccount {
    	
    	public String name;
        public String email;
        public String password;
    }
}
