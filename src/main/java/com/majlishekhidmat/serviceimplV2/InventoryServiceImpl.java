package com.majlishekhidmat.serviceimplV2;



import com.majlishekhidmat.dtoV2.InventoryDto;
import com.majlishekhidmat.entityV2.Inventory;
import com.majlishekhidmat.repositoryV2.InventoryRepository;
import com.majlishekhidmat.serviceV2.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public InventoryDto createInventory(InventoryDto dto) {
        Inventory inventory = Inventory.builder()
                .itemName(dto.getItemName())
                .category(dto.getCategory())
                .quantity(dto.getQuantity())
                .description(dto.getDescription())
                .build();

        Inventory saved = inventoryRepository.save(inventory);
        dto.setId(saved.getId());
        return dto;
    }

    @Override
    public InventoryDto updateInventory(Long id, InventoryDto dto) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventory.setItemName(dto.getItemName());
        inventory.setCategory(dto.getCategory());
        inventory.setQuantity(dto.getQuantity());
        inventory.setDescription(dto.getDescription());

        inventoryRepository.save(inventory);
        dto.setId(inventory.getId());
        return dto;
    }

    @Override
    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }

    @Override
    public InventoryDto getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        InventoryDto dto = new InventoryDto();
        dto.setId(inventory.getId());
        dto.setItemName(inventory.getItemName());
        dto.setCategory(inventory.getCategory());
        dto.setQuantity(inventory.getQuantity());
        dto.setDescription(inventory.getDescription());
        return dto;
    }

    @Override
    public List<InventoryDto> getAllInventory() {
        return inventoryRepository.findAll().stream().map(i -> {
            InventoryDto dto = new InventoryDto();
            dto.setId(i.getId());
            dto.setItemName(i.getItemName());
            dto.setCategory(i.getCategory());
            dto.setQuantity(i.getQuantity());
            dto.setDescription(i.getDescription());
            return dto;
        }).collect(Collectors.toList());
    }
}

