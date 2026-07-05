# WallWand

Ein Fabric-Mod für Minecraft 1.21, mit dem du schnell Wände bauen kannst.

## Features

- **WallWand** — Zauberstab zum schnellen Bauen von Wänden
- Baumodi: **Normal**, **Diagonal**, **Palisade**, **Turm**
- **Zufallsmodus**: nutzt zufällige Blöcke aus der Hotbar
- Einstellbare **Breite**, **Höhe** und **Durchmesser** (je 1–32 Blöcke)
- Turmformen: **Rund**, **Quadrat**, **Raute**
- Drei **Ausrichtungen**: spielerrelativ, Nord/Süd, Ost/West
- Verwendet ein per **Rechtsklick gewähltes Material** aus dem Inventar
- Einstellungsmenü per **Schleichen + Rechtsklick in die Luft**

## Installation

1. [Fabric Loader](https://fabricmc.net/use/) für Minecraft 1.21 installieren
2. [Fabric API](https://modrinth.com/mod/fabric-api) in den `mods`-Ordner legen
3. Die gebaute Mod-JAR aus `build/libs/wallwand-1.0.0.jar` in den `mods`-Ordner kopieren:
   - Windows: `%appdata%\.minecraft\mods\`
   - Linux: `~/.minecraft/mods/`

## Bedienung

1. Nimm die **WallWand** in die Hand
2. **Schleichen + Rechtsklick auf einen Block** → diesen Blocktyp als Material wählen
3. Halte die passenden **Baublöcke im Inventar**
4. **Schleichen + Rechtsklick in die Luft** → Modus, Größe und weitere Einstellungen
5. **Rechtsklick auf einen Block** → Struktur an der angeklickten Fläche bauen

### Baumodi

| Modus | Beschreibung |
|-------|--------------|
| Normal | Gerade Wand |
| Diagonal | Schräg ansteigende Wand |
| Palisade | Zickzack-Wand |
| Turm | Rund, quadratisch oder rautenförmig (Durchmesser + Höhe) |

Der **Zufallsmodus** kann zu jedem Modus aktiviert werden und wählt pro Block zufällig aus den platzierbaren Blöcken in der Hotbar.

Die WallWand findest du im Kreativ-Tab **Werkzeuge & Hilfsmittel** oder craftest sie im Überlebensmodus:

```
Stein
  Stock
    Stein
```

(diagonal in der Werkbank)

## Entwicklung

**Voraussetzungen:** Java 21

```bash
./gradlew build       # Mod bauen
./gradlew runClient   # Test-Client starten
```

Die fertige Mod liegt nach dem Build unter `build/libs/wallwand-1.0.0.jar`.

## Lizenz

Dieses Projekt steht unter der **GNU General Public License v3.0** (GPL-3.0).
Siehe [LICENSE](LICENSE) für den vollständigen Lizenztext.
