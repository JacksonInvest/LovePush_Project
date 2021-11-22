package com.lovepushapp.core.Component;


import com.lovepushapp.core.utils.DialogUtils;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.core.utils.Util;
import com.lovepushapp.interfaces.RestApiInterface;
import com.lovepushapp.network.ApiHitter;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Provides;


@dagger.Module
public class DiModule {


    @Provides
    @Singleton
    public Util getUtil() {
        return new Util();
    }

    @Provides
    @Singleton
    public SharedStorage getSharedStorage() {
        return new SharedStorage();
    }



    @Provides
    @Singleton
    @Named(GlobalsVariables.DAGGERS.AUTH)
    RestApiInterface provideAuthRestService() {
        return new ApiHitter().RestConfigWithAuth().create(RestApiInterface.class);
    }

    //provides unauthorised rest service
    @Provides
    @Singleton
    @Named(GlobalsVariables.DAGGERS.NON_AUTH)
    RestApiInterface provideNonAuthRestService() {
        return new ApiHitter().RestConfigWithOutAuth().create(RestApiInterface.class);
    }

    @Provides
    DialogUtils getDialogUtils() {
        return new DialogUtils();
    }


}