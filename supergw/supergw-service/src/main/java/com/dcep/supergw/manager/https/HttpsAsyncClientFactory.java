package com.dcep.supergw.manager.https;

public class HttpsAsyncClientFactory {

    private volatile static HttpsAsyncClient client = new HttpsAsyncClient();

    private HttpsAsyncClientFactory() {
    }

    public static HttpsAsyncClient getInstance() {
        if (!client.getClient().isRunning()) {
            synchronized (client) {
                if (!client.getClient().isRunning()) {
                    client.getClient().start();
                }
            }
        }
        return client;
    }
}
