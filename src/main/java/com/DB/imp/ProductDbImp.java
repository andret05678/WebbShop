package com.DB.imp;

import com.BO.Product;
import com.DB.supa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductDbImp extends Product {
    private ProductDbImp(int id, String name, String description, double price, int stock, int categoryId){
        super(id, name, description, price, stock, categoryId);
    }
    public static ProductDbImp findById(int id) throws SQLException
    {
        Connection conn = supa.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT WHERE ID = " + id);
        rs.next();
        String name = rs.getString("NAME");
        String description = rs.getString("DESCRIPTION");
        double price = rs.getDouble("PRICE");
        int stock = rs.getInt("STOCK");
        int categoryId = rs.getInt("CATEGORYID");
        return new ProductDbImp(id, name, description, price, stock, categoryId);
    }
}
