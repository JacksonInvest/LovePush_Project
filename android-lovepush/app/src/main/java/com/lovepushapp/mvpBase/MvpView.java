package com.lovepushapp.mvpBase;

/**
 * Base interface that any class that wants to act as a view in the MVP (Model view Presenter)
 * pattern must implement. Generally this interface will be extended by a more specific interface
 * that then usually will be implemented by an Activity or Fragment.
 */
public interface MvpView {

    <T> void onApiResponse(T test, String serviceMode);

    <T> void onApiError(T test);

    void onException(Exception e);

}