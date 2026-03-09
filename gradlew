#!/usr/bin/env bash
set -euo pipefail

JAVA_HOME_OVERRIDE="/root/.local/share/mise/installs/java/21.0.2"
if [ -d "$JAVA_HOME_OVERRIDE" ]; then
  export JAVA_HOME="$JAVA_HOME_OVERRIDE"
  export PATH="$JAVA_HOME/bin:$PATH"
fi

exec gradle "$@"
