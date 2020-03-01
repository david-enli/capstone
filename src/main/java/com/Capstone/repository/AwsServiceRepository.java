package com.Capstone.repository;

import com.Capstone.Model.Order;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableScan
@Repository
public interface AwsServiceRepository extends CrudRepository<Order, String> {

}