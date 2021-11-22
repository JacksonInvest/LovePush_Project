package com.lovepushapp.network.intracter;

import retrofit2.Response;

// interface to interact with view
public interface ResponseListner {
    <T>void onComplete(Response<T> response);
    Void onError(String message);
}
