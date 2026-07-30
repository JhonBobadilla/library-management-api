#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#    https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# Maven Start Up Batch script
#
# Required ENV vars:
# ------------------
#   JAVA_HOME - location of a JDK home dir
#
# Optional ENV vars
# -----------------
#   M2_HOME - location of maven2's installed home dir
#   MAVEN_OPTS - parameters passed to the Java VM when running Maven
#     e.g. to debug Maven itself, use
#       set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
#   MAVEN_SKIP_RC - flag to disable loading of mavenrc files
# ----------------------------------------------------------------------------

if [ -z "$MAVEN_SKIP_RC" ] ; then
  if [ -f /etc/mavenrc ] ; then
    . /etc/mavenrc
  fi
  if [ -f "$HOME/.mavenrc" ] ; then
    . "$HOME/.mavenrc"
  fi
fi

MAVEN_CMD_LINE_ARGS="$@"

# OS specific support
cygwin=false;
mingw=false;
darwin=false;
nonstop=false
case "`uname`" in
  CYGWIN* ) cygwin=true ;;
  MINGW* ) mingw=true;;
  Darwin* ) darwin=true ;;
  MINGW32* ) mingw=true;;
  MSYS* ) mingw=true;;
  NONSTOP* ) nonstop=true ;;
esac

# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

PRGDIR=`dirname "$PRG"`

# Check if Java is available
if [ -z "$JAVA_HOME" ] ; then
  echo "Error: JAVA_HOME not found in your environment."
  echo "Please set the JAVA_HOME variable in your environment to match the"
  echo "location of your Java installation."
  exit 1
fi

# For Cygwin or Mingw, ensure paths are in UNIX format
if $cygwin ; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
  [ -n "$CLASSPATH" ] && CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

if $mingw ; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME="`(cd "$JAVA_HOME"; pwd)`"
fi

# Determine the Maven wrapper jar
MAVEN_PROJECTBASEDIR=${MAVEN_PROJECTBASEDIR:-"$PRGDIR"}
WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

# Download maven-wrapper.jar if not present
if [ ! -f "$WRAPPER_JAR" ] ; then
    if [ -z "$MVNW_REPOURL" ] ; then
        MVNW_REPOURL=https://repo1.maven.org/maven2/
    fi
    DOWNLOAD_URL="$MVNW_REPOURL/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"

    if command -v wget > /dev/null 2>&1 ; then
        wget -O "$WRAPPER_JAR" "$DOWNLOAD_URL"
    elif command -v curl > /dev/null 2>&1 ; then
        curl -sS -o "$WRAPPER_JAR" "$DOWNLOAD_URL"
    else
        echo "Error: wget or curl not found. Cannot download maven-wrapper.jar."
        exit 1
    fi
fi

if [ ! -f "$WRAPPER_JAR" ] ; then
    echo "Error: maven-wrapper.jar not found. Please download it manually."
    exit 1
fi

# Execute Maven
exec "$JAVA_HOME/bin/java" $MAVEN_OPTS $MAVEN_DEBUG_OPTS \
    -classpath "$WRAPPER_JAR" \
    "-Dmaven.multiModuleProjectDirectory=$MAVEN_PROJECTBASEDIR" \
    $WRAPPER_LAUNCHER $MAVEN_CMD_LINE_ARGS
