# Makefile for the documentation.
# This file is part of the LiteLogger code library.
#
# LiteLogger is free software: you can redistribute it and/or modify it under
# the terms of the GNU Lesser General Public License as published by the Free
# Software Foundation, either version 3 of the License, or (at your option) any
# later version.
#
# LiteLogger is distributed in the hope that it will be useful, but WITHOUT ANY
# WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
# A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
# details.
#
# You should have received a copy of the GNU Lesser General Public License
# along with LiteLogger.  If not, see <http://www.gnu.org/licenses/>.

# the raw pod documentation to build
README=README.pod
COPYING=COPYING_GPL.pod
CHANGES=CHANGES.pod

# output documentation
README_OUTPUT=$(README:.pod=.html) $(README:.pod=.pdf)
COPYING_OUTPUT=$(COPYING:.pod=.html) $(COPYING:.pod=.pdf)
CHANGES_OUTPUT=$(CHANGES:.pod=.html) $(CHANGES:.pod=.pdf)
OUTPUT=$(README_OUTPUT) $(COPYING_OUTPUT) $(CHANGES_OUTPUT)

# pod options
POD2HTML_FLAGS=--noindex

all: build

build: $(OUTPUT)

rebuild: clean build

clean:
	rm $(OUTPUT) *.tmp 2> /dev/null

# html documentation
%.html: %.pod
	@echo "Converting $< to HTML ($@)"
	-pod2html $(POD2HTML_FLAGS) $< > $@

# pdf documentation
%.pdf: %.pod
	@echo "Converting $< to PDF ($@)"
	-pod2pdf $< > $@
