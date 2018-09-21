##SWARM COMPOSER



### Abgabe

Als Abgabe soll der Commit  `f0460a8603abf63df81e3967afba377e76ab42a6` gewertet werden, auch erkennbar am Tag `Abgabe`, der dort platziert wurde.

Die aktuelle Version der Webseite ist under [dieser Adresse](https://134.245.1.240:9061) erreichbar. Die Warnung des Browsers ist darauf zurückzuführen, dass das Zertifikat für die HTTPS-Verbindung selbst signiert ist und kann ignoriert werden.

Bei der App muss als Verbindung zum Server `https://134.245.1.240:9061` eingegeben werden, um die laufende Instanz zu nutzen.



### Anmeldedaten

Bei der Initialisierung der Datenbank werden folgende Nutzer erstellt:

* Administrator:
  * Nutzername/Email : `admin@admin.com`
  * Passwort: `passwort`
* Normaler Nutzer:
  * Nutzername/Email: `max@mustermann.de`
  * Passwort: `passwort`



### Installation

#### Server

Die `ROOT.war` Datei im Ordner `abgaben` stellt die finale Version des Webservers dar. Der Name ist dabei bewusst gewählt, da die Applikation mit der Basis-URL online gestellt werden muss. Das bedeutet, dass die Webseite funktioniert, wenn sie über `beispiel.com/` erreichbar ist, aber nicht über `beispiel.com/composer/`. Das liegt daran, dass die Anfragen des Frontends diesen relativen Pfad nicht berücksichtigen und somit nur die `index.html` geladen werden kann, während alle anderen Anfragen für den eigentlich Seiteninhalt fehlschlagen.

Zum Veröffentlichen auf einem Tomcat-Server gibt es zwei Möglichkeiten:

1. Bei einer laufenden Instanz in der Manger-gui eine eventuell laufende applikation im `/` Kontext zu undeployen und die `ROOT.war` mit genau diesem Namen mithilfe des Menüs hochzuladen. Damit wird automatisch die Applikation über die Adresse zugänglich.
2. Wenn Zugriff auf die Ordnerstruktur des Servers möglich ist, kann der Server heruntergefahren werden, das Verzeichnis `ROOT` gelöscht, die `ROOT.war` Datei hinzugefügt werden und der Server neu gestartet. Damit wird auch die Applikation an der richtigen Adresse deployed.

#### App

Die `app-release.apk` Datei auf das Gerät laden und ausführen. Beim ersten Start nach der Installation muss als erstes in den Einstellungen eine Serveraddresse angegeben werden, da sonst keine Kompositionen angezeigt werden und man sich nicht einloggen kann.
Natürlich werden erst dann Kompositionen zu sehen sein, wenn diese auf der Website erstellt wurden.


### Tests:

#### Server

Für den Server gibt es 21 verschiedene Testmethoden, die jeweils mehrere Aspekte der Funktionalität des Backends testen.

Diese Tests werden standardmäßig mit dem Befehl `mvn package` im Ordner `software` oder `backend` ausgeführt. 

Alternativ können diese Tests auch gezielt mit den Ordnern ausgeführt werden.



#### Frontend

Die Tests für das Frontend befinden sich im Ordner `seleniumTesting`.
Der Server muss unter `http://localhost:8080/` laufen und die Datenbank muss leer sein vor Ausführung der Tests.

Unter Windows muss jeweils in den Testfiles in der `init()` Methode `System.setProperty("webdriver.chrome.driver", "chromedriver");` auf `System.setProperty("webdriver.chrome.driver", "chromedriver.exe");` geändert werden. 
Sollte der Server nicht unter `http://localhost:8080/` laufen, kann hier auch der Parameter von `driver.navigate().to()` entsprechend angepasst werden.

Die Tests selbst können dann über `mvn -Dtest=* test` gestartet werden.

#### App

Die Tests befinden sich in den Ordnern `SWARMComposerApp\app\src\test` und `SWARMComposerApp\app\src\androidTest`. In Android Studio erscheinen die Dateien im Explorer. Um einen der Tests zu starten, muss nach einem Rechtsklick auf die Datei `Run` ausgewählt werden. 
