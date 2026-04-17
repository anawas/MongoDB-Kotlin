## Übungen (MongoDB)

### In MongoDB einloggen

Wenn Du MongoDB noch nie auf Deinem Rechner hattest, kannst Du Dich via Container einloggen:

```powershell
docker exec -it mongodb mongosh -u root -p rootpassword --authenticationDatabase admin
```

Wenn Du die Datenbank schon auf Deinem Rechner hast, dann hast Du auch `mongosh` installiert. Damit
kannst Du Dich wie folgt einloggen:

```powershell
mongosh "mongodb://root:rootpassword@localhost:27017"
```

Tipp: Vor den Aufgaben sicherstellen, dass die App läuft und Daten erzeugt.

### Aufgabe 1: Events abfragen

1. In die DB wechseln: `use smarthome`
2. Die letzten 20 Events anzeigen (neueste zuerst).

Hinweis: Sortieren geht über `_id` (ObjectId enthält Zeitanteil).

Beispiel:

```javascript
use smarthome
db.events.find().sort({ _id: -1 }).limit(20)
```

### Aufgabe 2: Events zu einer bestimmten `buildingId`

1. Alle Events für z.B. `haus-2` anzeigen.
2. Nur die Felder `buildingId`, `eventName`, `timestamp`, `data` anzeigen (ohne `_id`).

Beispiel:

```javascript
db.events
  .find({ buildingId: "haus-2" }, { _id: 0, buildingId: 1, eventName: 1, timestamp: 1, data: 1 })
  .sort({ _id: -1 })
  .limit(50)
```

### Aufgabe 3: Bestimmte Eventtypen zählen

3a) Gesamtzahl eines Eventtyps (z.B. `doorOpened`) zählen.

Beispiel:

```javascript
db.events.countDocuments({ eventName: "doorOpened" })
```

3b) Anzahl eines Eventtyps pro `buildingId` zählen (z.B. `co2TooHigh`).

Beispiel:

```javascript
db.events.aggregate([
  { $match: { eventName: "co2TooHigh" } },
  { $group: { _id: "$buildingId", count: { $sum: 1 } } },
  { $sort: { count: -1 } }
])
```

3c) Anzahl *aller* Eventtypen pro `buildingId` zählen (Rangliste pro Haus).

Beispiel:

```javascript
db.events.aggregate([
  { $group: { _id: { buildingId: "$buildingId", eventName: "$eventName" }, count: { $sum: 1 } } },
  { $sort: { "_id.buildingId": 1, count: -1 } }
])
```

### Aufgabe 4: Messwerte auswerten
4a) Die letzten 10 Messwerte von Haus "haus-1" abfragen:

```javascript
db.measurements.find({ buildingId: "haus-1" }).sort({ _id: -1 }).limit(10)
```

4b) Die maximale Temperatur in Haus 1 abfragen:

```javascript
db.measurements.aggregate([
  { $match: { buildingId: "haus-1" } },
  { $group: { _id: "$buildingId", maxTemperature: { $max: "$temperature" } } }
])
```

4c) Nun die mittlere Feuchtigkeit in jedem Haus bestimmen und in absteigender Reihenfolge ausgeben.

```javascript
db.measurements.aggregate([
  {
    $group: {
      _id: "$buildingId",
      avgHumidity: { $avg: "$humidity" }
    }
  },
  {
    $sort: { avgHumidity: -1 }
  }
])
```
