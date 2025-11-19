# <img src="https://github.com/firenh/random_motd/blob/master/img/logo_md.png?raw=true" alt="RandomMOTD">

## RandomMOTD ~ Make your server pop from the multiplayer window!

### _Server Side Mod: Does not need installation on client; compatible with vanilla clients!_

<br>

Minecraft Servers have a few precious options available to them to display information on the multiplayer screen--the most important of which are the Server icon and the MOTD, or 'Message of the Day', the two lines of text underneath the server's name giving a descriptor. The problem is, with the MOTD being static, a player may simply not care about what is being displayed as it's typically just the server's name plus a mediocre announcement.

With RandomMOTD, a new MOTD picked randomly from a list can be sent to the player upon each refresh of the multiplayer screen. This can be used in a variety of ways: you could change the styling of the server's name on every refresh, add easter eggs below the server's name, or cycle through various server announcements. In the config MOTDs are a list of lists of strings: each MOTD sent to the client picks a random MOTD from each sublist and concatenates them together. This enables servers to include both static information and randomized information, such as having the server name on the top line and an easter egg on the bottom line.

RandomMOTD also has one more optional feature: randomized server icons. These are on by default, and allow for multiple server icons that are updated with every refresh. These can be used to create variants of the server icon, or just full-on randomized pictures. There is no limit to the amount of MOTDs and icons one can add.

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
The Config file, found in `./config/random_motd.json`, includes several options. The config can be reloaded by running the `/reload` command in-game. (Does not modify the vanilla `/reload` command, just hooks on to it).
<br>

* `"motds"`: A list of lists of possible MOTDs. The mod will pick a random MOTD from every part of the list and concatenate them, producing a full MOTD. This feature is mainly so, if you would like to contain a static part of information independent from a random one, you can.

* `"use_randomized_icons"`: A boolean either enabling or disabling the randomized icon feature. Defaults to `true`.

* `"icons"`: A list of paths to server icons. Much like default server icons, they must be 64x64 PNG images.

* `"log_when_loaded"`: A boolean telling whether or not to log for every reload. Off by default.

* `"CONFIG_VERSION_DO_NOT_TOUCH_PLS"`: Mod version--please do not touch it.

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
4. Edit `config/random_motd.json` to customize your MOTDs
5. Use `/reload` to apply changes without restarting

## Requirements

- NeoForge 21.1+ for Minecraft 1.21.1
- Java 21+
- Server-side only

---

_Based on the original Fabric mod, ported to NeoForge with enhanced gradient support_