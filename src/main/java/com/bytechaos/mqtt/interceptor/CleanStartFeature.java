package com.bytechaos.mqtt.interceptor;

import com.bytechaos.mqtt.config.MqttConfig;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.interceptor.connect.ConnectInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.connect.parameter.ConnectInboundInput;
import com.hivemq.extension.sdk.api.interceptor.connect.parameter.ConnectInboundOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Abdullah Imal
 */
public class CleanStartFeature implements ConnectInboundInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CleanStartFeature.class);

    private final long sessionExpiryInterval;

    public CleanStartFeature(final @NotNull MqttConfig mqttConfig) {
        sessionExpiryInterval = mqttConfig.getSessionExpiryInterval();
    }

    @Override
    public void onConnect(final @NotNull ConnectInboundInput connectInboundInput,
                          final @NotNull ConnectInboundOutput connectInboundOutput) {

        // cleanSession is mapped by the HiveMQ Extension SDK to cleanStart.
        if (connectInboundInput.getConnectPacket().getCleanStart()) {

            logger.trace("MQTT 3 Client '{}' connected with cleanSession. Handling it as cleanStart " +
                            "Setting session expiry interval to '{}'.",
                    connectInboundInput.getClientInformation().getClientId(), sessionExpiryInterval);

            connectInboundOutput.getConnectPacket().setSessionExpiryInterval(sessionExpiryInterval);
        }
    }
}
