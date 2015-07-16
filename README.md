# Babble

Babble is a compiled, duck-typed, object-oriented programming language for the
Java Virtual machine.  It has syntax and semantics inspired by the Smalltalk
programming language, but the compile-once-run-anywhere development cycle from
Java.

## 1. Compiling the Babble compiler

To compile the Babble compiler, use Maven:

    mvn package

This will also fetch any required dependencies (ANTLR, JUnit, and commons-cli).

When the compilation finishes, `babble-0.1-SNAPSHOT-jar-with-dependencies.jar`
in the `target` directory will contain the Babble compiler. We recommend
renaming that file to `target/babble.jar`.

## 2. Running the test suite

Both unit tests and integration tests can be run with Maven:

    mvn verify

Two reports will be shown (one for the unit tests, which are written in Java,
and one for the integration tests, i.e. the ones written in Babble). For a
graphical report, try `mvn site`.

## 3. Compiling programs

Once you have the Babble compiler (see step 1), you can use it to compile
Babble programs. Output goes to the `target/classes` directory by default; this
can be changed using the `--dest` flag, for example:

    java -jar target/babble.jar --dest . MyProgram.bla

A `--verbose` flag is available if you'd like to have a confirmation that
something happened.

## 4. Running Babble programs

You should now have a number of .class files, including MyProgram.class. To run
a Babble program (that is, a Babble class that has a main method), make sure
the `org.twnc.runtime` package is on your classpath. In line with the previous
example:

    java -cp target/babble.jar:. MyProgram
