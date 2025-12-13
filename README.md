# 📚 Library Management System

## 💡 Project Overview

This project is a **comprehensive system for managing a library's books, patrons, and borrowing transactions** developed as part of a university course assignment. It is built using the Java programming language and the Spring Boot framework to demonstrate **RESTful API design, database interaction, and the implementation of CRUD (Create, Read, Update, Delete) operations**. 

## ✨ Features

* **Book Management (CRUD):** Allows for adding new books, viewing the entire inventory, updating book details, and removing books.
* **Transaction Handling:** Tracks the borrowing and returning of books, including due dates.
* **Search & Filter:** Provides efficient ways to search for books by title or author, and patrons by ID or name.

## ⚙️ Technology Stack

| Category | Technology | Version (Optional) |
| :--- | :--- | :--- |
| **Language** | Java | 17+ |
| **Framework** | Spring Boot | 3.5.8 |
| **Database** |H2 Database| |
| **Build Tool** | Maven | |
| **Frontend (Optional)** | Basic HTML/Thymeleaf | |

## 🚀 Getting Started

Follow these steps to set up and run the project locally.

### Prerequisites

* Java Development Kit (JDK) 17/21
* Maven

### Installation

1.  **Clone the repository:**
    ```bash
    git clone [Your-Library-Repo-URL]
    cd library-management-system
    ```

2.  **Run the application:**
    * **Using Maven:**
        ```bash
        ./mvnw spring-boot:run
        ```
    * *Alternatively, run the main class `LibraryManagementSystemApplication.java` from your IDE.*

The application will be accessible at `http://localhost:8081`.

## 📌 API Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/books` | Retrieve a list of all books. |
| `POST` | `/api/books` | Create a new book record. |
| `GET` | `/api/books/{id}` | Retrieve a single book by ID. |
| `POST` | `/api/borrow/{bookId}/{BorrwerId}` | Record a book borrowing transaction. |

## 🎓 University Project Details

* **Course:** Object Oriented Programming
* **Semester/Year:** Semester 1, Year 2
* **Student Name:** Junaeed Bin Saif
* **Student ID:** 242-15-475

---
