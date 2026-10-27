package io.github.sumyabazar1.weather;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import org.json.JSONObject;

/**
 * Read-only local data for the launcher's header weather widget. The launcher has no INTERNET
 * permission; this provider hands it the latest reading the weather app already fetched.
 */
public final class WeatherProvider extends ContentProvider {
    static final String AUTHORITY = "io.github.sumyabazar1.weather.current";
    private static final String[] COLUMNS = {
            "city", "temp", "condition", "high", "low", "low_c", "code", "day", "unit", "updated"
    };

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        MatrixCursor cursor = new MatrixCursor(COLUMNS);
        if (getContext() == null) return cursor;
        JSONObject data = WeatherCache.read(getContext());
        if (data == null) return cursor;

        boolean fahrenheit = data.optBoolean("fahrenheit");
        int code = data.optInt("code");
        boolean day = data.optBoolean("day", true);
        String city = data.optString("city", "");
        double tempC = data.optDouble("tempC", Double.NaN);
        double highC = data.optDouble("todayMaxC", Double.NaN);
        double lowC = data.optDouble("todayMinC", Double.NaN);
        if (city.isEmpty() || Double.isNaN(tempC) || Double.isNaN(highC) || Double.isNaN(lowC)) {
            return cursor;
        }

        cursor.addRow(new Object[]{
                city,
                Forecast.degrees(tempC, fahrenheit) + "°",
                getContext().getString(Forecast.sky(code, day)),
                Forecast.degrees(highC, fahrenheit) + "°",
                Forecast.degrees(lowC, fahrenheit) + "°",
                String.valueOf(lowC),
                code,
                day ? 1 : 0,
                fahrenheit ? "F" : "C",
                data.optLong("updated")
        });
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.item/weather";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("read-only");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
