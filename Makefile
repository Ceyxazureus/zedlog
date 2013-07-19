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

##### PROJECT DETAILS  #########################################################

NAME = zedlog
VERSION = v0.2beta3

##### BUILD TOOLS  #############################################################

# java build tools
JAVAC = javac
JAR = jar

# pod documentation tools
POD2TEXT = pod2text
POD2HTML = pod2html
POD2PDF = pod2pdf

TAR = tar
BZIP2 = bzip2

##### DIRECTORIES  #############################################################

BIN_DIR = bin
LIB_DIR = lib

SRC_DIR = src
TEST_DIR = test

PACKAGE_DIR = net/zeddev/zedlog

INSTALL_RSRC_DIR = installrsrc
	# the installation resource directory (in the package directory)

##### CODE COMPILATION  ########################################################

# the main source file (which depends on ALL others)
SOURCE := ZedLog.java

# the compiled class file name
CLASS_FILES := $(SOURCE:.java=.class)

# change source and output directories
SOURCE := $(addprefix $(SRC_DIR)/$(PACKAGE_DIR)/, $(SOURCE))
CLASS_FILES := $(addprefix $(BIN_DIR)/$(PACKAGE_DIR)/, $(CLASS_FILES))

# icon set resource
RSRC_ICONS := gui/icons
RSRC_ICONS_SRC = $(SRC_DIR)/$(PACKAGE_DIR)/$(RSRC_ICONS)
RSRC_ICONS_DEST = $(BIN_DIR)/$(PACKAGE_DIR)/$(RSRC_ICONS)

# embedded documentation resource
RSRC_DOCS := doc
RSRC_DOCS_SRC = $(SRC_DIR)/$(PACKAGE_DIR)/$(RSRC_DOCS)
RSRC_DOCS_DEST = $(BIN_DIR)/$(PACKAGE_DIR)/$(RSRC_DOCS)

# external library JARs
LIBS := JNativeHook.jar litelogger-v0.1beta.jar
LIBS := $(addprefix $(LIB_DIR)/, $(LIBS))
LIBS_CLASSPATH = $(shell perl classpathify.pl $(LIBS))

# the jar executable
JAR_FILE = $(BIN_DIR)/$(NAME)-$(VERSION).jar
MAIN_CLASS = net.zeddev.zedlog.ZedLog 

# other files to include in the jar file
OTHER_FILES = COPYING_GPL.html README.html CHANGES.html

##### TEST SUITE  ##############################################################

# the test suite source
TEST_SOURCE := TestSuite.java

# the compiled test suite class file name
TEST_CLASSES := $(TEST_SOURCE:.java=.class)

# the test suite dep libraries (just JUnit)
TEST_LIBS := junit.jar hamcrest-core.jar
TEST_LIBS := $(addprefix $(LIB_DIR)/, $(TEST_LIBS))
TEST_LIBS_CLASSPATH = $(shell perl classpathify.pl $(TEST_LIBS))

# change source and output directories
TEST_SOURCE := $(addprefix $(TEST_DIR)/$(PACKAGE_DIR)/, $(TEST_SOURCE))
TEST_CLASSES := $(addprefix $(BIN_DIR)/$(PACKAGE_DIR)/, $(TEST_CLASSES))

TEST_SUITE = net.zeddev.zedlog.TestSuite

#####  DOCUMENTATION  ##########################################################

# the raw pod documentation to build
README = README.pod
COPYING = COPYING_GPL.pod
CHANGES = CHANGES.pod

# output documentation
README_OUTPUT = $(README:.pod=.html) $(README:.pod=.pdf)
COPYING_OUTPUT = $(COPYING:.pod=.html) $(COPYING:.pod=.pdf)
CHANGES_OUTPUT = $(CHANGES:.pod=.html) $(CHANGES:.pod=.pdf)
DOC_OUTPUT = $(README_OUTPUT) $(COPYING_OUTPUT) $(CHANGES_OUTPUT)

# pod options
POD2HTML_FLAGS = --noindex

#####  DISTRIBUTION ARCHIVE  ###################################################

DIST_NAME = $(NAME)-$(VERSION)
DIST_FILE = $(DIST_NAME).tar.bz2

SCRIPTS = zedlog.sh zedlog.bat zedlog.vbs

DIST_FILES = $(JAR_FILE) $(LIB_DIR) README.html COPYING_GPL.html CHANGES.html \
$(SCRIPTS)

#####  INSTALLER  ##############################################################

# the main installer source file (which depends on ALL others)
INSTALLER_SOURCE := InstallerMain.java

# the compiled class file name
INSTALLER_CLASS_FILES := $(INSTALLER_SOURCE:.java=.class)

# change source and output directories
INSTALLER_SOURCE := $(addprefix $(SRC_DIR)/$(PACKAGE_DIR)/, $(INSTALLER_SOURCE))
INSTALLER_CLASS_FILES := $(addprefix $(BIN_DIR)/$(PACKAGE_DIR)/, $(INSTALLER_CLASS_FILES))

# the files to be installed, by the installer
INSTALL_FILES = COPYING_GPL.txt $(DIST_FILES) # defined above in distro archive section
INSTALL_RSRC = $(BIN_DIR)/$(PACKAGE_DIR)/$(INSTALL_RSRC_DIR)

