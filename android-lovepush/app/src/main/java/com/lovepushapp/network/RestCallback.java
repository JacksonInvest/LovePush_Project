package com.lovepushapp.network;

import retrofit2.Call;
import retrofit2.Response;


public interface RestCallback<T> {

    void onSuccess(Call<T> call, Response<T> model, String serviceMode);

    void onTimeOut(Call<T> call, Throwable t, String serviceMode);

    void onFailure(Call<T> call, Throwable t, String serviceMode);


}