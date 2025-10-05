<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*, com.example.Product" %>
<%@ page import="com.UI.Info.ProductInfo" %>
<html>
<head>
    <title>Your Cart</title>
</head>
<body>
<h2>Shopping Cart</h2>
<a href="store">Continue Shopping</a><br><br>

<%
    List<ProductInfo> cart = (List<ProductInfo>)request.getAttribute("cart");
    if(cart.isEmpty()){
%>
<p>Your cart is empty.</p>
<% } else { %>
<table border="1" cellpadding="5">
    <tr><th>Product</th><th>Price</th><th>Action</th></tr>
    <%
        double total = 0;
        for(ProductInfo p : cart){
            total += p.getPrice();
    %>
    <tr>
        <td><%= p.getName() %></td>
        <td>$<%= p.getPrice() %></td>
        <td>
            <form action="cart" method="post">
                <input type="hidden" name="id" value="<%= p.getId() %>">
                <input type="hidden" name="action" value="remove">
                <input type="submit" value="Remove">
            </form>
        </td>
    </tr>
    <% } %>
    <tr><td colspan="2">Total</td><td>$<%= total %></td></tr>
</table>
<% } %>
</body>
</html>
