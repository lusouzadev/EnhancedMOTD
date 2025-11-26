![Mod logo with mod name in a straight line](https://raw.githubusercontent.com/lusouzadev/EnhancedMOTD/refs/heads/main/img/logo/line/400x156.png)

## Features

- **Custom Gradients**: Create smooth color transitions using `<gradient:#RRGGBB:#RRGGBB>text</gradient>`
- **Rainbow Text**: Automatic rainbow gradients with `<rb>text</rb>`
- **Hex Colors**: Full RGB color support using `&#RRGGBB` format
- **Legacy Color Codes**: Standard Minecraft color codes (`&a`, `&c`, etc.)
- **Text Formatting**: Bold (`&l`), italic (`&o`), underline (`&n`), strikethrough (`&m`), obfuscated (`&k`)
- **Randomized Server Icons**: Optional feature to cycle through multiple server icons (supports local files and HTTPS URLs)


## Formatting Guide

### Gradient Syntax
- **Custom Gradient**: `<gradient:#FF0000:#FFFF00>Your Text</gradient>` - Smooth transition from red to yellow
- **Rainbow Gradient**: `<rb>Your Text</rb>` - Automatic rainbow effect

### Color Codes
- **Hex Colors**: `&#FF5500` - Any RGB color
- **Legacy Codes**: `&a` (green), `&c` (red), `&b` (aqua), `&e` (yellow), etc.

### Formatting Codes
- `&l` - **Bold**
- `&o` - *Italic*
- `&n` - Underlined
- `&m` - ~~Strikethrough~~
- `&k` - Obfuscated (random characters)
- `&r` - Reset all formatting

### Combining Formats
Place formatting codes **before** gradient tags:
```
&l<gradient:#FF0000:#FFFF00>Bold Gradient Text</gradient>
&o<rb>Italic Rainbow Text</rb>
```

<br>

<details>
    <summary>Config</summary>
The Config file, found in `./config/enhanced_motd.json`, includes several options. The config can be reloaded by running the `/reload` command in-game. (Does not modify the vanilla `/reload` command, just hooks on to it).
<br>

* `"motds"`: A list of possible MOTDs lists! The mod will pick a random MOTD from every part of the list and concatenate them, producing a full MOTD. This feature is mainly so if you would like to contain a static part of information independent of a random one.

* `"use_randomized_icons"`: A boolean either enabling or disabling the randomized icon feature. Defaults to `true`.

* `"icons"`: A list of paths to server icons or HTTPS URLs. Paths are relative to the server directory. URLs must use HTTPS protocol for security. All icons must be 64x64 PNG images.

* `"log_when_loaded"`: A boolean telling whether to log for every reload. Off by default.

* `"CONFIG_VERSION_DO_NOT_TOUCH_PLS"`: Mod version --please do not touch it.

### Example Config
```json
{
  "motds": [
    [
      "&l<rb>⚡ RAINBOW SERVER ⚡</rb>",
      "&l<gradient:#FF0000:#FFFF00>» FIRE REALM «</gradient>",
      "&l<gradient:#00FFFF:#0000FF>≋ OCEAN VIBES ≋</gradient>"
    ],
    [
      "&r&o- Join the &l<rb>adventure</rb>&r&n!",
      "&r- <gradient:#00AA00:#005500>Survival</gradient> | <gradient:#FF0000:#AA0000>PvP</gradient>",
      "&r&o- Fresh <gradient:#00FF00:#FFD700>world</gradient> just released!"
    ]
  ],
  "use_randomized_icons": true,
  "icons": [
    "server-icon.png",
    "server-icon-2.png",
    "https://dummyimage.com/64x64/000/fff.png&text=Icon3"
  ],
  "log_when_loaded": false
}
```

</details>

## Installation

1. Download the mod JAR from the releases
2. Place it in your server's `mods` folder
3. Start the server - it will generate a default config
4. Edit `config/enhanced_motd.json` to customize your MOTDs
5. Use `/reload` to apply changes without restarting

## Requirements

- NeoForge 21.1+ for Minecraft 1.21.x
- Java 21+
- Server-side only

## Development

### Commit Message Convention

This project uses [Conventional Commits](https://www.conventionalcommits.org/) for automated versioning and release notes. Please format your commits as:

```
<type>(<scope>): <subject>

[optional body]

[optional footer]
```

**Types:**
- `feat:` - New feature (triggers minor version bump)
- `fix:` - Bug fix (triggers patch version bump)
- `perf:` - Performance improvement (triggers patch version bump)
- `refactor:` - Code refactoring (triggers patch version bump)
- `docs:` - Documentation only changes
- `style:` - Code style/formatting changes
- `test:` - Adding or updating tests
- `build:` - Build system or dependency changes
- `ci:` - CI/CD configuration changes
- `chore:` - Other changes that don't modify src or test files

**Breaking Changes:**
Add `BREAKING CHANGE:` in the commit footer to trigger a major version bump:
```
feat: new API structure

BREAKING CHANGE: API endpoints have been restructured
```

**Examples:**
```
feat(motd): add support for multi-line gradients
fix(config): prevent crash when icons array is empty
docs(readme): update installation instructions
```

When you push commits to any of the versioned branches (`neoforge/1.21.x`, `fabric/1.20.x`, etc.), the semantic release workflow will automatically:
1. Analyze your commits
2. Determine the next version number
3. Create a tag with the appropriate version
4. Generate release notes
5. Trigger the build and release workflow

**Concurrency Handling:**
- Multiple pushes to the same branch are **queued and processed sequentially** to prevent version conflicts
- Each branch can release independently (different branches can release in parallel)
- If two developers push simultaneously, the second release waits for the first to complete, then analyzes all new commits

---

_Based on the original_ [RandomMOTD](https://github.com/firenh/random_motd) _fabric mod, ported to NeoForge with enhanced gradient support_
