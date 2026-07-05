# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/).

## [Unreleased]

## [1.1.3] - 2026-07-05

### Fixed
- Fixed crash on world load caused by writing recipes into an immutable recipe map (1.21.1 / Recipe Essentials)
- Crafting recipes are now injected safely during recipe loading instead of after reload

## [1.1.1] - 2026-07-05

### Fixed
- Crafting recipes now load correctly by using the `recipe/` data folder (Minecraft 1.21+)
- JEI now always shows WallWand crafting recipes (no longer depends on a loaded world)

## [1.1.0] - 2026-07-05

### Added
- EMI integration: crafting recipes shown in EMI (stone and cobblestone)
- JEI integration: mod recipes explicitly registered in JEI
- EMI info entry with crafting hint for the WallWand tool
- EMI recipe defaults for the recipe tree
- Semantic versioning documentation (`VERSIONING.md`)

### Changed
- Crafting recipes updated to the Minecraft 1.21 JSON format
- Separate recipes for stone and cobblestone

### Fixed
- Survival crafting recipe now works with stone and cobblestone

## [1.0.0] - 2026-07-05

### Added
- WallWand tool with magic wand texture
- Build modes: Normal, Diagonal, Palisade, Tower
- Adjustable width, height, diameter, and orientation
- Tower shapes: Round, Square, Diamond (hollow outer walls)
- Random mode for hotbar blocks
- Material selection via sneak + right-click on a block
- Settings menu via sneak + right-click in the air
- Survival crafting recipe (diagonal: stone, stick, stone)
- German and English localization
- GPL-3.0 license

### Changed
- Unified mod ID and assets under `wallwand`

[Unreleased]: https://github.com/Rathch/WallWand/compare/v1.1.3...HEAD
[1.1.3]: https://github.com/Rathch/WallWand/compare/v1.1.1...v1.1.3
[1.1.1]: https://github.com/Rathch/WallWand/compare/v1.1.0...v1.1.1
[1.1.0]: https://github.com/Rathch/WallWand/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/Rathch/WallWand/releases/tag/v1.0.0
