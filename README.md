# MongoDB + Kotlin (Übung)

Dieses Projekt ist eine kleine Kotlin-App, die jede Sekunde Messwerte und Events in eine MongoDB schreibt.

## Voraussetzungen

- Docker Desktop installiert (inkl. `docker compose`)
- Mindestens JDK 21 (oder IntelliJ mit entsprechendem SDK)

## 1) MongoDB einrichten (Docker Compose)

Im Projektordner:

```powershell
docker compose up -d
docker compose ps
```

Die MongoDB stoppen (die Daten bleiben erhalten):

```powershell
docker compose down
```

Aufräumen und die MongoDB inklusive der Daten löschen (Volume entfernen):

```powershell
docker compose down -v
```


## 2) Starte nun die App

Die App läuft endlos und schreibt jede Sekunde neue Dokumente. `gradle` ist 
ein sogenanntes Buildtool. Wir werden das im Rest des Moduls DTC noch besprechen.
Für den Moment genügt es, wenn Du den folgenden Befehl im Terminal eingibst.

Für Windows
```powershell
gradlew.bat run
```

Für macOS
```powershell
./gradlew run
```

Beenden mit `Ctrl+C`.

## Datenmodell

- Datenbank: `smarthome`
- Collections: `events`, `measurements`
- `buildingId`: `haus-1`, `haus-2`, `haus-3`, `haus-4`
- `events.eventName` ist z.B.: `brightnessLow`, `temperatureHigh`, `temperatureLow`, `humidityHigh`, `co2TooHigh`, `doorOpened`, `doorClosed`
