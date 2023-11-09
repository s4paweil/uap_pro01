# uap_pro01

Das Projekt enthält ein Maven-Buildscript zum Bauen der JAR-Datei (tram.jar) und zum Auflösen der Abhängigkeiten (Log4J).
Mit folgendem Befehlt das kann Buildscript ausgeführt werden:
```console
mvn clean package
```

Das Programm kann über die Konsole wie folgte ausgeführt werden (hier beispielhaft aus dem Hauptordner des Projekts):
```console
java -jar target\tram.jar -cp tramcode\ggt.tram
```

Als Paramter stehen zur Auswahl:
  - c: Debugging Ausgabe auf der Konsole.
  - f: Debugging Ausgabe in einer Datei (debug.log).
  - s: Step by step Ausführung des Programms. Der nächste Schritt wird erst ausgeführt, wenn der Nutzer die Eingabetaste drückt. Besonders sinnvoll in Kombination mit Parameter "c".
  - p: Gibt zusätzlich den gesamten Code des Programms aus bzw. schreibt diesen in die Logging-Datei (in Kombination mit Parameter "f").
