# Notif1ed

A JavaFX-based student notification and management system built with Java 21 and Maven.

## 🚀 Features

- Student record management
- Email notification system
- Subject tracking and management
- Database integration with MySQL
- Modern JavaFX user interface

## 📋 Prerequisites

Before running this application, ensure you have:

- **Java 21** (JDK 21 or later) - [Download here](https://www.oracle.com/java/technologies/downloads/#java21)
- **MySQL** database server
- **Git** (for cloning the repository)

## 🛠️ Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/toxicalchemist/notified.git
   cd notified
   ```

2. **Set up JAVA_HOME** (Windows)
   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
   [System.Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-21", "User")
   ```

3. **Configure MySQL Database**
   - Create a MySQL database for the application
   - Update database connection settings in `src/notif1ed/DatabaseConnectionn.java`

## ▶️ Running the Application

### Windows (PowerShell)

**Quick Run:**
```powershell
.\run.ps1
```

**Manual Run:**
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
.\mvnw.cmd javafx:run
```

### Linux/Mac

```bash
export JAVA_HOME=/path/to/jdk-21
./mvnw javafx:run
```

## 🔨 Building the Project

**Windows:**
```powershell
.\build.ps1
```

**Linux/Mac:**
```bash
./mvnw clean package
```

The compiled JAR file will be in the `target/` directory.

## 📦 Maven Commands

- **Compile:** `.\mvnw.cmd compile`
- **Run:** `.\mvnw.cmd javafx:run`
- **Package:** `.\mvnw.cmd package`
- **Clean:** `.\mvnw.cmd clean`
- **Test:** `.\mvnw.cmd test`

## 🏗️ Project Structure

```
Notif1ed/
├── src/
│   └── notif1ed/
│       ├── *.java           # Java source files
│       ├── *.fxml           # JavaFX UI layouts
│       └── *.css            # Stylesheets
├── pom.xml                  # Maven configuration
├── run.ps1                  # Quick run script (Windows)
├── build.ps1                # Build script (Windows)
└── README.md
```

## 🔧 Technology Stack

- **Java 21** (Latest LTS)
- **JavaFX 21.0.5** - UI Framework
- **Maven** - Build & Dependency Management
- **MySQL Connector/J 9.2.0** - Database connectivity
- **JavaMail 1.6.2** - Email functionality

## 📝 Configuration

### Database Configuration

Edit `src/notif1ed/DatabaseConnectionn.java` to configure your MySQL connection:

```java
// Update with your database credentials
String url = "jdbc:mysql://localhost:3306/your_database";
String username = "your_username";
String password = "your_password";
```

### Email Configuration

Configure email settings in the appropriate controller for email notifications.

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is open source and available under the MIT License.

## 👥 Authors

- **toxicalchemist** - [GitHub Profile](https://github.com/toxicalchemist)

## 🐛 Known Issues

- Some FXML files reference JavaFX API version 23.0.1 while running on 21.0.5 (minor warnings only)
- Missing CSS files for some views (Login.css, LandingPage.css)

## 📞 Support

If you encounter any issues or have questions, please open an issue on the [GitHub repository](https://github.com/toxicalchemist/notified/issues).