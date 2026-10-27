# 2. Debloat and ad cleanup

## What is disabled

The exact list is [`../debloat.txt`](../debloat.txt). Nine packages are disabled for user 0; none are
uninstalled.

| Package | Why |
| --- | --- |
| `com.zhixin.logservice.nebula` | Nebula log collector/uploader |
| `com.google.android.feedback` | Google feedback |
| `android.autoinstalls.config.sdmc` | Play auto-install of partner apps |
| `com.google.android.partnersetup` | Partner-app attribution |
| `fusion.android.tv.demo` | Retail display demo content |
| `com.google.android.katniss` | Google Assistant / voice search (owner choice) |
| `com.google.android.tvlauncher` | Stock launcher with sponsored/recommended rows |
| `com.google.android.tvrecommendations` | Recommendation rows for the stock Home |
| `com.mediatek.android.leanbacklauncher.partnercustomizer` | Partner rows for the stock Home |

## What is kept

- Chromecast and Google Cast
- AirPlay / AirPin
- Firmware updates
- Ambient screensaver (`com.google.android.backdrop`)
- Play Games
- TalkBack
- All installed streaming, utility, and VPN apps
- The setup wizard that owns the saved Wi-Fi network

## Why disabling is safer than uninstalling

`pm disable-user` keeps the APK and its data. The upstream `tools/restore.sh` can re-enable the exact
packages and restore the original Home component. Uninstalling a system package can break OTA updates
or leave no recovery path without a factory reset.

## Apply the list

> Install and verify the custom launcher **before** running this list. The list includes the stock
> launcher; disabling it while no working Home is installed can leave the projector unusable.

```bash
TV_HOST=<PROJECTOR_IP> tools/disable.sh /path/to/nebula-clean-tv/debloat.txt
```

The upstream script:

- refuses invalid package names
- refuses core Android packages
- protects the setup wizard/Wi-Fi owner
- records the previous state so `tools/restore.sh` can undo it

## Verify

```bash
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py 'pm list packages -d'
```

Expected: the nine package names above.

```bash
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py \
  'cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.HOME | tail -n 1'
```

Expected: `com.gathin.tvhome/.MainActivity`.

## Known firmware behavior

`com.google.android.katniss` was observed to return to the enabled list after a reboot or a Google
services update. Re-disable it when needed:

```bash
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py \
  'pm disable-user --user 0 com.google.android.katniss'
```

The owner chose to disable Assistant; if you want the microphone/voice button to keep working, remove
that line from the list and restore the package.
