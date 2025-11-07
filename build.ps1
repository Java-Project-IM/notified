# Set JAVA_HOME for this session
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"

Write-Host "JAVA_HOME is set to: $env:JAVA_HOME" -ForegroundColor Green
Write-Host "Building project..." -ForegroundColor Cyan

# Build the project
.\mvnw.cmd clean package
