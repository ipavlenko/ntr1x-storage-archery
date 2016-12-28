package com.ntr1x.storage.archery.services;

import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ntr1x.storage.archery.model.Portal;
import com.ntr1x.storage.core.jaxb.JsonStringXmlAdapter;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IPortalService {

	Portal create(PortalCreate create);
	Portal update(long id, PortalUpdate update);
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
        
		@XmlAnyElement
		@XmlJavaTypeAdapter(value = JsonStringXmlAdapter.class)
		@ApiModelProperty(dataType = "Object")
    	public String content;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortalCreate {
        
    	public String title;
    	public Long proto;
        public long user;
        public Long thumbnail;
    	public boolean shared;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortalUpdate {
        
    	public String title;
    	public Long thumbnail;
    	public boolean shared;
    }
}
