package com.bytechaos.mqtt.config;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Abdullah Imal
 */
public class MqttConfig {

    private static final String CONFIG_NAME = "mqtt-config.properties";

    private final @NotNull File configFile;

    private @Nullable Properties properties;

    public MqttConfig(final @NotNull File extensionDirectory) {
        configFile = new File(extensionDirectory, CONFIG_NAME);
    }

    public void load() throws ConfigException {
        properties = new Properties();
        try {
            properties.load(new FileReader(configFile));
        } catch (final FileNotFoundException e) {
            throw new ConfigException("Config file '" + configFile.getAbsolutePath() + "' doesn't exists.", e);
        } catch (final IOException e) {
            throw new ConfigException("Could not read configuration.", e);
        }
    }

    public long getSessionExpiryInterval() {
        if (properties == null) {
            throw new IllegalStateException("Config properties null.");
        }
        try {
            return Long.parseLong(properties.getProperty("sessionExpiryInterval", "0"));
        } catch (final NumberFormatException e) {
            throw new IllegalStateException("Property 'sessionExpiryInterval' could not be converted.", e);
        }
    }
}
