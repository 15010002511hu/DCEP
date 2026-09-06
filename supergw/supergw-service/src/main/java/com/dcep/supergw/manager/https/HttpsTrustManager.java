package com.dcep.supergw.manager.https;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

public class HttpsTrustManager implements X509TrustManager {

    public HttpsTrustManager(String certFile, String passwrod)
        throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        /*KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(certFile), passwrod.toCharArray());
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        for ( TrustManager tm : trustManagers) {
            if(tm instanceof X509TrustManager) {
                x509TrustManager = (X509TrustManager) tm;
                return;
            }
        }
        throw new GwBaseException(GwServiceErrorEnum.READ_FILE_ERROR.getCode(),"Not cert file found!");*/
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        /*if (null != x509Certificates && 1 == x509Certificates.length) {
            x509Certificates[0].checkValidity();
        } else {
            x509TrustManager.checkServerTrusted(x509Certificates, s);
        }*/
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[]{};
    }
}
