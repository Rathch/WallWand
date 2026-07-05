# Versionierung

WallWand folgt [Semantic Versioning 2.0.0](https://semver.org/lang/de/).

## Schema

```
MAJOR.MINOR.PATCH
```

- **MAJOR** — breaking changes (z. B. Minecraft-Versionssprung mit API-Änderungen, entfernte Features)
- **MINOR** — neue Features, abwärtskompatibel
- **PATCH** — Bugfixes, abwärtskompatibel

## Wo die Version steht

| Datei | Zweck |
|-------|--------|
| `gradle.properties` → `mod_version` | Quelle der Wahrheit für Builds |
| `fabric.mod.json` | Wird beim Build aus `mod_version` befüllt |
| `CHANGELOG.md` | Menschenlesbare Versionshistorie |
| Git-Tags `v*.*.*` | Release-Markierungen |

## Release-Ablauf

1. Einträge in `CHANGELOG.md` von `[Unreleased]` in die neue Version verschieben
2. `mod_version` in `gradle.properties` anpassen
3. `./gradlew build` ausführen
4. Committen: `chore: release vX.Y.Z`
5. Tag setzen: `git tag -a vX.Y.Z -m "Release vX.Y.Z"`
6. Tag pushen: `git push origin vX.Y.Z`

## Vor 1.0.0

Versionen vor `1.0.0` gelten als erste stabile öffentliche Version mit dem vollständigen Feature-Set zum Release-Zeitpunkt.
