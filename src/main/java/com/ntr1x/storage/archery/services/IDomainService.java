package com.ntr1x.storage.archery.services;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ntr1x.storage.archery.model.Domain;
import com.ntr1x.storage.archery.model.Portal;
import com.ntr1x.storage.core.model.Action;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IDomainService {

    Domain create(long scope, DomainCreate create);
    Domain update(Long scope, long id, DomainUpdate update);
    
    Domain select(Long scope, long id);
    Domain select(Long scope, String name);
    
    Page<Domain> query(Long scope, Long user, Long portal, Pageable pageable);
    
    Domain remove(Long scope, long id);
    
    void createDomains(Portal portal, RelatedDomain[] domains);
    void updateDomains(Portal portal, RelatedDomain[] domains);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedDomain {
        
        public Long id;
        
        @NotBlank
        public String name;
        
        public Action action;
    }
    
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
