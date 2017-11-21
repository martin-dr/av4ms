package net.droegemueller.av4ms.core.domain.client;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface Av4msApi {

    // http://gateway.marvel.com:80/messwerte
    @GET("messwerte")
    Observable<MesswerteResponse> getMesswerte();

    @GET("static/av4vers.js")
    Observable<String> getAv4VersBody();

    @GET("static/ext-version.txt")
    Observable<String> getAppExtensionVersionBody();
}
