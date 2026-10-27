# 6. Scope, privacy, and what is not included

## Scope

This repository documents and reproduces a **launcher/ad cleanup** on an Anker Nebula D2426
(Capsule III Pro) running Android 11:

- disable non-essential packages
- replace Home with a custom launcher
- apply a personal theme
- add a local weather header

It does **not** modify the Android system image, kernel, bootloader, DRM, or Netflix.

## Netflix is intentionally excluded

The current Android TV Netflix app (`com.netflix.ninja`) rejects this projector with:

```text
This version of Netflix app is not compatible with your device (-13).
```

That is Netflix's device-certification check, not a remote-control or APK-install problem. A much older
Android TV build launches but cannot reach the current service (`-121`). The phone/tablet Netflix app
runs, but is not a remote-first Google TV interface.

No Netflix APK, patched build, account data, device-spoofing procedure, or DRM bypass is included.
For a Google TV-style Netflix experience, use a certified HDMI device such as a Chromecast with Google
TV or a Fire TV Stick.

## What is not uploaded

- No device serial number, IP address, MAC address, SSID, or VPN profile
- No Google/Netflix account names, email addresses, passwords, or cookies
- No ADB private keys
- No APKs or proprietary application binaries
- No full device backup
- No screenshots of account pickers, emails, or other personal data
- No private signing keys

## Screenshot sanitization

The before/after screenshots are safe to publish:

- the original launcher's profile avatar was replaced with a neutral placeholder
- the weather city label was replaced with `YOUR CITY`
- no account or network data appears

The only location-like word left in the images is not present; the weather numbers are illustrative.

## Data flow disclosure

The Weather add-on contacts Open-Meteo with the selected city's search text and coordinates. That is
the upstream add-on's documented behavior. The launcher itself has no network permission. The content
provider exposes only the selected city and the weather fields listed in
[`04-weather-widget.md`](04-weather-widget.md).

## Licensing

- Code and documentation in this repository: MIT
- Upstream `cleanyourtv` code: MIT, copyright Gathin
- Weather add-on: upstream MIT, original author Sumyabazar1
- `wallpaper.jpg`: CC BY 2.0, amanderson2, via Wikimedia Commons
- Third-party app names/logos in screenshots: property of their owners; shown for documentation only

See [`../NOTICE.md`](../NOTICE.md).
