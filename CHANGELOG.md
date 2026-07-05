# Changelog

Alle wesentlichen Änderungen an diesem Projekt werden in dieser Datei dokumentiert.

Das Format basiert auf [Keep a Changelog](https://keepachangelog.com/de/1.1.0/),
und dieses Projekt folgt [Semantic Versioning](https://semver.org/lang/de/).

## [Unreleased]

## [1.1.0] - 2026-07-05

### Hinzugefügt
- EMI-Integration: Crafting-Rezepte werden in EMI angezeigt (Stein und Bruchstein)
- JEI-Integration: Mod-Rezepte werden explizit in JEI registriert
- EMI-Info-Eintrag mit Crafting-Hinweis für das WallWand-Werkzeug
- EMI-Rezept-Defaults für den Rezeptbaum
- Dokumentation zur semantischen Versionierung (`VERSIONING.md`)

### Geändert
- Crafting-Rezepte auf das Minecraft-1.21-JSON-Format umgestellt
- Separate Rezepte für Stein und Bruchstein

### Behoben
- Survival-Crafting-Rezept funktioniert jetzt mit Stein und Bruchstein

## [1.0.0] - 2026-07-05

### Hinzugefügt
- WallWand-Werkzeug mit Zauberstab-Textur
- Baumodi: Normal, Diagonal, Palisade, Turm
- Einstellbare Breite, Höhe, Durchmesser und Ausrichtung
- Turmformen: Rund, Quadrat, Raute (hohle Außenwände)
- Zufallsmodus für Hotbar-Blöcke
- Materialauswahl per Schleichen + Rechtsklick auf einen Block
- Einstellungsmenü per Schleichen + Rechtsklick in die Luft
- Survival-Crafting-Rezept (diagonal: Stein, Stock, Stein)
- Deutsche und englische Lokalisierung
- GPL-3.0-Lizenz

### Geändert
- Mod-ID und Assets auf `wallwand` vereinheitlicht

[Unreleased]: https://github.com/Rathch/WallWand/compare/v1.1.0...HEAD
[1.1.0]: https://github.com/Rathch/WallWand/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/Rathch/WallWand/releases/tag/v1.0.0
