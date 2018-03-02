#!/bin/sh

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

BIN_DIR=`dirname "$PRG"`
PRGDIR=$BIN_DIR/..

# java运行参数设置
JAVA_OPTS=" -server -Xms1g -Xmx2g"
LIB_DIR=$PRGDIR/lib
LOG_PATH=$PRGDIR/config/logback.xml

# executable
java $JAVA_OPTS -Dlogback.configurationFile=file:$LOG_PATH \
-Djava.ext.dirs=$LIB_DIR \
-Dfile.encoding=UTF-8 \
$1