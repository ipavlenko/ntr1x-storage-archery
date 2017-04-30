package com.ntr1x.storage.archery.services;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ntr1x.storage.archery.model.Portal;
import com.ntr1x.storage.archery.model.Store;
import com.ntr1x.storage.core.model.Action;
import com.ntr1x.storage.core.services.IParamService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IStoreService {
    
    Store create(long scope, StoreCreate create);
    Store update(Long scope, long id, StoreUpdate update);
    Store remove(Long scope, long id);
    
    Store select(Long scope, long id);
    Store select(Long scope, String name);
    
    Page<Store> query(Long scope, Long user, Long portal, Pageable pageable);
    
    Map<String, String> params(Store store, Store.ParamType type);
    
    void createStores(Portal portal, RelatedStore[] stores);
    void updateStores(Portal portal, RelatedStore[] stores);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedStore {
        
        public Long id;
        
        @NotBlank
        public String name;
        
        @NotBlank
        public String title;
        
        @XmlElement
        public IParamService.RelatedParam[] params;
        
        public Action action;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StorePageResponse {

        public long count;
        public int page;
        public int size;

        @XmlElement
        public List<Store> content;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoreCreate {

        public long portal;
        
        @NotBlank
        public String name;
        
        @NotBlank
        public String title;
        
        @XmlElement
        public IParamService.RelatedParam[] params;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoreUpdate {
        
        @NotBlank
        public String name;
        
        @NotBlank
        public String title;
        
        @XmlElement
        public IParamService.RelatedParam[] params;
    }
}
