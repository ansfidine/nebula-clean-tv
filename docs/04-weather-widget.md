# 4. Weather widget under the clock

The final Home screen shows the weather under the date, in the same visual language as the Apple TV
header:

- sun / moon / cloud icon
- current temperature
- city name
- condition and today's high/low
- a clothing tip pill

The design goal was **no network permission in the launcher**. The separate Weather add-on owns the
network request; the launcher only reads the latest cached reading through a local content provider.

## Data flow

```text
Open-Meteo
    │  (Weather add-on, INTERNET permission)
    ▼
WeatherActivity
    │  saves Forecast.Report to SharedPreferences
    ▼
WeatherCache
    │  exposes a read-only MatrixCursor
    ▼
WeatherProvider  content://io.github.sumyabazar1.weather.current/current
    │  (launcher has no INTERNET permission)
    ▼
com.gathin.tvhome header widget
```

## Provider contract

Authority: `io.github.sumyabazar1.weather.current`

Path: `/current`

Columns:

| Column | Type | Meaning |
| --- | --- | --- |
| `city` | string | Selected place name |
| `temp` | string | Current temperature with degree symbol |
| `condition` | string | Localized weather condition |
| `high` | string | Today's high with degree symbol |
| `low` | string | Today's low with degree symbol |
| `low_c` | string | Today's low in Celsius, for the clothing tip |
| `code` | int | WMO weather code |
| `day` | int | `1` during the day, `0` at night |
| `unit` | string | `C` or `F` |
| `updated` | long | Cache timestamp in milliseconds |

The provider is read-only: `insert`, `update`, and `delete` do not modify data. It returns zero rows
until the Weather app has a city and a successful reading.

## Add-on changes

The patch adds:

- `Forecast.Report.todayMaxC` and `todayMinC`, read from the daily arrays
- `WeatherCache.java` — saves/reads a small JSON object in the add-on's SharedPreferences
- `WeatherProvider.java` — a read-only `ContentProvider` with the schema above
- `WeatherActivity.show()` — writes the cache after a successful refresh
- a `<provider>` entry in the add-on manifest

The add-on still owns the network request to Open-Meteo. No API key is required. The provider is
exported so the local launcher can query it; it contains weather data only, not account or location
history.

## Launcher changes

The patch adds:

- the `weatherBlock` view under `leftBlock` in `activity_main.xml`
- `weatherIcon`, `weatherTemp`, `weatherCity`, `weatherDetail`, and `weatherTip` bindings
- a 30-minute `weatherTick` in `MainActivity`
- a background provider query with `getContentResolver().query(...)`
- a `<queries>` entry for the add-on package/provider
- `ic_weather_sun.xml`, `ic_weather_moon.xml`, `ic_weather_cloud.xml`, and `weather_tip.xml`
- strings for the clothing tips

Icon selection:

```text
code <= 1 and day != 0  → sun
code <= 1 and day == 0  → moon
otherwise               → cloud
```

Tip thresholds use `low_c`:

```text
<= 5°C   → warm layer
<= 12°C  → light jacket
<= 18°C  → long sleeves
> 18°C   → t-shirt
```

If parsing fails, the light-jacket tip is used.

## Build and install

The add-on and launcher are built independently.

### Add-on

```bash
cd addons/weather
./gradlew assembleRelease
cd ../..

TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py \
  --push addons/weather/app/build/outputs/apk/release/app-release.apk /data/local/tmp/weather.apk

TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py \
  'pm install -r /data/local/tmp/weather.apk; rm -f /data/local/tmp/weather.apk'
```

### Launcher

```bash
cd launcher
TV_HOST=<PROJECTOR_IP> ./build-and-install.sh
```

### Set the city

Open **Weather** on the projector, search for a city, and select it once. The add-on saves the first
successful reading; the launcher picks it up on its next refresh or after returning Home.

If the widget is hidden, check the provider directly:

```bash
TV_HOST=<PROJECTOR_IP> tools/venv/bin/python -I tools/tv.py \
  'content query --uri content://io.github.sumyabazar1.weather.current/current'
```

One row means the widget has data. No rows means the Weather add-on has not saved a city/reading yet.

## Privacy and limits

- The launcher has no `INTERNET` permission.
- The add-on sends the selected city name and coordinates to Open-Meteo, as described by the upstream
  add-on. It does not use an account, telemetry, ads, background service, or location permission.
- The provider is readable by local apps because Android content providers are not private to one app
  unless protected. It contains only the selected city and weather fields above.
- Temperature and condition text follow the add-on's locale/unit settings.
- The widget does not replace the full Weather app; it is a small cached header.
