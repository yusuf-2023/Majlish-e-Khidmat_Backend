package com.majlishekhidmat.repositoryV2;



import com.majlishekhidmat.entityV2.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
