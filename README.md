# WebbShopp
labb1dist
+-------------------+     +-------------------+     +-------------------+
|     UserServices  |     |    PasswordUtil   |     |       User        |
+-------------------+     +-------------------+     +-------------------+
| - userDbImp       |     | - RAND: SecureRandom|   | - id: int         |
+-------------------+     | - SALT_BYTES: int  |   | - email: String   |
| + UserServices()  |     +-------------------+   | - username: String|
| + register()      |     | + generateSalt()   |   | - password: String|
| + login()         |     | + hashPassword()   |   | - roleId: int     |
+-------------------+     | + verifyPassword() |   +-------------------+
                          +-------------------+   | + createUser()    |
                                                  | + getters/setters |
+-------------------+                             +-------------------+
|    UserDbImp      |
+-------------------+     +-------------------+     +-------------------+
| - constructor     |     |    ProductDbImp   |     |     ProductInfo   |
+-------------------+     +-------------------+     +-------------------+
| + findByEmail()   |     | + getAllProducts()|     | - id: int         |
| + findById()      |     | + addProduct()    |     | - name: String    |
| + insert()        |     | + updateProduct() |     | - description:Str|
| + update()        |     | + deleteProduct() |     | - price: double   |
| + delete()        |     | + findById()      |     | - stockQuantity:int
| + getAllUsers()   |     +-------------------+     | - category: int   |
| + updateUserRole()|                               +-------------------+
| + deleteUser()    |                               | + getters/setters |
+-------------------+                               +-------------------+

+-------------------+     +-------------------+     +-------------------+
|    OrderDbImp     |     |     OrderInfo     |     |   OrderItemInfo   |
+-------------------+     +-------------------+     +-------------------+
| + getAllOrders()  |     | - id: int         |     | - id: int         |
| + getOrderItems() |     | - userId: int     |     | - orderId: int    |
| + updateOrderStatus()|  | - userEmail: String   | - productId: int  |
+-------------------+     | - status: String  |     | - productName:Str|
                          | - totalAmount: BigDecimal| - quantity: int |
+-------------------+     | - orderDate: Timestamp| - price: BigDecimal|
|     UserInfo      |     | - items: List<OrderItemInfo>| +-------------------+
+-------------------+     +-------------------+     
| - id: int         |     | + getters/setters |
| - username: String|     +-------------------+
| - role: int       |
+-------------------+
| + getters()       |
+-------------------+

+-------------------+     +-------------------+     +-------------------+
|   LoginServlet    |     |   TestPageServlet |     |    CartServlet    |
+-------------------+     +-------------------+     +-------------------+
| - userServices    |     | - doGet()         |     | - doGet()         |
+-------------------+     +-------------------+     | - doPost()        |
| + init()          |                               +-------------------+
| + doGet()         |     +-------------------+     
| + doPost()        |     |    AdminServlet   |     +-------------------+
| + handleLogin()   |     +-------------------+     |   StaffServlet    |
| + handleRegistration()| | - userServices    |     +-------------------+
+-------------------+     +-------------------+     | - doGet()         |
                    |     | + init()          |     | - doPost()        |
+-------------------+     | + doGet()         |     | + handle methods  |
|   LogoutServlet   |     | + doPost()        |     +-------------------+
+-------------------+     | + handleAddUser() |
| + doGet()         |     | + handleEditUser()|
+-------------------+     | + handleProductMethods()|
                          | + handleOrderMethods() |
                          +-------------------+



Servlet Layer (UI)
├── LoginServlet
│   ├── uses → UserServices
│   └── creates → UserInfo (session)
├── TestPageServlet
│   ├── uses → ProductDbImp
│   └── creates → cart items
├── CartServlet
│   └── manages → cart session
├── AdminServlet
│   ├── uses → UserServices, UserDbImp, ProductDbImp, OrderDbImp
│   └── creates → UserInfo, ProductInfo, OrderInfo
├── StaffServlet
│   ├── uses → OrderDbImp, ProductDbImp
│   └── creates → OrderInfo, ProductInfo
└── LogoutServlet

Services Layer (BO)
├── UserServices
│   ├── uses → PasswordUtil, UserDbImp
│   └── creates → User, UserInfo
└── PasswordUtil
    └── utilities for hashing

Data Access Layer (DB)
├── UserDbImp
│   └── extends → User
├── ProductDbImp
│   └── creates → ProductInfo
└── OrderDbImp
    └── creates → OrderInfo, OrderItemInfo

Model Classes (Info/BO)
├── User (BO)
├── UserInfo (UI)
├── ProductInfo (UI)
├── OrderInfo (UI)
└── OrderItemInfo (UI)

















                          
