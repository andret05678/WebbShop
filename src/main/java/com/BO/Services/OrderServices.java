package com.BO.Services;

import com.BO.Order;
import com.DB.imp.OrderDbImp;
import com.UI.Info.OrderInfo;
import com.UI.Info.ProductInfo;

import java.sql.SQLException;

public class OrderServices {
    public OrderServices() {

    }
    public OrderInfo placeOrder(ProductInfo product, int userId){
        try {
            Order existingOrder = OrderDbImp.findById(product.getId());
            if (existingOrder != null) {
                return null;
            }

            Order newOrder = Order.createOrder(0,userId,"Processing");
            boolean inserted = OrderDbImp.insert(newOrder);

            if (inserted) {
                return new OrderInfo(0,userId,"Processing");
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
