package com.java.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.java.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

}
