package net.droegemueller.av4ms.core.srv;

import org.apache.commons.lang3.tuple.Pair;

public interface PreferenceRepository {
    String PREF_SERVER_URL = "PREF_SERVER_URL";
    String PREF_SERVER_DO_AUTH = "PREF_SERVER_DO_AUTH";
    String PREF_SERVER_USERNAME = "PREF_SERVER_USERNAME";
    String PREF_SERVER_PASSWORD = "PREF_SERVER_PASSWORD";
    String PREF_LAST_SUCCESSFUL_CONN_CHECKSUM = "PREF_LAST_SUCCESSFUL_CONN_CHECKSUM";
    String PREF_CHECKSUMMED_CONN_HAS_BASIC_MEASSUREMENT_VALUES = "PREF_CHECKSUMMED_CONN_HAS_BASIC_MEASSUREMENT_VALUES";
    String PREF_CHECKSUMMED_CONN_AV4MS_VERSION = "PREF_CHECKSUMMED_CONN_AV4MS_VERSION";
    String PREF_CHECKSUMMED_CONN_APP_EXTENSION_VERSION = "PREF_CHECKSUMMED_CONN_APP_EXTENSION_VERSION";


    String getPrefServerUrl(String defaultValue);
    boolean getPrefServerDoAuth(boolean defaultValue);
    String getPrefServerUsername(String defaultValue);

    String getPrefServerPassword(String defaultValue);

    String getConfiguredConnChecksum();
    String getPrefLastSuccessfulConnChecksum(String defaultValue);
    void setPrefLastSuccessfulConnChecksum(String value);

    boolean getPrefChecksummedConnHasBasicMeasurementValues(String chksum, boolean defaultValue);
    void setPrefChecksummedConnHasBasicMeasurementValues(String chksum, boolean value);

    Pair<Integer,Integer> getPrefChecksummedConnAv4msVersion(String chksum, Pair<Integer, Integer> defaultValue);
    void setPrefChecksummedConnAv4msVersion(String chksum, Pair<Integer, Integer> value);

    Pair<Integer,Integer> getPrefChecksummedConnAppExtensionVersion(String chksum, Pair<Integer, Integer> defaultValue);
    void setPrefChecksummedConnAppExtensionVersion(String chksum, Pair<Integer, Integer> value);
}
