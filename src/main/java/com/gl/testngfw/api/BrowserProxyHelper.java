package com.gl.testngfw.api;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.filters.ResponseFilter;
import net.lightbody.bmp.mitm.CertificateAndKeySource;
import net.lightbody.bmp.mitm.KeyStoreFileCertificateSource;
import net.lightbody.bmp.mitm.RootCertificateGenerator;
import net.lightbody.bmp.mitm.TrustSource;
import net.lightbody.bmp.mitm.manager.ImpersonatingMitmManager;
import net.lightbody.bmp.proxy.CaptureType;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BrowserProxyHelper {
    public static final String ORIGINAL_STRING = "original-string";
    private static BrowserMobProxy proxy = new BrowserMobProxyServer();

    /**
     * start proxy server on specified port.
     */
    public static void start(int port) {
        proxy.start(port);
    }

    public static BrowserMobProxy getProxy() {
        return proxy;
    }

    /**
     * Starts the proxy on the specified port. The proxy will listen for connections on the network interface specified by the bindAddress, and will
     * also initiate connections to upstream servers on the same network interface.
     *
     * @param port        port to listen on
     * @param bindAddress address of the network interface on which the proxy will listen for connections and also attempt to connect to upstream servers.
     * @throws java.lang.IllegalStateException if the proxy has already been started
     */
    public static void start(int port, InetAddress bindAddress) {
        proxy.start(port, bindAddress);
    }

    /**
     * Returns true if the proxy is started and listening for connections, otherwise false.
     */
    public static boolean isStarted() {
        return proxy.isStarted();
    }

    /**
     * Like {@link #stop()}, shuts down the proxy server and no longer accepts incoming connections, but does not wait for any existing
     * network traffic to cease. Any existing connections to clients or to servers may be force-killed immediately.
     * If the proxy was previously stopped or aborted, this method has no effect.
     *
     * @throws java.lang.IllegalStateException if the proxy has not been started
     */
    public static void abort() {
        proxy.abort();
    }

    /**
     * Stops accepting new client connections and initiates a graceful shutdown of the proxy server, waiting up to 5 seconds for network
     * traffic to stop. If the proxy was previously stopped or aborted, this method has no effect.
     *
     * @throws java.lang.IllegalStateException if the proxy has not been started.
     */
    public static void stop() {
        proxy.stop();
    }

    /**
     * Returns the actual port on which the proxy is listening for client connections.
     *
     * @throws java.lang.IllegalStateException if the proxy has not been started
     */
    public static int getPort() {
        return proxy.getPort();
    }

    /**
     * Starts a new HAR file with the default page name
     *
     * @return existing HAR file, or null if none exists or HAR capture was disabled
     */
    public static Har newHar() {
        return proxy.newHar();
    }

    /**
     * Starts a new HAR file with the specified initialPageRef as the page name and page title. Enables HAR capture if it was not previously enabled.
     *
     * @param initialPageRef initial page name of the new HAR file
     * @return existing HAR file, or null if none exists or HAR capture was disabled
     */
    public static Har newHar(String initialPageRef) {
        return proxy.newHar(initialPageRef);
    }

    /**
     * Starts a new HAR file with the specified page name and page title. Enables HAR capture if it was not previously enabled.
     *
     * @param initialPageRef   initial page name of the new HAR file
     * @param initialPageTitle initial page title of the new HAR file
     * @return existing HAR file, or null if none exists or HAR capture was disabled
     */
    public static Har newHar(String initialPageRef, String initialPageTitle) {
        return proxy.newHar(initialPageRef, initialPageTitle);
    }

    /**
     * Stops capturing traffic in the HAR. Populates the {@link net.lightbody.bmp.core.har.HarPageTimings#onLoad} value for the current page
     * based on the amount of time it has been captured.
     *
     * @return the existing HAR
     */
    public static Har endHar() {
        return proxy.endHar();
    }

    public static Har getHar() {
        return proxy.getHar();
    }

    public static void clearBlackList() {
        proxy.clearBlacklist();
    }

    public static void getBlacklist() {
        proxy.getBlacklist();
    }

    public static void setBlackListRequest(String request, int responseCode) {
        proxy.blacklistRequests(request, responseCode);
    }

    /**
     * Returns true if the proxy is started and listening for connections, otherwise false.
     */
    public static void setHarCaptureTypes(CaptureType... var1) {
        proxy.setHarCaptureTypes(var1);
    }

    public static void addRequestFilter(String filterContent, String modifiedContent) {
        proxy.addRequestFilter(new RequestFilter() {
            @Override
            public HttpResponse filterRequest(HttpRequest httpRequest, HttpMessageContents httpMessageContents, HttpMessageInfo httpMessageInfo) {
                if (httpMessageInfo.getOriginalUrl().endsWith(filterContent)) {
                    // retrieve the existing message contents as a String or, for binary contents, as a byte[]
                    String messageContents = httpMessageContents.getTextContents();
                    // do some manipulation of the contents
                    String newContents = messageContents.replaceAll(ORIGINAL_STRING, modifiedContent);
                    //[...]

                    // replace the existing content by calling setTextContents() or setBinaryContents()
                    httpMessageContents.setTextContents(newContents);
                }

                return null;
            }
        });
    }

    /**
     * Method to Modify Response Content
     *
     * @param filterContent:   String to filter Request
     * @param modifiedContent: Modified Content
     */
    public static void addResponseFilter(String filterContent, String modifiedContent) {
        proxy.addResponseFilter(new ResponseFilter() {
            @Override
            public void filterResponse(HttpResponse httpResponse, HttpMessageContents httpMessageContents, HttpMessageInfo httpMessageInfo) {
                if (httpMessageInfo.getOriginalUrl().endsWith(filterContent)) {
                    // retrieve the existing message contents as a String or, for binary contents, as a byte[]
                    String messageContents = httpMessageContents.getTextContents();
                    // do some manipulation of the contents
                    String newContents = messageContents.replaceAll(ORIGINAL_STRING, modifiedContent);
                    //[...]

                    // replace the existing content by calling setTextContents() or setBinaryContents()
                    httpMessageContents.setTextContents(newContents);
                }
            }
        });

    }

    /**
     * Method to Modify Response Content
     *
     * @param filterContent:   String to filter Request
     * @param modifiedContent: Modified Content
     * @param status:          Modified response status
     */
    public static void addResponseFilter(String filterContent, HttpResponseStatus status, String modifiedContent) {
        proxy.addResponseFilter(new ResponseFilter() {
            @Override
            public void filterResponse(HttpResponse httpResponse, HttpMessageContents httpMessageContents, HttpMessageInfo httpMessageInfo) {
                if (httpMessageInfo.getOriginalUrl().endsWith(filterContent)) {
                    httpResponse.setStatus(status);
                    // retrieve the existing message contents as a String or, for binary contents, as a byte[]
                    String messageContents = httpMessageContents.getTextContents();
                    // do some manipulation of the contents
                    String newContents = messageContents.replaceAll(ORIGINAL_STRING, modifiedContent);
                    //[...]
                    // replace the existing content by calling setTextContents() or setBinaryContents()
                    httpMessageContents.setTextContents(newContents);
                }
            }
        });

    }

    /**
     * Method to get  API Response
     *
     * @param filterContent :   String to filter Request
     * @param status        :    Modified response status
     */
    public static String getFilteredResponse(String filterContent, HttpResponseStatus status) {
        final String[] messageContents = new String[1];
        proxy.addResponseFilter(new ResponseFilter() {
            @Override
            public void filterResponse(HttpResponse httpResponse, HttpMessageContents httpMessageContents, HttpMessageInfo httpMessageInfo) {
                if (httpMessageInfo.getOriginalUrl().endsWith(filterContent)) {
                    httpResponse.setStatus(status);
                    // retrieve the existing message contents as a String or, for binary contents, as a byte[]
                    messageContents[0] = httpMessageContents.getTextContents();
                }
            }
        });
        return messageContents[0];
    }

    public static void addWhiteListPattern(String pattern) {
        proxy.addWhitelistPattern(pattern);
    }

    /**
     * Returns the current bandwidth limit for reading, in bytes per second.
     */
    public static long getReadBandwidthLimit() {
        return proxy.getReadBandwidthLimit();
    }

    /**
     * Sets the maximum bandwidth to consume when reading server responses.
     *
     * @param bytesPerSecond maximum bandwidth, in bytes per second
     */
    public static void setReadBandwidthLimit(long bytesPerSecond) {
        proxy.setReadBandwidthLimit(bytesPerSecond);
    }

    /**
     * Returns the current bandwidth limit for writing, in bytes per second.
     */
    public static long getWriteBandwidthLimit() {
        return proxy.getWriteBandwidthLimit();
    }

    /**
     * Sets the maximum bandwidth to consume when sending requests to servers.     *
     *
     * @param bytesPerSecond maximum bandwidth, in bytes per second
     */
    public static void setWriteBandwidthLimit(long bytesPerSecond) {
        proxy.setWriteBandwidthLimit(bytesPerSecond);
    }

    public static void set2GSpeed() {
        setWriteBandwidthLimit(200000);
        setReadBandwidthLimit(200000);
        setConnectTimeout(5, TimeUnit.SECONDS);
        setRequestTimeout(5, TimeUnit.SECONDS);
    }

    public static void set3GSpeed() {
        setWriteBandwidthLimit(8000000);
        setReadBandwidthLimit(8000000);
        setConnectTimeout(2, TimeUnit.SECONDS);
        setRequestTimeout(2, TimeUnit.SECONDS);
    }

    public static void set4GSpeed() {
        setWriteBandwidthLimit(15000000);
        setReadBandwidthLimit(15000000);
        setConnectTimeout(2, TimeUnit.SECONDS);
        setRequestTimeout(2, TimeUnit.SECONDS);
    }

    /**
     * Maximum amount of time to wait to establish a connection to a remote server. If the connection has not been established within the
     * specified time, the proxy will respond with an HTTP 502 Bad Gateway. The default value is 60 seconds.
     *
     * @param connectionTimeout maximum time to wait to establish a connection to a server, or 0 to wait indefinitely
     * @param timeUnit          TimeUnit for the connectionTimeout
     */
    public static void setConnectTimeout(int connectionTimeout, TimeUnit timeUnit) {
        proxy.setConnectTimeout(connectionTimeout, timeUnit);
    }

    /**
     * Maximum amount of time to allow a connection to remain idle. A connection becomes idle when it has not received data from a server
     * within the the specified timeout. If the proxy has not yet begun to forward the response to the client, the proxy
     * will respond with an HTTP 504 Gateway Timeout. If the proxy has already started forwarding the response to the client, the
     * connection to the client <i>may</i> be closed abruptly. The default value is 60 seconds.
     *
     * @param idleConnectionTimeout maximum time to allow a connection to remain idle, or 0 to wait indefinitely.
     * @param timeUnit              TimeUnit for the idleConnectionTimeout
     */
    public static void setIdleConnectionTimeout(int idleConnectionTimeout, TimeUnit timeUnit) {
        proxy.setIdleConnectionTimeout(idleConnectionTimeout, timeUnit);
    }

    /**
     * Maximum amount of time to wait for an HTTP response from the remote server after the request has been sent in its entirety. The HTTP
     * request must complete within the specified time. If the proxy has not yet begun to forward the response to the client, the proxy
     * will respond with an HTTP 504 Gateway Timeout. If the proxy has already started forwarding the response to the client, the
     * connection to the client <i>may</i> be closed abruptly. The default value is 0 (wait indefinitely).
     *
     * @param requestTimeout maximum time to wait for an HTTP response, or 0 to wait indefinitely
     * @param timeUnit       TimeUnit for the requestTimeout
     */
    public static void setRequestTimeout(int requestTimeout, TimeUnit timeUnit) {
        proxy.setIdleConnectionTimeout(requestTimeout, timeUnit);
    }

    public static List<String> getResponse() {
        List<HarEntry> harEntries = getHar().getLog().getEntries();
        List<String> list = new ArrayList<>();
        String response;
        for (HarEntry entry : harEntries) {
            response = entry.getResponse().getContent().getText();
            list.add(response);
        }
        endHar();
        return list;
    }

    public static void rewriteUrl(String urlPattern, String replacementExpression) {
        proxy.rewriteUrl(urlPattern, replacementExpression);
    }

    /**
     * Clears all existing rewrite rules.
     */
    public static void clearRewriteRules() {
        proxy.clearRewriteRules();
    }

    /**
     * Disables verification of all upstream servers' SSL certificates. All upstream servers will be trusted, even if they
     * do not present valid certificates signed by certification authorities in the JDK's trust store. <b>This option
     * exposes the proxy to MITM attacks and should only be used when testing in trusted environments.</b>
     *
     * @param trustAllServers when true, disables upstream server certificate verification
     */
    public static void setTrustAllServers(boolean trustAllServers) {
        proxy.setTrustAllServers(trustAllServers);
    }

    /**
     * Sets the {@link TrustSource} that contains trusted root certificate authorities that will be used to validate
     * upstream servers' certificates. When null, disables certificate validation (see warning at {@link #setTrustAllServers(boolean)}).
     *
     * @param trustSource TrustSource containing root CAs, or null to disable upstream server validation
     */
    public static void setTrustSource(TrustSource trustSource) {
        proxy.setTrustSource(trustSource);
    }

    /**
     * Generate and use default ssl certificate for Https sites
     */
    public static void useDefaultSSLCertificate() {
        // create a CA Root Certificate using default settings
        RootCertificateGenerator rootCertificateGenerator = RootCertificateGenerator.builder().build();
        // tell the ImpersonatingMitmManager  use the RootCertificateGenerator we just configured
        ImpersonatingMitmManager mitmManager = ImpersonatingMitmManager.builder()
                .rootCertificateSource(rootCertificateGenerator)
                .trustAllServers(true)
                .build();
        // tell LittleProxy to use the ImpersonatingMitmManager when MITMing
        DefaultHttpProxyServer.bootstrap().withManInTheMiddle(mitmManager);
    }

    /**
     * Use Existing ssl certificate in PKCS12 file format
     *
     * @param certificatePath: path to keystore.p12 file
     * @param privateKeyAlias: private Key Alias for the certificate
     * @param password:        Password for the certificate
     */
    public static void useCustomSSLCertificate(String certificatePath, String privateKeyAlias, String password) {
        // create a CA Root Certificate using default settings
        CertificateAndKeySource existingCertificateSource =
                new KeyStoreFileCertificateSource("PKCS12", new File(certificatePath), privateKeyAlias, password);
        // tell the ImpersonatingMitmManager  use the RootCertificateGenerator we just configured
        ImpersonatingMitmManager mitmManager = ImpersonatingMitmManager.builder()
                .rootCertificateSource(existingCertificateSource)
                .build();
        proxy.setMitmManager(mitmManager);
        // disable server certificate validation:
        proxy.setTrustAllServers(true);
    }

    /**
     * Starts the proxy on the specified port. The proxy will listen for connections on the network interface specified by the clientBindAddress, and will
     * initiate connections to upstream servers from the network interface specified by the serverBindAddress.
     *
     * @param port              port to listen on
     * @param clientBindAddress address of the network interface on which the proxy will listen for connections
     * @param serverBindAddress address of the network interface on which the proxy will connect to upstream servers
     * @throws java.lang.IllegalStateException if the proxy has already been started
     */
    void start(int port, InetAddress clientBindAddress, InetAddress serverBindAddress) {
        proxy.start(port, clientBindAddress, serverBindAddress);
    }

    /**
     * The minimum amount of time that will elapse between the time the proxy begins receiving a response from the server and the time the
     * proxy begins sending the response to the client.
     *
     * @param latency  minimum latency, or 0 for no minimum
     * @param timeUnit TimeUnit for the latency
     */
    public void setLatency(long latency, TimeUnit timeUnit) {
        proxy.setLatency(latency, timeUnit);
    }
}
