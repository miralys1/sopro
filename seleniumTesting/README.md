Der Server muss unter `http://localhost:8080/` laufen und die Datenbank muss entweder leer sein oder gelöscht werden vor Ausführung der Tests.
Des Weiteren sind die Tests nur unter Linux konfiguriert.

Sollte der Server nicht unter `http://localhost:8080/` laufen, kann jeweils in den Testfiles in der `init()` Methode der Parameter von `driver.navigate().to()` entsprechend angepasst werden.

Die Tests selbst können dann über `mvn -Dtest=* test` gestartet werden.