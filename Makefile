JAVAC = javac
JAVA_FLAGS = -source 1.8 -target 1.8

SOURCES = AggregationServer.java ContentServer.java GETClient.java LamportClock.java WeatherData.java UtilTool.java

CLASSES = $(SOURCES:.java=.class)

all: $(CLASSES)

%.class: %.java
	$(JAVAC) $(JAVA_FLAGS) $<

clean:
	rm -f *.class

.PHONY: clean
