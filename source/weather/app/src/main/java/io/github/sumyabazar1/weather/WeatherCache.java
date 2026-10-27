package io.github.sumyabazar1.weather;

import android.content.Context;

import org.json.JSONObject;

/** The latest reading, kept for the launcher's header widget. No network, no location. */
final class WeatherCache {
    private static final String PREFS = "weather_cache";
    private static final String KEY = "data";

    private WeatherCache() { }

    static void save(Context context, String city, Forecast.Report report, boolean fahrenheit) {
        try {
            JSONObject data = new JSONObject();
            data.put("city", city);
            data.put("tempC", report.tempC);
            data.put("feelsC", report.feelsC);
            data.put("code", report.code);
            data.put("day", report.day);
            data.put("todayMaxC", report.todayMaxC);
            data.put("todayMinC", report.todayMinC);
            data.put("fahrenheit", fahrenheit);
            data.put("updated", System.currentTimeMillis());
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                    .edit().putString(KEY, data.toString()).apply();
        } catch (Exception ignored) {
        }
    }

    static JSONObject read(Context context) {
        String raw = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, null);
        if (raw == null) return null;
        try {
            return new JSONObject(raw);
        } catch (Exception e) {
            return null;
        }
    }
}
