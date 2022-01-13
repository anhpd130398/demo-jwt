package com.example.repository;

import com.example.model.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {
    @Query(value = "select u from Users u where u.username= ?1 ")
    Users getByUserName(@Param("username") String username);

    @Query(value = "select u from Users u where u.username= ?1 ")
    List<Users> findByUserName(@Param("username") String username);

}
