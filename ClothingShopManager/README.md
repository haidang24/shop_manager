# Clothing Shop Manager

A Java Swing application for managing a clothing shop's inventory.

## Features

- Add, edit, and delete products
- Search products by name or category
- View all products in a table format
- Input validation and error handling
- Modern and user-friendly interface

## Prerequisites

- Java 11 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/clothing-shop-manager.git
cd clothing-shop-manager
```

2. Create the database and tables:
- Open MySQL command line or your preferred MySQL client
- Run the SQL script in `sql/create_database.sql`

3. Configure the database connection:
- Open `src/main/resources/database.properties`
- Update the database URL, username, and password if needed

4. Build the project:
```bash
mvn clean package
```

## Running the Application

After building the project, you can run it using:

```bash
java -jar target/clothing-shop-manager-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Or run it directly from your IDE by executing the `main` method in `com.shop.ui.MainWindow` class.

## Usage

1. **View Products**
   - All products are displayed in a table when the application starts
   - Click on column headers to sort the table

2. **Search Products**
   - Select search type (Name or Category)
   - Enter search text
   - Click "Search" or press Enter
   - Clear the search field and click "Search" to show all products

3. **Add Product**
   - Click "Add Product"
   - Fill in all required fields
   - Click "Save"

4. **Edit Product**
   - Select a product from the table
   - Click "Edit"
   - Modify the fields
   - Click "Save"

5. **Delete Product**
   - Select a product from the table
   - Click "Delete"
   - Confirm deletion

## Development

The project uses the following structure:

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── shop/
│   │           ├── dao/         # Data Access Objects
│   │           ├── models/      # Domain Models
│   │           ├── ui/          # User Interface
│   │           └── utils/       # Utilities
│   └── resources/
│       └── database.properties  # Database Configuration
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.