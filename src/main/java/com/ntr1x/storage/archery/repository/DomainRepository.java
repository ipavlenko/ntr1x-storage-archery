package com.ntr1x.storage.archery.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntr1x.storage.archery.model.Domain;

public interface DomainRepository extends JpaRepository<Domain, Long> {

    @Query(
        " SELECT d"
      + " FROM Domain d"
      + " WHERE (:scope IS NULL OR d.scope = :scope)"
      + "    AND (:user IS NULL OR d.portal.user.id = :user)"
      + "   AND (:portal IS NULL OR d.portal.id = :portal)"
    )
    Page<Domain> query(
        @Param("scope") Long scope,
        @Param("user") Long user,
        @Param("portal") Long portal,
        Pageable pageable
    );

    @Query(
        " SELECT d"
      + " FROM Domain d"
      + " WHERE (:scope IS NULL OR d.scope = :scope)"
      + "    AND (d.id = :id)"
    )
    Domain select(@Param("scope") Long scope, @Param("id") long id);
    
    @Query(
        " SELECT d"
      + " FROM Domain d"
      + " WHERE (:scope IS NULL OR d.scope = :scope)"
      + "    AND (d.name = :name)"
    )
    Domain select(@Param("scope") Long scope, @Param("name") String name);
}
