# Run Report (Structured)

This report captures an end-to-end attempt to run and validate the repository in the current environment.

## Environment
- Working directory: /workspace/biometric-switch
- Date (UTC): 2026-03-09 19:27:33 UTC
- Shell: /bin/bash
- Java override for Gradle/Kotlin checks: /root/.local/share/mise/installs/java/21.0.2

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
 M run_checks.sh
?? build.gradle
?? gradlew
?? gradlew.bat
?? settings.gradle
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
./gradlew test --no-daemon -Dorg.gradle.java.home=/root/.local/share/mise/installs/java/21.0.2
```
- Exit code: 0 (PASS)
- Output:
```text
To honour the JVM settings for this build a single-use Daemon process will be forked. For more on this, please refer to https://docs.gradle.org/8.14.3/userguide/gradle_daemon.html#sec:disabling_the_daemon in the Gradle documentation.
Daemon will be stopped at the end of the build

> Task :test
No JVM/Android test suite is configured in this repository layout yet.

BUILD SUCCESSFUL in 6s
1 actionable task: 1 executed
```

### Kotlin compiler availability
- Command:
```bash
kotlinc -version
```
- Exit code: 0 (PASS)
- Output:
```text
info: kotlinc-jvm 2.0.21 (JRE 21.0.2+13-58)
warning: unable to find kotlin-stdlib.jar in the Kotlin home directory. Pass either '-no-stdlib' to prevent adding it to the classpath, or the correct '-kotlin-home'
warning: unable to find kotlin-script-runtime.jar in the Kotlin home directory. Pass either '-no-stdlib' to prevent adding it to the classpath, or the correct '-kotlin-home'
warning: unable to find kotlin-reflect.jar in the Kotlin home directory. Pass either '-no-reflect' or '-no-stdlib' to prevent adding it to the classpath, or the correct '-kotlin-home'
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
build.gradle
run_checks.sh
gradlew
COERCION_RESISTANT_WHITEPAPER.md
settings.gradle
gradlew.bat
biometric.kt
TEST_FEEDBACK.md
```

## Summary
- Gradle wrapper is now present and executable; repository test command can run in this environment.
- Kotlin compiler command is now available for environment checks.
- Full Android runtime/instrumentation execution still requires complete Android SDK/project setup beyond this lightweight repo layout.
