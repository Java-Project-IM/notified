# Project Structure Documentation

## Overview

This project follows standard Maven conventions for a JavaFX application with clear separation of concerns.

## Directory Structure

```
notified/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── notif1ed/
│   │   │           ├── Notif1ed.java              # Main application entry point
│   │   │           ├── controller/                # UI Controllers (MVC pattern)
│   │   │           │   ├── HomepageController.java
│   │   │           │   ├── LandingPageController.java
│   │   │           │   ├── LoginController.java
│   │   │           │   ├── SignUpController.java
│   │   │           │   ├── StudentPageController.java
│   │   │           │   ├── StudentFormController.java
│   │   │           │   ├── SubjectPageController.java
│   │   │           │   ├── SubjectFormController.java
│   │   │           │   └── RecordsPageController.java
│   │   │           ├── model/                     # Data models/entities
│   │   │           │   ├── StudentEntry.java
│   │   │           │   ├── SubjectEntry.java
│   │   │           │   └── RecordEntry.java
│   │   │           └── util/                      # Utility classes
│   │   │               ├── DatabaseConnectionn.java
│   │   │               └── TestConnection.java
│   │   └── resources/
│   │       └── com/
│   │           └── notif1ed/
│   │               ├── view/                      # FXML view files
│   │               │   ├── LandingPage.fxml
│   │               │   ├── LogIn.fxml
│   │               │   ├── SignUp.fxml
│   │               │   ├── Homepage.fxml
│   │               │   ├── StudentPage.fxml
│   │               │   ├── StudentForm.fxml
│   │               │   ├── SubjectPage.fxml
│   │               │   ├── SubjectForm.fxml
│   │               │   ├── RecordsPage.fxml
│   │               │   ├── EmailPrompt.fxml
│   │               │   └── StartPage.fxml
│   │               ├── css/                       # Stylesheets
│   │               │   ├── homepage.css
│   │               │   ├── signup.css
│   │               │   ├── emailprompt.css
│   │               │   ├── recordspage.css
│   │               │   ├── studentpage.css
│   │               │   ├── subjectpage.css
│   │               │   └── table-styles.css
│   │               └── images/                    # Application images
│   │                   ├── logo.png
│   │                   └── silhouette.png
│   └── test/
│       └── java/
│           └── com/
│               └── notif1ed/                      # Unit tests (to be added)
├── database/                                       # Database scripts
│   ├── README.md
│   ├── schema.sql                                  # Database schema
│   ├── update_schema.sql                           # Schema updates
│   └── update_schema_simple.sql
├── .mvn/                                           # Maven wrapper config
├── target/                                         # Build output (generated)
├── pom.xml                                         # Maven project configuration
├── mvnw                                            # Maven wrapper (Linux/Mac)
├── mvnw.cmd                                        # Maven wrapper (Windows)
├── .gitignore                                      # Git ignore rules
├── README.md                                       # Project documentation
└── STRUCTURE.md                                    # This file
```

## Package Organization

### `com.notif1ed`

- **Main Package**: Contains the application entry point
- `Notif1ed.java`: JavaFX Application class that launches the app

### `com.notif1ed.controller`

- **MVC Controllers**: Handle UI logic and user interactions
- Each controller corresponds to a view (FXML file)
- Controllers manage form validation, navigation, and business logic

### `com.notif1ed.model`

- **Data Models**: Represent domain entities
- `StudentEntry`: Student data structure
- `SubjectEntry`: Subject/course data structure
- `RecordEntry`: Academic record data structure

### `com.notif1ed.util`

- **Utility Classes**: Reusable helper functions
- `DatabaseConnectionn`: Manages MySQL database connections
- `TestConnection`: Database connection testing utility

## Resource Loading

Resources are loaded using absolute paths from the classpath root:

```java
// FXML Views
FXMLLoader.load(getClass().getResource("/com/notif1ed/view/Homepage.fxml"));

// CSS Stylesheets
scene.getStylesheets().add(getClass().getResource("/com/notif1ed/css/homepage.css").toExternalForm());

// Images
new Image(getClass().getResourceAsStream("/com/notif1ed/images/logo.png"));
```

## Build System

### Maven

- **Build tool**: Apache Maven 3.6+
- **Java version**: 21 (LTS)
- **JavaFX version**: 21.0.5

