package com.DB.imp;

import com.BO.Product;
import com.BO.User;
import com.DB.supa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public static Collection findByName(String name) throws SQLException{
        Connection conn = supa.getConnection();
        Collection products = new ArrayList();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT WHERE NAME = '" + name + "'");
        while (rs.next()) {
            products.add(findById(rs.getInt("ID")));
        }
        return products;
    }

    public static List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection conn = supa.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, name, categoryid, description, price, stock, created_at FROM product ORDER BY id");

        while (rs.next()) {
            Product product = Product.createProduct(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getInt("categoryid")
            );
            products.add(product);
        }

        rs.close();
        stmt.close();
        conn.close();

        return products;
    }
}
