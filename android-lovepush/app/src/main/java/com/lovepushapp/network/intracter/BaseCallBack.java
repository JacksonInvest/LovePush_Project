package com.lovepushapp.network.intracter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// generic callback for api request.
public class BaseCallBack<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {

        // call when get respone.
        if (!response.isSuccessful()){
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        //call when get exception.
    }
}
