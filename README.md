# WallWand

A Fabric mod for Minecraft 1.21 that lets you build walls quickly.

## Features

- **WallWand** — magic wand for fast wall building
- Build modes: **Normal**, **Diagonal**, **Palisade**, **Tower**
- **Random mode**: uses random blocks from your hotbar
- Adjustable **width**, **height**, and **diameter** (1–32 blocks each)
- Tower shapes: **Round**, **Square**, **Diamond**
- Three **orientations**: player-relative, North/South, East/West
- Uses a **right-click selected material** from your inventory
- Settings menu via **sneak + right-click in the air**

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.21
2. Place [Fabric API](https://modrinth.com/mod/fabric-api) in your `mods` folder
3. Copy the built mod JAR from `build/libs/wallwand-<version>.jar` into your `mods` folder:
   - Windows: `%appdata%\.minecraft\mods\`
   - Linux: `~/.minecraft/mods/`

## Usage

1. Hold the **WallWand** in your hand
2. **Sneak + right-click a block** → select that block type as your material
3. Keep the required **building blocks in your inventory**
4. **Sneak + right-click in the air** → open mode, size, and other settings
5. **Right-click a block** → place the structure on the clicked surface

### Build modes

| Mode | Description |
|------|-------------|
| Normal | Straight wall |
| Diagonal | Sloped / ascending wall |
| Palisade | Zigzag wall |
| Tower | Round, square, or diamond tower (diameter + height) |

**Random mode** can be enabled for any mode and picks randomly from placeable blocks in your hotbar.

You can find the WallWand in the Creative tab **Tools & Utilities**, or craft it in survival mode:

```
Stein oder Bruchstein
      Stock
        Stein oder Bruchstein
```

(diagonal in der Werkbank – Stein und Bruchstein funktionieren beide)

Das Rezept wird in **EMI** und **JEI** angezeigt, wenn diese Mods installiert sind.

## Development

**Requirements:** Java 21

```bash
./gradlew build       # Build the mod
./gradlew runClient   # Start test client
```

The built mod is located at `build/libs/wallwand-<version>.jar` after a successful build.

## Versionierung

Dieses Projekt nutzt [Semantic Versioning](https://semver.org/lang/de/):

| Teil | Bedeutung | Beispiel |
|------|-----------|----------|
| **MAJOR** | Inkompatible Änderungen | `2.0.0` |
| **MINOR** | Neue Features (abwärtskompatibel) | `1.1.0` |
| **PATCH** | Fehlerbehebungen (abwärtskompatibel) | `1.0.1` |

Die Version steht in `gradle.properties` (`mod_version`) und wird in die Mod-JAR übernommen.
Änderungen sind im [CHANGELOG.md](CHANGELOG.md) dokumentiert.
Releases werden als Git-Tags (`v1.0.0`, `v1.1.0`, …) markiert.

## License

This project is licensed under the **GNU General Public License v3.0** (GPL-3.0).
See [LICENSE](LICENSE) for the full license text.