### Key Maven Commands

```bash
# Compile the project
./mvnw clean compile

# Run the application
./mvnw javafx:run

# Package as JAR
./mvnw clean package

# Run tests (when implemented)
./mvnw test

# Clean build artifacts
./mvnw clean
```

## Design Patterns

### MVC (Model-View-Controller)

- **Model**: `com.notif1ed.model.*` - Data structures
- **View**: `src/main/resources/com/notif1ed/view/*.fxml` - UI layouts
- **Controller**: `com.notif1ed.controller.*` - UI logic

### Separation of Concerns

- **Business Logic**: Controllers
- **Data Access**: Utility classes (DatabaseConnection)
- **Presentation**: FXML + CSS
- **Configuration**: pom.xml, properties files

## Changes from Original Structure

### Removed (Redundant)

- ❌ `nbproject/` - NetBeans-specific build files
- ❌ `build.xml` - Ant build script
- ❌ `manifest.mf` - Replaced by Maven JAR plugin
- ❌ `build.ps1` / `run.ps1` - Replaced by `./mvnw` commands
- ❌ `mvnwDebug*` - Rarely used debug wrappers
- ❌ `notified_db.sql` (root) - Consolidated in `database/` folder

### Migrated to Standard Maven Layout

- ✅ `src/notif1ed/` → `src/main/java/com/notif1ed/`
- ✅ FXML/CSS/images → `src/main/resources/com/notif1ed/`
- ✅ Package structure reflects directory hierarchy
- ✅ All Java files updated with correct packages and imports

### Renamed for Consistency

- ✅ `Student Page.fxml` → `StudentPage.fxml` (removed space)
- ✅ `student page.css` → `studentpage.css` (removed space)
- ✅ `Notifyed_Logo.png` → `logo.png` (standardized)
- ✅ `Silhoutte log.png` → `silhouette.png` (fixed typo, removed space)
- ✅ `srcIMGs/` → `images/` (standard naming)

## Adding New Features

### Adding a New View

1. Create FXML file in `src/main/resources/com/notif1ed/view/`
2. Create Controller in `src/main/java/com/notif1ed/controller/`
3. Link them via `fx:controller="com.notif1ed.controller.YourController"`
4. Add CSS in `src/main/resources/com/notif1ed/css/` if needed

### Adding a New Model

1. Create class in `src/main/java/com/notif1ed/model/`
2. Define fields, constructors, getters/setters
3. Import in controllers: `import com.notif1ed.model.YourModel;`

### Adding Tests

1. Create test class in `src/test/java/com/notif1ed/`
2. Mirror the package structure (e.g., `controller`, `model`, `util`)
3. Add JUnit dependency to `pom.xml` if not present
4. Run with `./mvnw test`

## Best Practices

### Code Organization

- ✅ One public class per file
- ✅ Package names all lowercase
- ✅ Class names in PascalCase
- ✅ Methods in camelCase
- ✅ Constants in UPPER_SNAKE_CASE

### Resource Management

- ✅ Use try-with-resources for database connections
- ✅ Load resources from classpath (not file system)
- ✅ Use absolute paths for resources: `/com/notif1ed/...`

### Version Control

- ✅ Commit source files, not compiled artifacts
- ✅ `.gitignore` excludes `target/`, IDE files
- ✅ Use `git mv` to preserve file history when restructuring

## IDE Setup

### IntelliJ IDEA

1. Open as Maven project
2. Set JDK 21 in Project Structure
3. Enable annotation processing
4. Maven will auto-import dependencies

### Eclipse

1. Import → Existing Maven Project
2. Select JDK 21 in build path
3. Install JavaFX plugin if needed

### VS Code

1. Install "Extension Pack for Java"
2. Install "JavaFX Support"
3. Maven commands via Command Palette

## Troubleshooting

### Build fails with "package does not exist"

- Ensure all imports use `com.notif1ed.*` packages
- Run `./mvnw clean compile` to rebuild from scratch

### Resource not found errors

- Check resource paths use absolute format: `/com/notif1ed/view/File.fxml`
- Verify files exist in `src/main/resources/com/notif1ed/`

### JavaFX runtime errors

- Always run via `./mvnw javafx:run` (handles module path)
- Don't run compiled JAR directly without JavaFX on module path
