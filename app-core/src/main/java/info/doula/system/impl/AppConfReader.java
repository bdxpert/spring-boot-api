package info.doula.system.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * AppConfReader
 * This class can read configuration for app
 *
 */
public class AppConfReader {
    Properties prop = new Properties();

    String filePath;
    long timeoutMillisecond = 0L;
    long lastReadTime = 0L;
    long lastModifiedTime = 0L;

    /**
     * Constructor
     *
     * @param filePath The file path
     * @param timeoutMillisecond The duration to check file (millisecond)
     */
    public AppConfReader(String filePath, long timeoutMillisecond) {
        this.filePath = filePath;
        this.timeoutMillisecond = timeoutMillisecond;
    }

    void readConf() {
        try {
            if ((System.currentTimeMillis() - lastReadTime) > timeoutMillisecond) {
                File file = new File(filePath);

                if (!file.canRead()) {
                    return;
                }

                long fileModifiedTime = file.lastModified();
                if ((fileModifiedTime - lastModifiedTime) > 0L) {
                    synchronized (prop) {
                        FileInputStream inputStream = new FileInputStream(file);
                        prop.load(inputStream);
                        inputStream.close();
                        lastModifiedTime = fileModifiedTime;
                    }
                }
                lastReadTime = System.currentTimeMillis();
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Gets configuration value as String
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        readConf();
        return prop.getProperty(key);
    }

    /**
     *  Gets configuration value as String
     *  If there is no value, this method returns defaultString.
     *
     *
     * @param key
     * @param defaultString
     * @return
     */
    public String getString(String key, String defaultString) {
        String result = getString(key);
        if (result == null || result .equals("")) {
            return defaultString;
        }

        return result;
    }

    /**
     * get configuration value as Int
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public int getInt(String key, int defaultValue) {
        readConf();
        try {
            return Integer.parseInt(prop.getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * get configuration value as boolean
     * if there is no configuration, this method
     * return false
     *
     * @param key
     * @return
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * get configuration value as boolean
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        readConf();
        String value = prop.getProperty(key);
        if (value == null || value.equals("")) {
            return defaultValue;
        }
        if (value.equals("true")) {
            return true;
        }
        if (value.equals("false")) {
            return false;
        }

        try {
            int intValue = Integer.parseInt(value);
            return (intValue >= 1);
        } catch (Exception e) {
        }

        return defaultValue;
    }

    /**
     * Gets configuration value as Long
     * If there is no value, this method returns defaultValue.
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public long getLong(String key, long defaultValue) {
        readConf();
        try {
            long value = Long.parseLong(prop.getProperty(key));
            return value;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
