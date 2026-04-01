# To-Do App (Java + MySQL + Swing)

This project is a desktop-based To-Do application developed with Java Swing, MySQL and JDBC.

## Features
- Add task
- List tasks
- Search tasks
- Filter pending tasks
- Mark selected task as completed
- Delete selected task
- Show task date
- Dark mode / Light mode support

## Technologies Used
- Java
- Java Swing
- MySQL
- JDBC

## Database
Database name: `todo_app`

Table name: `gorevler`

Example table structure:

```sql
CREATE TABLE gorevler (
    id INT AUTO_INCREMENT PRIMARY KEY,
    baslik VARCHAR(255),
    tamamlandi BOOLEAN DEFAULT FALSE,
    tarih TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
