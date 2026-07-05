# Wall Builder

Ein Fabric-Mod für Minecraft 1.21, mit dem du schnell Wände bauen kannst.

## Features

- **Wand-Bauwerkzeug** mit Zauberstab-Textur
- Einstellbare **Breite** und **Höhe** (je 1–32 Blöcke)
- Drei **Ausrichtungen**: spielerrelativ, Nord/Süd, Ost/West
- Verwendet das **Baumaterial aus der anderen Hand**
- Einstellungsmenü per **Schleichen + Rechtsklick**

## Installation

1. [Fabric Loader](https://fabricmc.net/use/) für Minecraft 1.21 installieren
2. [Fabric API](https://modrinth.com/mod/fabric-api) in den `mods`-Ordner legen
3. Die gebaute Mod-JAR aus `build/libs/wallbuilder-1.0.0.jar` in den `mods`-Ordner kopieren:
   - Windows: `%appdata%\.minecraft\mods\`
   - Linux: `~/.minecraft/mods/`

## Bedienung

1. Nimm das **Wand-Bauwerkzeug** in eine Hand
2. Halte **Baublöcke in der anderen Hand**
3. **Schleichen + Rechtsklick** → Breite, Höhe und Ausrichtung einstellen
4. **Rechtsklick auf einen Block** → Wand an der angeklickten Fläche bauen

Das Werkzeug findest du im Kreativ-Tab **Werkzeuge & Hilfsmittel**.

## Entwicklung

**Voraussetzungen:** Java 21

```bash
./gradlew build       # Mod bauen
./gradlew runClient   # Test-Client starten
```

Die fertige Mod liegt nach dem Build unter `build/libs/wallbuilder-1.0.0.jar`.

## Lizenz

MIT
