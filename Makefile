GS = -g
JC = javac
.SUFFIXES: .java .class
.java.class: 
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        Algorithm4_11.java \
        BasicTerm.java \
        Costs.java \
        Metric.java \
	Subset.java \
	Execution.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
