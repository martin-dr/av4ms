package net.droegemueller.av4ms.core.srv;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import net.droegemueller.av4ms.core.domain.client.Av4msApi;
import net.droegemueller.av4ms.core.domain.client.MesswerteResponse;
import net.droegemueller.av4ms.core.util.SchedulerProvider;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class ServerInteractorImpl implements ServerInteractor {

    @Override
    public void unbind() {
    }

    private SchedulerProvider scheduler;
    private PreferenceRepository prefs;

    @Inject
    ServerInteractorImpl(SchedulerProvider scheduler, PreferenceRepository prefs) {
        this.scheduler = scheduler;
        this.prefs = prefs;
    }

    @Override
    public Observable<MesswerteResponse> loadBasicData() {
        try {
            String url = prefs.getPrefServerUrl("");
            if (!url.endsWith("/")) url = url + "/";
            if (url.length() == 1) return Observable.error(new ServerInteractorException.NotConfigured());
            HttpUrl httpUrl = HttpUrl.parse(url);
            if (httpUrl == null) return Observable.error(new ServerInteractorException.BadUrl(url));
            return new Retrofit.Builder()
                    .baseUrl(httpUrl)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create(RxJava2CallAdapterFactory.createWithScheduler(scheduler.backgroundThread())))
                    .client(createOkHttpBuilder().build())
                    .build()
                    .create(Av4msApi.class)
                    .getMesswerte().onErrorResumeNext(new RxToApplicationExceptionConverter<>());
        } catch (Exception e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<Pair<Integer, Integer>> loadAv4msBasicVersion() {
        try {
            String url = prefs.getPrefServerUrl("");
            if (!url.endsWith("/")) url = url + "/";
            if (url.length() == 1) return Observable.error(new ServerInteractorException.NotConfigured());
            HttpUrl httpUrl = HttpUrl.parse(url);
            if (httpUrl == null) return Observable.error(new ServerInteractorException.BadUrl(url));
            CallAdapter.Factory factory = RxErrorHandlingCallAdapterFactory.create(RxJava2CallAdapterFactory.createWithScheduler(scheduler.backgroundThread()));
            return new Retrofit.Builder()
                    .baseUrl(httpUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(factory)
                    .client(createOkHttpBuilder().build())
                    .build()
                    .create(Av4msApi.class)
                    .getAv4VersBody().
                            map((Function<String, Pair<Integer, Integer>>) s -> {
                                try (BufferedReader reader = new BufferedReader(new StringReader(s))) {
                                    String line;
                                    Pattern p = Pattern.compile("^.*\"(\\d+)\\.(\\d+).*?\".*$");
                                    while ((line = reader.readLine()) != null) {
                                        Matcher m = p.matcher(line);
                                        if (m.matches()) {
                                            return new ImmutablePair<>(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
                                        }
                                    }
                                    return null;
                                }
                            }).onErrorResumeNext(new RxToApplicationExceptionConverter<>());
        } catch (Exception e) {
            return Observable.error(e);
        }
    }

    @Override
    public Observable<Pair<Integer, Integer>> loadAppExtensionVersion() {
        try {
            String url = prefs.getPrefServerUrl("");
            if (!url.endsWith("/")) url = url + "/";
            if (url.length() == 1) return Observable.error(new ServerInteractorException.NotConfigured());
            HttpUrl httpUrl = HttpUrl.parse(url);
            if (httpUrl == null) return Observable.error(new ServerInteractorException.BadUrl(url));
            CallAdapter.Factory factory = RxErrorHandlingCallAdapterFactory.create(RxJava2CallAdapterFactory.createWithScheduler(scheduler.backgroundThread()));
            return new Retrofit.Builder()
                    .baseUrl(httpUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(factory)
                    .client(createOkHttpBuilder().build())
                    .build()
                    .create(Av4msApi.class)
                    .getAppExtensionVersionBody().
                            map((Function<String, Pair<Integer, Integer>>) s -> {
                                try (BufferedReader reader = new BufferedReader(new StringReader(s))) {
                                    String line;
                                    Pattern p = Pattern.compile("^.\\s*(\\d+)(\\.(\\d+))?.*?$");
                                    while ((line = reader.readLine()) != null) {
                                        Matcher m = p.matcher(line);
                                        if (m.matches()) {
                                            String subversion = m.group(2);
                                            return new ImmutablePair<>(Integer.parseInt(m.group(1)),
                                                    subversion == null || subversion.length() == 0 ? 0 : Integer.parseInt(subversion));
                                        }
                                    }
                                    return null;
                                }
                            }).onErrorResumeNext(new RxToApplicationExceptionConverter<>());
        } catch (Exception e) {
            return Observable.error(e);
        }
    }

    private OkHttpClient.Builder createOkHttpBuilder() {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS);
        if (prefs.getPrefServerDoAuth(false)) {
            okHttpBuilder = okHttpBuilder.addInterceptor(chain -> {
                Request newRequest = chain.request().newBuilder()
                        .header("Authorization",
                                Credentials.basic(prefs.getPrefServerUsername(""), prefs.getPrefServerPassword("")))
                        .build();
                return chain.proceed(newRequest);
            });
        }
        return okHttpBuilder;
    }

    private static class RxToApplicationExceptionConverter<T> implements Function<Throwable, ObservableSource<? extends T>> {
        @Override
        public ObservableSource<? extends T> apply(Throwable throwable) throws Exception {
            if (throwable instanceof RetrofitException) {
                RetrofitException rxException = (RetrofitException) throwable;
                ServerInteractorException siException = ServerInteractorException.fromRetrofitException(rxException);
                return Observable.error(siException != null ? siException : rxException);
            }
            return Observable.error(throwable);
        }
    }
}