#!/usr/bin/env bash
# Apply the weather patches to a clean cleanyourtv checkout.
set -euo pipefail

: "${CLEANYOURTV_DIR:?set CLEANYOURTV_DIR to a cleanyourtv checkout}"

REPO_DIR="$(cd "$(dirname "$0")/.." && pwd)"

cd "$CLEANYOURTV_DIR"

git apply --check "$REPO_DIR/patches/weather-addon.patch"
git apply --check "$REPO_DIR/patches/launcher-weather.patch"
git apply "$REPO_DIR/patches/weather-addon.patch" "$REPO_DIR/patches/launcher-weather.patch"

echo "Patches applied. Build the launcher and Weather add-on next."
