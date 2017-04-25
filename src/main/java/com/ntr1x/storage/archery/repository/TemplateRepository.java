package com.ntr1x.storage.archery.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntr1x.storage.archery.model.Template;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    
    @Query(
        " SELECT t"
      + " FROM Template t"
      + " WHERE (:scope IS NULL OR t.scope = :scope)"
      + "    AND (:user IS NULL OR t.portal.user.id = :user)"
      + "    AND (t.portal.id = :portal)"
    )
    Page<Template> query(
        @Param("scope") Long scope,
        @Param("user") Long user,
        @Param("portal") long portal,
        Pageable pageable
    );
    
    @Query(
        " SELECT t"
      + " FROM Template t"
      + " WHERE (:scope IS NULL OR t.scope = :scope)"
      + "    AND (t.id = :id)"
    )
    Template select(
        @Param("scope") Long scope,
        @Param("id") long id
    );
    
    @Query(
        " SELECT t"
      + " FROM Template t"
      + " WHERE (:scope IS NULL OR t.scope = :scope)"
      + "    AND (t.portal.id = :portal)"
      + "    AND (t.name = :name)"
    )
    Template select(
        @Param("scope") Long scope,
        @Param("portal") long portal,
        @Param("name") String name
    );
}
