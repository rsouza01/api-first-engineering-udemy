package com.rsouza01.apifirst_server.repositories;

import org.springframework.data.repository.CrudRepository;
import java.util.UUID;

import com.rsouza01.apifirst.model.CustomerDto;

public interface CustomerRepository extends CrudRepository<CustomerDto, UUID> {

}
