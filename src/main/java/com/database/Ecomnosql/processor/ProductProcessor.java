package com.database.Ecomnosql.processor;

import com.database.Ecomnosql.model.Product;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component("productProcessor")
public class ProductProcessor implements ItemProcessor<Product, Product> {

    @Override
    public Product process(Product item) {
        // Logic: If no sales yet and stock is high, give a 20% discount
        if (item.getSales() == 0 && item.getStock() > 50) {
            double discountedPrice = item.getPrice() * 0.80;

            // Rounding to 2 decimal places
            item.setPrice(Math.round(discountedPrice * 100.0) / 100.0);
            item.setStatus("PROMOTIONAL");

            System.out.println("🔥 DISCOUNT APPLIED: " + item.getProductName() + " | New Price: $" + item.getPrice());
        } else {
            item.setStatus("REGULAR");
            System.out.println("✅ PROCESSED: " + item.getProductName() + " | Price: $" + item.getPrice());
        }

        return item;
    }
}