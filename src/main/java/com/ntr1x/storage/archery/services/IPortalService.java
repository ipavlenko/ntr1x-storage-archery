package com.ntr1x.storage.archery.services;

import java.util.List;
import java.util.Properties;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.JsonNode;
import com.ntr1x.storage.archery.model.Domain;
import com.ntr1x.storage.archery.model.Portal;
import com.ntr1x.storage.archery.model.Template;
import com.ntr1x.storage.core.model.Param;
import com.ntr1x.storage.core.services.IParamService;
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
    PortalDetails details(Long scope, long id);
    Properties properties(Long scope, long id, String type);

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
    public static class PortalDetails {
        
        public Portal portal;
        
        @XmlElement
        public List<Param> meta;
        
        @XmlElement
        public List<Param> mail;
        
        @XmlElement
        public List<Param> routes;
        
        @XmlElement
        public List<Template> templates;
        
        @XmlElement
        public List<Domain> domains;
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
        
        @XmlElement
        public IParamService.RelatedParam[] params;
        
        @XmlElement
        public ITemplateService.RelatedTemplate[] templates;

        @XmlElement
        public IStoreService.RelatedStore[] stores;
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
        
        @XmlElement
        public IParamService.RelatedParam[] params;
        
        @XmlElement
        public ITemplateService.RelatedTemplate[] templates;
        
        @XmlElement
        public IStoreService.RelatedStore[] stores;
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
