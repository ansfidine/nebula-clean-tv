# 5. Verify and restore

## Verify the final state

### Home

```bash
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py \
  'cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.HOME | tail -n 1'
```

Expected:

```text
com.gathin.tvhome/.MainActivity
```

### Disabled packages

```bash
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py 'pm list packages -d'
```

Expected: the nine packages in [`../debloat.txt`](../debloat.txt).

`com.google.android.katniss` is known to return after some reboots/Google updates. Re-disable it if
the owner wants Assistant off.

### Weather provider

```bash
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py \
  'content query --uri content://io.github.sumyabazar1.weather.current/current'
```

Expected: one row with `city`, `temp`, `condition`, `high`, `low`, `low_c`, `code`, `day`, `unit`, and
`updated`. If there are zero rows, open Weather and select a city.

### Screenshot

```bash
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py \
  --screenshot backup/after.png --wait 2
```

Keep personal screenshots out of the public repository. The screenshots in this repo are redacted.

### Reboot

Reboot once and repeat the Home, disabled-package, and weather checks. This catches:

- a stock launcher that came back
- a package that the firmware re-enabled
- a weather widget that failed to start
- a new DHCP address

If ADB stops answering, use the projector's network settings to read the new IP, or run:

```bash
adb mdns services
```

## Restore the stock setup

### 1. Re-enable the debloat list and stock Home

```bash
TV_HOST=<PROJECTOR_IP> tools/restore.sh /path/to/nebula-clean-tv/debloat.txt com.google.android.tvlauncher
```

The upstream restore script uses the state recorded by `disable.sh` and restores the original Home
component.

### 2. Re-enable the stock screensaver if it was changed

This project did not change the screensaver. If you changed it separately, restore the original with:

```bash
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py \
  'settings put secure screensaver_components com.google.android.backdrop/.Backdrop'
```

### 3. Remove the custom launcher (optional)

Only remove it after the stock Home is working:

```bash
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py 'pm uninstall com.gathin.tvhome'
```

### 4. Remove the Weather add-on (optional)

```bash
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py 'pm uninstall io.github.sumyabazar1.weather'
```

### 5. Remove the theme overlay (optional)

```bash
TV_HOST=<PROJECTOR_IP> tools/theme.sh --reset
```

## Recovery notes

- Do not disable core Android packages; the upstream `disable.sh` refuses them.
- Do not disable `com.google.android.tungsten.setupwraith` while it owns the saved Wi-Fi network.
- Keep ADB enabled until the final reboot check passes.
- If the projector has no working Home after a failed handover, restore the stock launcher over ADB:

```bash
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py 'pm enable --user 0 com.google.android.tvlauncher'
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py \
  'cmd package set-home-activity --user 0 $(cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.HOME com.google.android.tvlauncher | tail -n 1)'
```
