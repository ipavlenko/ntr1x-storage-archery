package com.ntr1x.storage.archery.services;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.JsonNode;
import com.ntr1x.storage.archery.model.Portal;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IPortalService {

	Portal create(PortalCreate create);
	Portal update(long id, PortalUpdate update);
	Portal share(long id, boolean share);
	Portal remove(long id);

    Page<Portal> query(Boolean shared, Long user, Pageable pageable);
    
    Portal select(long id);

    PortalContent pull(long id);
    PortalContent push(long id, PortalContent content);
    
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
    public static class PortalContent {
        
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
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortalUpdate {
        
    	@NotBlank
    	public String title;
    	
    	public Long thumbnail;
    	public Boolean shared;
    }
}