# the jar executable
INSTALLER_JAR = $(BIN_DIR)/$(NAME)-$(VERSION)-installer.jar
INSTALLER_MAIN = net.zeddev.zedlog.InstallerMain 

##### BUILD TARGETS  ###########################################################

.PHONY: all build doc resources rebuild test clean_class_files clean dist installer

all: build doc

build: test $(JAR_FILE) $(SCRIPTS)

doc: $(DOC_OUTPUT)

# copy resources to output directory
# NOTE Must be called AFTER classes have been compiled!
resources:
	@echo ">>>>> Copying Resources <<<<<"
	cp -r $(RSRC_ICONS_SRC) $(RSRC_ICONS_DEST) >/dev/null
	cp -r $(RSRC_DOCS_SRC) $(RSRC_DOCS_DEST) >/dev/null

rebuild: clean build

# build and run the test suite
test : $(TEST_CLASSES)
	java -classpath $(LIBS_CLASSPATH):$(TEST_LIBS_CLASSPATH):$(BIN_DIR) $(TEST_SUITE)

# clean compilation output only
clean_class_files:
	rm  $(BIN_DIR)/$(PACKAGE_DIR) -r 2> /dev/null

clean: 
	-rm $(DIST_FILE) $(DIST_NAME) $(BIN_DIR)/* $(DOC_OUTPUT) $(SCRIPTS) *.tmp -r 2> /dev/null

dist: clean $(DIST_FILE)

installer: rebuild $(INSTALLER_JAR)

# build jar file
$(JAR_FILE): $(CLASS_FILES) resources $(OTHER_FILES)
	@echo ">>>>> Creating $@ <<<<<"
	-mkdir $(BIN_DIR) 2>/dev/null
	$(JAR) -cfe .jar.tmp $(MAIN_CLASS) -C $(BIN_DIR) . $(OTHER_FILES) >/dev/null
	@mv .jar.tmp $@

# build distribution archive
$(DIST_FILE): $(DIST_FILES)
	@echo ">>>>> Creating $@ <<<<<"
	mkdir $(DIST_NAME)
	cp -r $^ $(DIST_NAME)
	mkdir $(DIST_NAME)/$(BIN_DIR)
	mv $(DIST_NAME)/$(notdir $(JAR_FILE)) $(DIST_NAME)/$(JAR_FILE) # move JAR back to bin dir
	$(TAR) c $(DIST_NAME) | $(BZIP2) > $@
	rm -r $(DIST_NAME)

# build installer jar file
$(INSTALLER_JAR): $(INSTALL_FILES) clean_class_files $(INSTALLER_CLASS_FILES)
	@echo ">>>>> Creating $@ <<<<<"
	-mkdir $(INSTALLER_BIN_DIR) $(INSTALL_RSRC) 2>/dev/null
	cp -r --parents $(INSTALL_FILES) $(INSTALL_RSRC) >/dev/null
	$(JAR) -cfe .instjar.tmp $(INSTALLER_MAIN) -C $(BIN_DIR) $(PACKAGE_DIR) $(OTHER_FILES) >/dev/null
	@mv .instjar.tmp $@
	
# build java class file
$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	@echo ">>>>> Compiling $< <<<<<"
	-mkdir $(BIN_DIR) 2>/dev/null
	$(JAVAC) -classpath $(LIBS_CLASSPATH):$(BIN_DIR) -sourcepath $(SRC_DIR) -d $(BIN_DIR) $< >/dev/null

# build java unit test class
$(BIN_DIR)/%.class: $(TEST_DIR)/%.java
	@echo ">>>>> Compiling Test Class $< <<<<<"
	-mkdir $(BIN_DIR) 2>/dev/null 
	@echo $(LIBS_CLASSPATH)
	$(JAVAC) -classpath $(LIBS_CLASSPATH):$(BIN_DIR) -sourcepath $(SRC_DIR):$(TEST_DIR) -d $(BIN_DIR) $< >/dev/null

# build batch (windows) script
%.bat: 
	@echo ">>>>> Building $@ Script <<<<<"
	./build-scripts.pl bat $(JAR_FILE) $(MAIN_CLASS) $(LIBS) > $@

# build bash (linux + mac osx) script
%.sh: 
	@echo ">>>>> Building $@ Script <<<<<"
	./build-scripts.pl sh $(JAR_FILE) $(MAIN_CLASS) $(LIBS) > $@
	chmod +x $@

# build visual basic script (Windows)
%.vbs: 
	@echo ">>>>> Building $@ Script <<<<<"
	./build-scripts.pl vbs $(JAR_FILE) $(MAIN_CLASS) $(LIBS) > $@
	chmod +x $@

# text documentation
%.txt: %.pod
	@echo ">>>>> Converting $< to ASCII ($@) <<<<<"
	$(POD2TEXT) $< > $@
	
# html documentation
%.html: %.pod
	@echo ">>>>> Converting $< to HTML ($@) <<<<<"
	$(POD2HTML) $(POD2HTML_FLAGS) $< > $@

# pdf documentation
%.pdf: %.pod
	@echo ">>>>> Converting $< to PDF ($@) <<<<<"
	$(POD2PDF) $< > $@

