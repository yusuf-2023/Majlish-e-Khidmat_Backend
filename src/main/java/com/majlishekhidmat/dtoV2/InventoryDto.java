package com.majlishekhidmat.dtoV2;



import lombok.Data;

@Data
public class InventoryDto {
    private Long id;
    private String itemName;
    private String category;
    private Long quantity;
    private String description;
}

