# Patches

These are unified diffs against `Gathin23/cleanyourtv` commit `f592b6d`.

- `weather-addon.patch` — cached read-only weather provider and daily high/low in the add-on
- `launcher-weather.patch` — header weather block, provider query, 30-minute refresh, icons, strings

Apply from the root of a clean `cleanyourtv` checkout:

```bash
git apply /path/to/nebula-clean-tv/patches/weather-addon.patch
git apply /path/to/nebula-clean-tv/patches/launcher-weather.patch
```

Both patches were checked with `git apply --check` on a clean worktree at the upstream commit.

The full changed files are also under [`../source`](../source).
