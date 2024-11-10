package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;
//interacts with the database and allows users to execute CRUD operations 
@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
}
