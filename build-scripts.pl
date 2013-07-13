#!/usr/bin/perl
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

# convert a whitespace separated file list to a Java classpath
sub classpathify {
	
	my $classpath = "";
	
	$classpath .= $_ . ":"
		foreach (@_);
	chop $classpath;
	
	return $classpath;
	
}

# the copyright notice
my $COPYRIGHT_NOTICE = 
'Copyright (C) 2013  Zachary Scott <zscott.dev@gmail.com>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.';

my $SCRIPT_TYPE = shift @ARGV; # either sh or bat

my $JAR_FILE = shift @ARGV;
my $MAIN_CLASS = shift @ARGV;

# remainder of args are the libraries
my @LIBS = @ARGV;

my $CLASS_PATH = classpathify($JAR_FILE, @LIBS);

# build the bash script
sub buildsh {

	# add the shebang line
	print "#!/bin/bash \n";
	
	# add the copyright notice
	my $copyright = $COPYRIGHT_NOTICE;
	$copyright =~ s/^(.*)$/# $1/gm;
	print "$copyright \n\n";

	print "CLASSPATH=\"$CLASS_PATH\" \n";
	print "java -classpath \"\$CLASSPATH\" $MAIN_CLASS \$\@ \n";
	
	print "\n";
	print "#";
	
}

# build the bat script (Windows)
sub buildbat {
	
	print "\@ECHO OFF \n";
	
	# add the copyright notice
	my $copyright = $COPYRIGHT_NOTICE;
	$copyright =~ s/^(.*)$/REM $1/gm;
	print "$copyright \n\n";

	my $classpath = $CLASS_PATH;
	$classpath =~ s/:/;/g; # convert to windows format
	
	print "SET CLASSPATH=$classpath \n";
	print "java -classpath \%CLASSPATH\% $MAIN_CLASS \n";
	
	print "\n";
	print "REM ";
	
}

# build the visual basic script (Windows)
sub buildvbs {
	
	# add the copyright notice
	my $copyright = $COPYRIGHT_NOTICE;
	$copyright =~ s/^(.*)$/' $1/gm;
	print "$copyright \n\n";

	my $classpath = $CLASS_PATH;
	$classpath =~ s/:/;/g; # convert to windows format
	
	print "Dim CLASSPATH \n";
	print "CLASSPATH = \"$classpath\" \n";
	print "\n";
	print "Dim ZEDLOG_CMD \n";
	print "ZEDLOG_CMD = \"java -classpath \" + CLASSPATH + \" $MAIN_CLASS\" \n";	
	print "\n";
	print "CreateObject (\"Wscript.Shell\").Run ZEDLOG_CMD, 0, true \n";
	print "\n";
	print "'";
	
}

# execute the appropriate script gen
if (lc($SCRIPT_TYPE) eq "sh") {
	print buildsh;
} elsif (lc($SCRIPT_TYPE) eq "bat") {
	print buildbat;
} elsif (lc($SCRIPT_TYPE) eq "vbs") {
	print buildvbs;
} else {
	print STDERR "Unknown script type $SCRIPT_TYPE \n";
	exit 1;	
}

