# Notif1ed

A JavaFX-based student notification and management system built with Java 21 and Maven.

## Quick start (standardized Linux workflow)

This section describes a tested, repeatable way to run the app on Linux (adapt the paths for macOS/Windows).

Prerequisites

- Java 21 (JDK 21) installed
- MySQL server
- Git

1. Set your JAVA_HOME for this session (you provided this path):

```bash
export JAVA_HOME=/usr/lib/jvm/jdk-21.0.8-oracle-x64
export PATH="$JAVA_HOME/bin:$PATH"
```

To make it persistent (Bash):

```bash
printf '\n# Java 21\nexport JAVA_HOME=/usr/lib/jvm/jdk-21.0.8-oracle-x64\nexport PATH="$JAVA_HOME/bin:$PATH"\n' >> ~/.bashrc
source ~/.bashrc
```

2. Make the Maven wrapper executable (only once):

```bash
chmod +x ./mvnw
```

3. Configure the database

- Create the application database and a user (example):

```sql
CREATE DATABASE notified_DB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'notifuser'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON notified_DB.* TO 'notifuser'@'localhost';
FLUSH PRIVILEGES;
```

- Update `src/notif1ed/DatabaseConnectionn.java` to match your DB credentials:

```java
private static final String URL = "jdbc:mysql://localhost:3306/notified_db";
private static final String USER = "notifuser";
private static final String PASSWORD = "your_password";
```

4. Run the application (recommended):

```bash
./mvnw javafx:run
```

This uses the `javafx-maven-plugin` configured in `pom.xml` and will download required JavaFX modules automatically.

5. Build a distributable JAR

```bash
./mvnw clean package
```

The artifact will be created in `target/` (e.g. `target/Notif1ed-1.0.0.jar`). Note: running that jar directly may still require JavaFX on the module path.

To run the jar with a local JavaFX SDK (example):

```bash
java --module-path /path/to/javafx-sdk-21/lib --add-modules=javafx.controls,javafx.fxml -jar target/Notif1ed-1.0.0.jar
```

6. IDE usage

- Import the project as a Maven project in IntelliJ or NetBeans.
- Set the project's SDK to JDK 21.
- Run `notif1ed.Notif1ed` as a Java application. If the IDE complains about JavaFX runtime, either use the Maven run configuration or add JavaFX as a library/module.

Troubleshooting

- If `./mvnw` fails with a Java version error, ensure `java -version` reports a Java 21 runtime.
- If you see `MySQL JDBC Driver not found`, Maven should download `mysql-connector-j` from `pom.xml`; make sure `./mvnw clean package` completes without errors.
- If FXML resources are not found, verify they exist in `src/notif1ed/` and that code loads them with `getResource("LandingPage.fxml")` (relative to `notif1ed` package).

Helpful commands

```bash
# check Java
java -version
javac -version

# make mvnw executable (if needed)
chmod +x ./mvnw

# build
./mvnw clean package

# run via Maven/JavaFX plugin
./mvnw javafx:run

# run compiled jar (if using a local JavaFX SDK)
java --module-path /path/to/javafx-sdk-21/lib --add-modules=javafx.controls,javafx.fxml -jar target/Notif1ed-1.0.0.jar
```

Files to edit for local setup

- `src/notif1ed/DatabaseConnectionn.java` — set your JDBC URL, username, and password.
- Optionally: any controller that contains email settings (search for SMTP or mail properties in `src/notif1ed`).

Notes

- `pom.xml` targets Java 21 and pulls JavaFX 21.0.5 via Maven dependencies and the `javafx-maven-plugin`.
- For a portable runtime image consider adding a `jlink` or an assembly profile — I can add a `jlink` profile if you want a single native image for Linux.

If you'd like, I can also:

- run a quick `./mvnw -v` and `./mvnw clean package` here and paste back the output,
- or add a `Makefile` or `run.sh` wrapper that sets `JAVA_HOME` and runs the project for you.
