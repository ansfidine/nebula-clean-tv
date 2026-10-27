# Upstream baseline

- Repository: <https://github.com/Gathin23/cleanyourtv>
- Commit used for the patches: `f592b6d`
- License: MIT

The patches were generated against that commit. Apply them from the root of a clean `cleanyourtv`
checkout:

```bash
git clone https://github.com/Gathin23/cleanyourtv.git
cd cleanyourtv
git checkout f592b6d
git apply /path/to/nebula-clean-tv/patches/weather-addon.patch
git apply /path/to/nebula-clean-tv/patches/launcher-weather.patch
```

If upstream has moved, review the patch context before applying. The changes are deliberately small
and contained to the launcher header and the Weather add-on; they do not replace upstream tooling.

## Why patches instead of a fork

A fork would carry the whole upstream repository and make it harder to see the actual changes. The
patches here show exactly what was added:

- a cached, read-only weather provider in the add-on
- a weather block in the launcher header
- a 30-minute refresh loop
- sun/moon/cloud icon selection
- the small strings and drawable resources those views need

The full changed files are also included under `source/` for reading and review.
