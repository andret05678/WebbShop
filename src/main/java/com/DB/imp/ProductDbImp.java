package com.DB.imp;

import com.BO.Product;

public class ProductDbImp extends Product {
    private ProductDbImp(int id, String name, String description, double price, int stock, int categoryId){
        super(id, name, description, price, stock, categoryId);
    }
}
