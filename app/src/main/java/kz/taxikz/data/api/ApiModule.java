package kz.taxikz.data.api;

import android.app.Application;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import kz.taxikz.AppConfig;
import kz.taxikz.data.api.service.AuthService;
import kz.taxikz.data.api.service.AutoCompleteService;
import kz.taxikz.data.api.service.ClientApiService;
import kz.taxikz.data.api.service.CostService;
import kz.taxikz.data.api.service.GoogleService;
import kz.taxikz.data.api.service.NewsService;
import kz.taxikz.data.api.service.OrderApiService;
import kz.taxikz.data.api.service.ParamsService;
import kz.taxikz.taxi4.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

@Module
public final class ApiModule {

    @Singleton
    @Provides
    AuthService provideAuthService(Retrofit retrofit) {
        return retrofit.create(AuthService.class);
    }

    @Singleton
    @Provides
    CostService provideCostService(Retrofit retrofit) {
        return retrofit.create(CostService.class);
    }

    @Singleton
    @Provides
    OrderApiService provideOrderApiService(Retrofit retrofit) {
        return retrofit.create(OrderApiService.class);
    }

    @Singleton
    @Provides
    NewsService provideNewsService(Retrofit retrofit) {
        return retrofit.create(NewsService.class);
    }

    @Singleton
    @Provides
    ParamsService provideParamService(Retrofit retrofit) {
        return retrofit.create(ParamsService.class);
    }

    @Singleton
    @Provides
    AutoCompleteService provideAutoCompleteService(Retrofit retrofit) {
        return retrofit.create(AutoCompleteService.class);
    }

    @Singleton
    @Provides
    ClientApiService provideClientApiService(Retrofit retrofit) {
        return retrofit.create(ClientApiService.class);
    }

    @Singleton
    @Provides
    GoogleService provideGoogleService(Retrofit retrofit) {
        return retrofit.create(GoogleService.class);
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(Application app) {
        Retrofit retrofit = null;
        try {
            retrofit = new Builder().baseUrl(AppConfig.API_DOMAIN)
                    .client(new OkHttpClient.Builder()
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .readTimeout(15, TimeUnit.SECONDS)
                            .dns(hostname -> {
                                List<InetAddress> addresses = new ArrayList<>();
                                for (String ip : AppConfig.API_SERVER_IP_LIST) {
                                    addresses.add(InetAddress.getByName(ip));
                                }
                                return addresses;
                            })
                            .addInterceptor(new HttpLoggingInterceptor()
                                    .setLevel(Level.BODY))
                            .sslSocketFactory(getSSLConfig(app.getApplicationContext())
                                    .getSocketFactory(), new X509TrustManager() {
                                @Override
                                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                                }

                                @Override
                                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                                }

                                @Override
                                public X509Certificate[] getAcceptedIssuers() {
                                    return new X509Certificate[0];
                                }
                            })
                            .build()).addConverterFactory(GsonConverterFactory.create()).build();
        } catch (Exception e) {
            Timber.e("provideRetrofit: %s", e.toString());
        }
        return retrofit;
    }

    private static SSLContext getSSLConfig(Context context) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        try {
            InputStream certDevCA = context.getResources().openRawResource(R.raw.dev_cert);
            InputStream certDev2CA = context.getResources().openRawResource(R.raw.dev2_cert);
            InputStream certAndroidCA = context.getResources().openRawResource(R.raw.android_cert);
            Certificate devCA = cf.generateCertificate(certDevCA);
            Certificate dev2CA = cf.generateCertificate(certDev2CA);
            Certificate androidCA = cf.generateCertificate(certAndroidCA);
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", devCA);
            keyStore.setCertificateEntry("ca1", dev2CA);
            keyStore.setCertificateEntry("ca2", androidCA);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            return sslContext;
        } catch (CertificateException e) {
            return null;
        }
    }
}
