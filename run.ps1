# Set JAVA_HOME for this session
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"

Write-Host "JAVA_HOME is set to: $env:JAVA_HOME" -ForegroundColor Green
Write-Host "Running Notif1ed application..." -ForegroundColor Cyan

# Compile first to ensure resources are copied
.\mvnw.cmd compile

# Run the JavaFX application
.\mvnw.cmd javafx:run
