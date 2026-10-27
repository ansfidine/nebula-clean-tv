# 3. Launcher and theme

## Launcher

The new Home is the open-source `com.gathin.tvhome` launcher from
[Gathin23/cleanyourtv](https://github.com/Gathin23/cleanyourtv).

It replaces the stock Android TV launcher and removes the sponsored/recommendation rows. The stock
launcher is disabled only after the new launcher has been installed, started, and checked by the
upstream `build-and-install.sh` script.

### Build and install

```bash
cd launcher
TV_HOST=<PROJECTOR_IP> ./build-and-install.sh
```

The script:

1. Checks the TV's SDK against the launcher's `minSdk`.
2. Builds the release APK.
3. Pushes and installs it.
4. Starts `com.gathin.tvhome/.MainActivity` and checks `Status: ok`.
5. Disables the stock launcher only after the start succeeds.
6. Verifies that Home resolves to the new launcher; if not, it attempts to put the stock Home back.

This project's launcher build also includes the weather-header changes (see
[`04-weather-widget.md`](04-weather-widget.md)).

## Theme: `nebula-coast`

The live theme is in [`../theme/nebula-coast`](../theme/nebula-coast).

```bash
TV_HOST=<PROJECTOR_IP> tools/theme.sh /path/to/nebula-clean-tv/theme/nebula-coast
```

### What the config does

| Key | Value | Effect |
| --- | --- | --- |
| `dock_apps` | seven package names | YouTube, Apple TV, HBO Max, Prime Video, TF1+, NekoBox, Tencent/云视听极光 |
| `fill_dock` | `false` | Keep the seven curated apps; do not fill the dock automatically |
| `columns` | `7` | Seven app columns |
| `rows_below_dock` | `0` | No app rows below the dock |
| `wallpaper` | `true` | Use the theme's wallpaper |
| `wallpaper_rotation_minutes` | `0` | Do not rotate wallpapers |
| `wallpaper_dim` | `25` | Dark overlay for readable text |
| `wallpaper_captions` | `false` | No photo captions |
| `show_greeting` | `false` | No “Good morning” greeting |
| `date_below_clock` | `true` | Date under the clock |
| `header_white` | `true` | White header text/icons |
| `dock_glass` | `true` | Frosted dock |
| `dock_title` | `false` | No “Apps” label above the dock |
| `app_library` | `true` | Keep the full app library |
| `hidden_apps` | `com.mediatek.wwtv.tvcenter` | Hide the system TV center panel from the launcher |
| `clock_wallpaper` | `true` | Clock/date sit over the wallpaper |
| `home_idle_clock_minutes` | `5` | Ambient clock after idle |

### Wallpaper

`theme/nebula-coast/wallpaper.jpg` is the side-view manta photograph:

- **“Manta in German Channel Palau”** by **amanderson2**
- CC BY 2.0
- Cropped to 16:9 and resized for 1920×1080
- The launcher adds the `wallpaper_dim` overlay

See [`../theme/nebula-coast/CREDITS.md`](../theme/nebula-coast/CREDITS.md) for the full attribution.

### Dock customization

The dock is a list of package names in `config.json`. Use the package names installed on your device:

```bash
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py 'pm list packages'
```

If a package is missing, the launcher simply omits that tile. Edit `config.json`, then push the theme
again.

## Reverting the theme

```bash
TV_HOST=<PROJECTOR_IP> tools/theme.sh --reset
```

This removes the overlay and returns the launcher to its built-in config; it does not remove the
launcher or the debloat changes.
