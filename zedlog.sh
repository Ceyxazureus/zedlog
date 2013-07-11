#!/bin/bash

CLASSPATH='lib/JNativeHook.jar:lib/litelogger-v0.1beta.jar:zedlog-v0.2beta1.jar'

java -classpath "$CLASSPATH" net.zeddev.zedlog.ZedLog
