Der Server muss unter `http://localhost:8080/` laufen und die Datenbank muss leer sein vor Ausführung der Tests.

Unter Windows muss jeweils in den Testfiles in der `init()` Methode `System.setProperty("webdriver.chrome.driver", "chromedriver");` auf `System.setProperty("webdriver.chrome.driver", "chromedriver.exe");` geändert werden. 
Sollte der Server nicht unter `http://localhost:8080/` laufen, kann hier auch der Parameter von `driver.navigate().to()` entsprechend angepasst werden.

Die Tests selbst können dann über `mvn -Dtest=* test` gestartet werden.
