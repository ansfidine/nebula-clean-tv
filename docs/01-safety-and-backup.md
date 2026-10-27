# 1. Safety and backup

This project disables system packages and changes the Home app. Both are reversible, but a bad
package list or a Home switch that fails halfway can leave a TV without a usable launcher. Take a
backup first.

## Before you start

1. Connect the projector to the same network as your computer.
2. Enable **Developer options → Network debugging / ADB debugging**.
3. Authorize the computer when the projector asks.
4. Confirm ADB answers:

```bash
export TV_HOST=<PROJECTOR_IP>
tools/venv/bin/python -I tools/tv.py 'getprop ro.build.version.sdk'
```

The tested device reports `30` (Android 11).

## Capture a private baseline

Run these from the upstream `cleanyourtv` root. The output can contain your network name, device
identifiers, or account names. **Keep it private; do not commit it.**

```bash
mkdir -p backup/device
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py 'pm list packages' > backup/device/packages.txt
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py 'pm list packages -d' > backup/device/packages-disabled.txt
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py 'getprop' > backup/device/getprop.txt
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py 'settings list global' > backup/device/settings-global.txt
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py 'settings list secure' > backup/device/settings-secure.txt
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py 'settings list system' > backup/device/settings-system.txt
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py 'dumpsys wifi' > backup/device/wifi.txt
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py 'cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.HOME' > backup/device/home.txt
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py --screenshot backup/device/before.png
```

The repository's `.gitignore` excludes `backup/`, APKs, signing material, and logs.

## Why the stock setup wizard is not in the list

The first-run Wi-Fi network can be owned by `com.google.android.tungsten.setupwraith`. Disabling that
package can delete the saved network and take the projector off Wi-Fi. The upstream `disable.sh`
checks this and skips the package when necessary. Do not force it unless you are on Ethernet and know
what you are doing.

## Reboot check

After the changes, reboot once and verify:

- The projector reconnects to Wi-Fi.
- The Home button opens the custom launcher.
- The debloat packages are still disabled.
- The weather widget returns after the launcher restarts.

On this firmware, `com.google.android.katniss` can be re-enabled by Google services after a reboot.
Check it and disable it again if the owner wants Assistant off:

```bash
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py 'pm disable-user --user 0 com.google.android.katniss'
```

The device may receive a different LAN address after a reboot. Use the address shown in the
projector's network settings, or discover the ADB service with `adb mdns services`.
