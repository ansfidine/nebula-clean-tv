#!/usr/bin/env bash
# Read-only checks for the Nebula clean-TV setup.
set -euo pipefail

: "${TV_HOST:?set TV_HOST to the projector address}"
: "${CLEANYOURTV_DIR:?set CLEANYOURTV_DIR to a cleanyourtv checkout}"

PY="$CLEANYOURTV_DIR/tools/venv/bin/python"
TV="$CLEANYOURTV_DIR/tools/tv.py"

echo "== Home =="
"$PY" -I "$TV" 'cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.HOME | tail -n 1'

echo
echo "== Disabled packages =="
"$PY" -I "$TV" 'pm list packages -d'

echo
echo "== Weather provider =="
"$PY" -I "$TV" 'content query --uri content://io.github.sumyabazar1.weather.current/current'

echo
echo "== Launcher / weather versions =="
"$PY" -I "$TV" 'dumpsys package com.gathin.tvhome | grep -E "versionName|versionCode" | head -2; dumpsys package io.github.sumyabazar1.weather | grep -E "versionName|versionCode" | head -2'
