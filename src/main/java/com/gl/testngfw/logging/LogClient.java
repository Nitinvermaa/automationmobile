package com.gl.testngfw.logging;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogClient extends WebSocketClient {
    private static final Logger LOGGER = Logger.getLogger(LogClient.class.getName());
    public StringBuilder deviceLogStringBuilder = new StringBuilder();
    public StringBuilder errorLogStringBuilder = new StringBuilder();
    public LogClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        LOGGER.log(Level.INFO, "WEBSOCKET OPENED");
    }

    @Override
    public void onMessage(String message) {
        deviceLogStringBuilder.append(message);

        LOGGER.log(Level.INFO, message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LOGGER.log(Level.INFO, "Connection closed, log streaming has stopped");
    }

    @Override
    public void onError(Exception ex) {
        errorLogStringBuilder.append(ex);
        LOGGER.log(Level.WARNING, "", ex);
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }
}