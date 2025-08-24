package com.majlishekhidmat.serviceV2;



import com.majlishekhidmat.dtoV2.InventoryDto;
import java.util.List;

public interface InventoryService {
    InventoryDto createInventory(InventoryDto dto);
    InventoryDto updateInventory(Long id, InventoryDto dto);
    void deleteInventory(Long id);
    InventoryDto getInventoryById(Long id);
    List<InventoryDto> getAllInventory();
}

