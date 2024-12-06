# Library Management System

## 📚 Overview
The **Library Management System** is a software application designed to streamline the management of library resources, including books, users, loans, and returns. This system is for libraries using MySQL databases, offering efficient workflows and enhanced user experience.

---

## ✨ Features
### Admins:

- **Book Management**: Add, update, delete, and search for books.
- **User Management**: Manage library users.
- **Loan and Return**: Track book checkouts and returns.
- **Documents Search**: Search and add news books by title, author, genre, ISBN or APIs.
- **Controlling**: Control documents and users through charts.

### Users:

- **Borrow Books**: Add and return books.
- **Rating**: Comment and evaluate documents.
- **Get suggestions**: Get suggestions for new materials and recommended reading.
- **View history**: View the history of borrowed books and the status of books being borrowed.

### Application Supports:
- APIs search.
- Light-Dark mode.
- Using *AWS RDS* as a database.
- Multithreading.
- Rating and commenting documents.
- JUnit test.

---

## 💻 Installation

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/NguyenKhoa311/Library_management.git
   cd library-management-system
   ```

2. Configure the database in the `ConnectJDBC.java`.
    ``` bash
   DB_URL = "jdbc:mysql://your_database";
   DB_USERNAME = "your_username";
   DB_PASSWORD = "your_password";
   ```
3. Build the application.
4. Run application.
   ``` bash
    java -jar target/Library_management.jar
   ```
---

## 🛠️ Technologies Used
- **Frontend**: CSS, JavaFx
- **Backend**: Java
- **Database**: MySQL

---

## 🚀 Usage
### Admins:
1. **Dashboard**: See the management summary chart for documents and users.
2. **Profile**: Monitor and edit user profiles.
3. **Users**: Add, edit, delete and manage user accounts.
4. **Documents**: Add, edit, delete and manage documents.
5. **Search docs**: Search and add new documents to the library.

### Users:
1. **Dashboard**: Follow the summary of the borrowed documents. New books and books to borrow suggestion.
2. **Profile**: Monitor and edit user profiles.
3. **Documents**: Search and borrow documents.
4. **History**: View book borrowing history and borrowed book status.

---

## 📝 Creators

### Nguyen Van Khoa: 
- **Role**: Project manager, Backend developer, Database designer.
- **Contact**:
  - Email: khoabeo651@gmail.com
  - Facebook: https://www.facebook.com/profile.php?id=100014293383493
  - GitHub: https://github.com/NguyenKhoa311
### Do Ngoc Khanh:
- **Role**: Frontend developer, UX/UI designer, database manager.
- **Contact**:
    - Email: dongockhanh.vn@gmail.com
    - Facebook: https://www.facebook.com/profile.php?id=100015122586295
    - GitHub: https://github.com/Xipog77
### Nguyen Huy Hoang:
- **Role**: Project analyst, JUnit developer, Tester.
- **Contact**:
    - Email: huyhoangnguyen5405@gmail.com
    - Facebook: https://www.facebook.com/profile.php?id=100012702061324
    - GitHub: https://github.com/Sfyelf
---

## References
- ChatGPT
- Claude
- Gemini
- Stack Overflow
- Color Hunt