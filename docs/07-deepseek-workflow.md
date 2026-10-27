# 7. How DeepSeek was used

This project was executed with **DeepSeek V4.1 Flash via opencode** as an engineering assistant. The
model worked against the real projector over ADB; it was not a code-only exercise.

## Work performed

1. **Discovery**
   - Read the Android build, SDK, product, and package list.
   - Identified the stock launcher, recommendation stack, telemetry packages, and the setup wizard
     that owned Wi-Fi.
   - Checked whether the device was Netflix-certified; it is not for the current Android TV app.

2. **Safety review**
   - Took a baseline before changing packages.
   - Used `disable-user` rather than uninstall.
   - Kept Chromecast, AirPlay, updates, Ambient, Play Games, TalkBack, and all user apps.
   - Installed and verified the new Home before disabling the stock Home.

3. **Implementation**
   - Built the upstream custom launcher.
   - Added a weather provider to the separate Weather add-on.
   - Added the header widget to the launcher, including sun/moon selection and clothing tips.
   - Built and installed both APKs.
   - Applied the manta theme and verified Home.

4. **Verification**
   - Checked Home resolution after a reboot.
   - Checked the disabled-package list.
   - Queried the weather provider.
   - Captured the final screenshot.
   - Found and re-disabled Google Assistant after it returned following a reboot.

5. **Documentation and sanitization**
   - Rewrote the procedure with placeholder IPs.
   - Removed serial numbers, network names, accounts, VPN data, and personal screenshots.
   - Replaced the profile avatar and weather city in the public screenshots.
   - Excluded all APKs, backups, and Netflix artifacts.

## Reproducibility

The patches in [`../patches`](../patches) are the actual code changes. The files under
[`../source`](../source) are the changed source files for review. The commands in the docs are the
ones used on the device, with private values replaced by placeholders.

## Human control

The operator approved the device changes and provided the projector, network access, and account
decisions. The model did not receive or publish passwords, account cookies, VPN credentials, or
private keys.
