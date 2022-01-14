package com.example.repository;

import com.example.model.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepo extends JpaRepository<Roles, Integer> {
    @Query(value = "select r from Roles  r where r.name = ?1")
    Roles getByName(@Param("name") String name);

    @Query(value ="select r from Roles  r where r.name = ?1")
    List<Roles> getByNameRoles(@Param("name") String name);
}
