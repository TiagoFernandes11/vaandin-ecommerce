🛒 E-commerce Platform - Vaadin + Java
  This is a full-featured e-commerce platform built with Java and Vaadin. The project includes core features such as user authentication, product management, shopping cart functionality, order handling, and more.

🚀 Features
  Authentication and Authorization
  User login and role-based access control (admin/customer).
  Product & Category Management
  Full CRUD operations for products, categories, administrators, and orders.
  Pagination and filtering in all list views.
  Shopping Cart & Checkout
  Persistent cart per user session.
  Stock quantity is updated after order confirmation.
  Discount simulation and order summary.
  Order Management
  Order confirmation and order history.
  Order status updates.
  Email Notifications
  Password recovery via email.
  Order confirmation email after successful checkout.
  Address Lookup
  Automatically fetches address data from Brazilian ViaCEP API based on ZIP code (CEP).
  Payment Simulation. Simulates a simple payment workflow (no real payment gateway integration).

🧰 Tech Stack
  Backend: Java 17, Spring Boot, Spring Security, Spring Web
  Frontend: Vaadin Flow
  Database: MySQL
  Build Tool: Maven
  Email: JavaMailSender
  External API: ViaCEP
