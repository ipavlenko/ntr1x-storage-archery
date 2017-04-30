package com.ntr1x.storage.archery.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntr1x.storage.archery.model.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query(
        " SELECT s"
      + " FROM Store s"
      + " WHERE (:scope IS NULL OR s.scope = :scope)"
      + "   AND (:user IS NULL OR s.portal.user.id = :user)"
      + "   AND (:portal IS NULL OR s.portal.id = :portal)"
    )
    Page<Store> query(
        @Param("scope") Long scope,
        @Param("user") Long user,
        @Param("portal") Long portal,
        Pageable pageable
    );

    @Query(
        " SELECT s"
      + " FROM Store s"
      + " WHERE (:scope IS NULL OR s.scope = :scope)"
      + "    AND (s.id = :id)"
    )
    Store select(@Param("scope") Long scope, @Param("id") long id);
    
    @Query(
        " SELECT s"
      + " FROM Store s"
      + " WHERE (:scope IS NULL OR s.scope = :scope)"
      + "    AND (s.name = :name)"
    )
    Store select(@Param("scope") Long scope, @Param("name") String name);
}
