#!/bin/bash
# For every directory in the locale data files, the Ant build creates a file with the
# names of all locales. Find all such directories and execute the script that replicates
# the logic of generating that file.
#
# Command is intended to run from the root of the directory of the Maven submodule project
# in which the shaded/assembly/uberjar is being created (ex: for the `icu4j` artifact).
find  target/classes/com/ibm/icu/impl/data/* -type d -exec ./create-fullLocaleNames.sh {} \;