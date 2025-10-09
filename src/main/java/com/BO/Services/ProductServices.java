package com.BO.Services;

import com.DB.imp.ProductDbImp;
import com.DB.supa;
import com.UI.Info.ProductInfo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductServices {


    public static boolean isProductInStock(int productId) throws SQLException {
        return ProductDbImp.isProductInStock(productId);
    }

    public static void updateProductStock(int productId) throws SQLException {
        ProductDbImp.updateProductStock(productId);
    }
    public static List<ProductInfo> getAllProducts() throws SQLException {
        return ProductDbImp.getAllProducts();
    }


}
