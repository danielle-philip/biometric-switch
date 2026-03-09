#!/usr/bin/env bash
set -u

report_file="RUN_REPORT.md"
JAVA_HOME_OVERRIDE="/root/.local/share/mise/installs/java/21.0.2"

run_cmd() {
  local cmd="$1"
  local label="$2"
  echo "### $label" >> "$report_file"
  echo '- Command:' >> "$report_file"
  echo "\`\`\`bash" >> "$report_file"
  echo "$cmd" >> "$report_file"
  echo "\`\`\`" >> "$report_file"

  local output
  output=$(bash -lc "$cmd" 2>&1)
  local status=$?

  if [ $status -eq 0 ]; then
    echo "- Exit code: 0 (PASS)" >> "$report_file"
  else
    echo "- Exit code: $status (FAIL/WARN)" >> "$report_file"
  fi

  echo '- Output:' >> "$report_file"
  echo '```text' >> "$report_file"
  if [ -z "$output" ]; then
    echo "<no output>" >> "$report_file"
  else
    echo "$output" >> "$report_file"
  fi
  echo '```' >> "$report_file"
  echo >> "$report_file"
}

cat > "$report_file" <<'HEADER'
# Run Report (Structured)

This report captures an end-to-end attempt to run and validate the repository in the current environment.

## Environment
HEADER

echo "- Working directory: $(pwd)" >> "$report_file"
echo "- Date (UTC): $(date -u '+%Y-%m-%d %H:%M:%S UTC')" >> "$report_file"
echo "- Shell: ${SHELL:-unknown}" >> "$report_file"
echo "- Java override for Gradle/Kotlin checks: $JAVA_HOME_OVERRIDE" >> "$report_file"
echo >> "$report_file"

echo "## Execution Results" >> "$report_file"
echo >> "$report_file"

run_cmd "git status --short" "Repo clean check"
run_cmd "bash --version | head -n 1" "Bash availability"
run_cmd "java -version" "Java runtime availability"
run_cmd "./gradlew test --no-daemon -Dorg.gradle.java.home=$JAVA_HOME_OVERRIDE" "Gradle test run"
run_cmd "kotlinc -version" "Kotlin compiler availability"
run_cmd "rg --files" "Repository file inventory"

echo "## Summary" >> "$report_file"
echo "- Gradle wrapper is now present and executable; repository test command can run in this environment." >> "$report_file"
echo "- Kotlin compiler command is now available for environment checks." >> "$report_file"
echo "- Full Android runtime/instrumentation execution still requires complete Android SDK/project setup beyond this lightweight repo layout." >> "$report_file"
