package com.ntr1x.storage.archery.services;

import javax.json.JsonObject;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ntr1x.storage.archery.model.Portal;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IPortalService {

	Portal create(PortalCreate create);
	Portal update(long id, PortalUpdate update);
	Portal remove(long id);

    Page<Portal> query(Boolean shared, Long user, Pageable pageable);
    
    Portal select(long id);

    Portal push(long id, PortalPush update);
    
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
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortalPush {
        
    	public JsonObject content;
    }
}
