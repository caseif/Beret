# Beret #

### What is Beret? ###

The **B**eret **E**xtraordinary **R**everse **E**ngineering **T**oolkit is software designed to handle the automatic
parsing and decompilation of JVM bytecode. More info to come as the project progresses.

### Usage ###

Currently Beret is capable of parsing and dumping partial information from class files. To use this function:

`Beret.jar /path/to/class_file /path/to/dump_file`

### Building ###

Beret uses Gradle as its build system. To clone and build the project from scratch:

- `git clone https://github.com/caseif/Beret.git`
- `cd Beret`
- `cp scripts/* .git/hooks`
- `./gradlew` (`gradlew` or `sh ./gradlew` on Windows)

### Licensing ###

Beret is made available as free software under the BSD-3 Clause license.
