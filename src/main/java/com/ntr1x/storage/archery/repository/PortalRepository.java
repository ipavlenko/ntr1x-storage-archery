package com.ntr1x.storage.archery.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntr1x.storage.archery.model.Portal;

public interface PortalRepository extends JpaRepository<Portal, Long> {

    @Query(
        " SELECT p"
      + " FROM Portal p"
      + " WHERE (:scope IS NULL OR p.scope = :scope)"
      + "   AND (:shared IS NULL OR p.shared = :shared)"
      + "   AND (:user IS NULL OR p.user.id = :user)"
    )
    Page<Portal> query(
        @Param("scope") Long scope,
        @Param("shared") Boolean shared,
        @Param("user") Long user,
        Pageable pageable
    );
    
    @Query(
        " SELECT p"
      + " FROM Portal p"
      + " WHERE (:scope IS NULL OR p.scope = :scope)"
      + "   AND p.id = :id"
    )
    Portal select(
        @Param("scope") Long scope,
        @Param("id") Long id
    );
}
