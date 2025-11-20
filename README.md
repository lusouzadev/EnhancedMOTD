# <img src="https://github.com/lusouzadev/EnhancedMOTD/blob/master/imge/logo/line/400x156.png?raw=true" width="400" height="156" title="EnhancedMOTD" alt="Mod logo with mod name in a straight line"/>

## Features

- **Custom Gradients**: Create smooth color transitions using `<gradient:#RRGGBB:#RRGGBB>text</gradient>`
- **Rainbow Text**: Automatic rainbow gradients with `<rb>text</rb>`
- **Hex Colors**: Full RGB color support using `&#RRGGBB` format
- **Legacy Color Codes**: Standard Minecraft color codes (`&a`, `&c`, etc.)
- **Text Formatting**: Bold (`&l`), italic (`&o`), underline (`&n`), strikethrough (`&m`), obfuscated (`&k`)
- **Randomized Server Icons**: Optional feature to cycle through multiple server icons


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

* `"icons"`: A list of paths to server icons. Much like default server icons, they must be 64x64 PNG images.

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
    "server-icon-2.png"
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

---

_Based on the original Fabric mod, ported to NeoForge with enhanced gradient support_