package com.ntr1x.storage.archery.services;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ntr1x.storage.archery.model.Portal;
import com.ntr1x.storage.archery.model.Template;
import com.ntr1x.storage.core.model.Action;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface ITemplateService {
    
    Template create(long scope, TemplateCreate create);
    Template update(Long scope, long id, TemplateUpdate update);
    
    Template select(Long scope, long id);
    Template select(Long scope, long resource, String name);
    
    Page<Template> query(Long scope, Long user, Long portal, Pageable pageable);
    
    Template remove(Long scope, long id);
    
    void createTemplates(Portal portal, RelatedTemplate[] templates);
    void updateTemplates(Portal portal, RelatedTemplate[] templates);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplatePageResponse {

        public long count;
        public int page;
        public int size;

        @XmlElement
        public List<Template> content;
    }
    
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateCreate {
        
        public long portal;
        public String name;
        public String sender;
        public String subject;
        public String content;
    }
    
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateUpdate {
        
        public long id;
        public String name;
        public String sender;
        public String subject;
        public String content;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedTemplate {
        
        public Long id;
        public String name;
        public String sender;
        public String subject;
        public String content;
        public Action action;
    }
}
