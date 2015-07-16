all: report.pdf

report.pdf: report.tex grammar.tex visitor.tex pygments.tex duck.tex
	pdflatex report.tex

pygments.tex:
	pygmentize -f tex -S trac > $@

grammar.tex: ../src/main/antlr4/org/twnc/Babble.g4
	pygmentize -f tex -l antlr -o $@ $<

visitor.tex: ../src/main/java/org/twnc/irtree/ASTGenerator.java
	pygmentize -f tex -l java -o $@ $<

duck.tex: ../src/test/bla/examples/Duck.bla
	pygmentize -f tex -l st -o $@ $<
