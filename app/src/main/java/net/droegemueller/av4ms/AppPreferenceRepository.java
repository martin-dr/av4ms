package net.droegemueller.av4ms;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.droegemueller.av4ms.core.srv.PreferenceRepository;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class AppPreferenceRepository implements PreferenceRepository {

    private SharedPreferences prefs;

    public AppPreferenceRepository(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    private void setString(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

    private int getInteger(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    private void setBoolean(String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    private boolean getBoolean(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }

    private void setInteger(String key, int value) {
        prefs.edit().putInt(key, value).apply();
    }

    private static Pattern pairPattern = Pattern.compile("^((?:-?\\d+)?)::((?:-?\\d+))$");

    private Pair<Integer, Integer> getIntegerPair(String s, Pair<Integer, Integer> defaultValue) {
        Matcher m = pairPattern.matcher(getString(s, ""));
        if (m.matches()) {
            String s1 = m.group(1);
            String s2 = m.group(2);
            return new ImmutablePair<>(s1.length() == 0 ? null : Integer.parseInt(s1), s2.length() == 0 ? null : Integer.parseInt(s2));
        }
        return defaultValue;
    }
    private void setIntegerPair(String key, Pair<Integer, Integer> value) {
        String s = "";
        if (value != null) {
            String s1 = value.getLeft() == null ? "" : value.getLeft().toString();
            String s2 = value.getRight() == null ? "" : value.getRight().toString();
            s1 = s1 + "::" + s2;
        }
        setString(key, s);
    }

    @Override
    public String getPrefServerUrl(String defaultValue) {
        return getString(PREF_SERVER_URL, defaultValue);
    }

    @Override
    public boolean getPrefServerDoAuth(boolean defaultValue) {
        return getBoolean(PREF_SERVER_DO_AUTH, defaultValue);
    }

    @Override
    public String getPrefServerUsername(String defaultValue) {
        return getString(PREF_SERVER_USERNAME, defaultValue);
    }

    @Override
    public String getPrefServerPassword(String defaultValue) {
        return getString(PREF_SERVER_PASSWORD, defaultValue);
    }

    @Override
    public String getConfiguredConnChecksum() {
        StringBuilder sb = new StringBuilder();
        sb.append(getPrefServerUrl(""));
        if (getPrefServerDoAuth(false)) {
            sb.append("|");
            sb.append(getPrefServerUsername(""));
            sb.append("|");
            sb.append(getPrefServerPassword(""));
        }
        byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
        Checksum cksum = new CRC32();
        cksum.update(data, 0, data.length);
        String resultString = String.format("%08X", cksum.getValue());
        return resultString;
    }

    @Override
    public String getPrefLastSuccessfulConnChecksum(String defaultValue) {
        return getString(PREF_LAST_SUCCESSFUL_CONN_CHECKSUM, defaultValue);
    }

    @Override
    public void setPrefLastSuccessfulConnChecksum(String value) {
        setString(PREF_LAST_SUCCESSFUL_CONN_CHECKSUM, value);
    }

    @Override
    public boolean getPrefChecksummedConnHasBasicMeasurementValues(String chksum, boolean defaultValue) {
        return getBoolean(PREF_CHECKSUMMED_CONN_HAS_BASIC_MEASSUREMENT_VALUES + "@" + chksum, defaultValue);
    }

    @Override
    public void setPrefChecksummedConnHasBasicMeasurementValues(String chksum, boolean value) {
        setBoolean(PREF_CHECKSUMMED_CONN_HAS_BASIC_MEASSUREMENT_VALUES + "@" + chksum, value);
    }


    @Override
    public Pair<Integer,Integer> getPrefChecksummedConnAv4msVersion(String chksum, Pair<Integer, Integer> defaultValue) {
        return getIntegerPair(PREF_CHECKSUMMED_CONN_AV4MS_VERSION + "@" + chksum, defaultValue);
    }

    @Override
    public void setPrefChecksummedConnAv4msVersion(String chksum, Pair<Integer, Integer> value) {
        setIntegerPair(PREF_CHECKSUMMED_CONN_AV4MS_VERSION + "@" + chksum, value);
    }

    @Override
    public Pair<Integer,Integer> getPrefChecksummedConnAppExtensionVersion(String chksum, Pair<Integer, Integer> defaultValue) {
        return getIntegerPair(PREF_CHECKSUMMED_CONN_APP_EXTENSION_VERSION + "@" + chksum, defaultValue);
    }

    @Override
    public void setPrefChecksummedConnAppExtensionVersion(String chksum, Pair<Integer, Integer> value) {
        setIntegerPair(PREF_CHECKSUMMED_CONN_APP_EXTENSION_VERSION + "@" + chksum, value);
    }

}
