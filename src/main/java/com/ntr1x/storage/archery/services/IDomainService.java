package com.ntr1x.storage.archery.services;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ntr1x.storage.archery.model.Domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IDomainService {

	Domain create(DomainCreate create);
	Domain update(long id, DomainUpdate update);
	
	Domain select(long id);
	
	Domain select(String name);
	
	Page<Domain> query(Long user, Long portal, Pageable pageable);
	
	Domain remove(long id);
	
	@XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DomainPageResponse {

    	public long count;
        public int page;
        public int size;

        @XmlElement
        public List<Domain> content;
	}
	
	@XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DomainCreate {
        
		@NotBlank
    	public String name;
    	public long portal;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DomainUpdate {
    	
    	@NotBlank
    	public String name;
    }
}
