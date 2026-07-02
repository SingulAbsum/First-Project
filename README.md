# Store Management System (JavaFX + SQLite)

A desktop-based **Store Management System** developed in **Java** using **JavaFX** for the graphical interface and **SQLite** for persistent data storage. The application simulates the daily operations of a retail business by providing dedicated workflows for administrators, managers, and cashiers through a role-based architecture.

---

# Project Overview

The system is designed to digitize common retail management tasks, including employee administration, inventory management, billing, supplier management, financial tracking, and statistical reporting.

Rather than functioning as a collection of isolated assignments, the application integrates these features into a single desktop environment where multiple user roles collaborate through different permission levels.

The project emphasizes:

* Object-Oriented Programming principles
* MVC-inspired project organization
* JavaFX desktop application development
* SQLite database integration
* Role-based access control
* Business workflow implementation
* CRUD operations across multiple entities
* Practical software engineering practices

---

# Running the Project

Open a terminal in:

```text
Model1(first Year edition) (3)\Model1 (1)\Model
```

Then run:

```powershell
mvn javafx:run
```

To run the regression suite:

```powershell
mvn test
```


# Main Features

### Administrator

* Manage administrator accounts
* Manage manager accounts
* Manage cashier accounts
* Edit and delete employee records
* Generate statistical reports
* Manage user permissions

### Manager

* Product management
* Inventory control
* Supplier management
* Expense tracking
* Employee monitoring
* Business reporting

### Cashier

* Customer billing
* Sales transactions
* Receipt generation
* Product lookup
* Daily sales workflow

---

# Technology Stack

| Technology  | Purpose                          |
| ----------- | -------------------------------- |
| Java        | Core application logic           |
| JavaFX      | Desktop graphical user interface |
| SQLite      | Embedded relational database     |
| JDBC        | Database communication           |
| Eclipse IDE | Primary development environment  |

---

# Project Architecture

The project follows a modular structure separating different application responsibilities.

```
Model
│
├── model/
│   ├── Business entities
│   ├── Database models
│   └── Domain objects
│
├── control/
│   ├── Event handlers
│   ├── UI controllers
│   └── Business logic
│
├── view/
│   ├── JavaFX layouts
│   └── User interface components
│
└── SQLite Database
```

Although originally developed using a traditional JavaFX structure, the project is organized around an MVC-inspired approach to separate presentation, application logic, and data management.

---

# Learning Objectives

This project demonstrates experience with:

* Java object-oriented programming
* Class design and inheritance
* Event-driven programming
* GUI application development
* Database design and SQL
* JavaFX scene management
* File handling
* User authentication
* CRUD operations
* Software modularization
* Business logic implementation

---
