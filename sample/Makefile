all: Hotel.class

Hotel.class: Hotel.bla
	@java -jar ../target/babble-0.1-SNAPSHOT-jar-with-dependencies.jar -d. Hotel.bla

run: all
	@java -cp "../target/classes/:." Hotel
