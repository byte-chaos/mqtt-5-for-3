package com.bytechaos.mqtt;

import com.bytechaos.mqtt.config.ConfigException;
import com.bytechaos.mqtt.config.MqttConfig;
import com.bytechaos.mqtt.interceptor.CleanStartFeature;
import com.hivemq.extension.sdk.api.ExtensionMain;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.packets.general.MqttVersion;
import com.hivemq.extension.sdk.api.parameter.ExtensionStartInput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStartOutput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStopInput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStopOutput;
import com.hivemq.extension.sdk.api.services.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Abdullah Imal
 */
public class Mqtt5for3Main implements ExtensionMain {

    private static final Logger logger = LoggerFactory.getLogger(Mqtt5for3Main.class);

    @Override
    public void extensionStart(final @NotNull ExtensionStartInput extensionStartInput,
                               final @NotNull ExtensionStartOutput extensionStartOutput) {

        final MqttConfig mqttConfig = new MqttConfig(extensionStartInput.getExtensionInformation().getExtensionHomeFolder());
        try {
            mqttConfig.load();
        } catch (final ConfigException e) {
            extensionStartOutput.preventExtensionStartup("Config could not be loaded. See debug log for more details.");
            logger.debug("Original reason: ", e);
        }

        final CleanStartFeature cleanStartFeature = new CleanStartFeature(mqttConfig);

        Services.interceptorRegistry().setConnectInboundInterceptorProvider(
                input -> input.getConnectionInformation().getMqttVersion() == MqttVersion.V_5 ? null : cleanStartFeature);
    }

    @Override
    public void extensionStop(final @NotNull ExtensionStopInput extensionStopInput,
                              final @NotNull ExtensionStopOutput extensionStopOutput) {

    }
}
