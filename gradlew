#!/usr/bin/env sh

DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
JAVA_21_HOME="/usr/local/sdkman/candidates/java/21.0.10-ms"
if [ -x "$JAVA_21_HOME/bin/java" ]; then
  JAVA_CMD="$JAVA_21_HOME/bin/java"
elif [ -n "$JAVA_HOME" ]; then
  JAVA_CMD="$JAVA_HOME/bin/java"
else
  JAVA_CMD="java"
fi

exec "$JAVA_CMD" -classpath "$DIR/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
