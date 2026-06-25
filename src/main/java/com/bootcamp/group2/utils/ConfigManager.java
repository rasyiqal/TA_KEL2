package com.bootcamp.group2.utils;

import com.bootcamp.group2.enums.Platform;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigManager {

    private static final Logger log = LoggerFactory.getLogger(ConfigManager.class);
    private static Config config;

    static {
        loadConfig();
    }

    private ConfigManager() {}

    private static void loadConfig() {
        String env = resolveEnvironment();
        log.info("Loading configuration for environment: {}", env);

        Config base   = ConfigFactory.load("config/application");
        Config envCfg = ConfigFactory.load("config/" + env);
        Config sys    = ConfigFactory.systemProperties();

        config = sys.withFallback(envCfg).withFallback(base).resolve();
    }

    private static String resolveEnvironment() {
        String env = System.getProperty("env");
        if (env == null || env.isBlank()) env = System.getenv("ENV");
        return (env != null && !env.isBlank()) ? env.toLowerCase() : "dev";
    }

    public static String get(String key) {
        return config.getString(key);
    }

    public static String get(String key, String defaultValue) {
        return config.hasPath(key) ? config.getString(key) : defaultValue;
    }

    public static int getInt(String key, int defaultValue) {
        return config.hasPath(key) ? config.getInt(key) : defaultValue;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return config.hasPath(key) ? config.getBoolean(key) : defaultValue;
    }

    public static String getEnvironment() {
        return resolveEnvironment();
    }

    public static Platform getPlatform() {
        String platform = get("platform", "web").toUpperCase();
        return Platform.valueOf(platform);
    }

    public static String getBaseUrl() {
        return switch (getPlatform()) {
            case MOBILE -> get("mobile.baseUrl");
            case WEB    -> get("web.baseUrl");
        };
    }

    public static String getWebBaseUrl() {
        return get("web.baseUrl");
    }

    public static String getMobileBaseUrl() {
        return get("mobile.baseUrl");
    }

    public static String getApiBaseUrl() {
        return get("api.baseUrl", "");
    }

    public static String getValidEmail() {
        return get("testData.validUser.email");
    }

    public static String getValidPassword() {
        return get("testData.validUser.password");
    }

    public static String getAdminEmail() {
        return get("testData.adminUser.email");
    }

    public static String getAdminPassword() {
        return get("testData.adminUser.password");
    }
}
