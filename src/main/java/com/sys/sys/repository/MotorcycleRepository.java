package com.sys.sys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sys.sys.model.Motorcycle;
import com.sys.sys.model.Yard;

public interface MotorcycleRepository extends JpaRepository<Motorcycle, Long>{
     long countByPatio(Yard patio);
}
