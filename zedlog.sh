#!/bin/bash 
# 
# Copyright (C) 2013  Zachary Scott <zscott.dev@gmail.com>
# 
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
 
CLASSPATH="bin/zedlog-v0.2beta1.jar:lib/JNativeHook.jar:lib/litelogger-v0.1beta.jar" 
java -classpath "$CLASSPATH" "net.zeddev.zedlog.ZedLog" 

1