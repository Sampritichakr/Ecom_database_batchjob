package com.database.Ecomnosql.model;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import java.util.UUID;

@Data // <-- THIS IS THE MAGIC WORD!
@Table("products")
public class Product {
    @PrimaryKey
    private UUID productId;
    private String productName;
    private double price;
    private int stock;
    private int sales;
    private String status;
}