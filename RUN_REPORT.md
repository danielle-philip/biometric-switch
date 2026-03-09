# Run Report (Structured)

This report captures an end-to-end attempt to run and validate the repository in the current environment.

## Environment
- Working directory: /workspace/biometric-switch
- Date (UTC): 2026-03-09 19:21:11 UTC
- Shell: /bin/bash

## Execution Results

### Repo clean check
- Command:
```bash
git status --short
```
- Exit code: 0 (PASS)
- Output:
```text
 M RUN_REPORT.md
```

### Bash availability
- Command:
```bash
bash --version | head -n 1
```
- Exit code: 0 (PASS)
- Output:
```text
GNU bash, version 5.2.21(1)-release (x86_64-pc-linux-gnu)
```

### Java runtime availability
- Command:
```bash
java -version
```
- Exit code: 0 (PASS)
- Output:
```text
openjdk version "25.0.1" 2025-10-21
OpenJDK Runtime Environment (build 25.0.1+8-27)
OpenJDK 64-Bit Server VM (build 25.0.1+8-27, mixed mode, sharing)
```

### Gradle test run
- Command:
```bash
./gradlew test
```
- Exit code: 127 (FAIL/WARN)
- Output:
```text
bash: line 1: ./gradlew: No such file or directory
```

### Kotlin compile check
- Command:
```bash
kotlinc main.kt biometric.kt file_hidder.kt -d /tmp/biometric-switch.jar
```
- Exit code: 127 (FAIL/WARN)
- Output:
```text
bash: command not found: kotlinc
```

### Repository file inventory
- Command:
```bash
rg --files
```
- Exit code: 0 (PASS)
- Output:
```text
RUN_REPORT.md
ui.xml
file_hidder.kt
app.gradle
duress_policy.kt
TECHNICAL_REVIEW.md
main.kt
run_checks.sh
COERCION_RESISTANT_WHITEPAPER.md
biometric.kt
```

## Summary
- The project cannot be fully executed as an Android app in this environment because Gradle wrapper and Android project scaffolding are not present at repo root.
- Kotlin CLI compiler is not installed, so standalone Kotlin compilation could not be completed here.
- Java is present, but Android build/run pipeline is unavailable without wrapper/project structure.
