package com.lovepushapp.core.Component;

import com.lovepushapp.activities.ProfileEditActivity;
import com.lovepushapp.modules.Chat.ChatPresenter;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.BaseFragment;
import com.lovepushapp.modules.PostAds.PostAdsPresenter;
import com.lovepushapp.modules.RecommProfile.RecommPresenter;
import com.lovepushapp.modules.SaveAfterSocialLogin.SocialPresenter;


import javax.inject.Singleton;

import dagger.Component;




@Singleton
@Component(modules = {DiModule.class})

public interface DiComponent {

    void inject(BaseActivity baseActivity);

    void inject(BaseFragment baseFragment);

    void inject(ProfileEditActivity activity);

    void inject(LoginRegisterProfilePresenter loginRegisterProfilePresenter);
    void inject(SocialPresenter Socialpreseneter);

    void inject(PostAdsPresenter postAdsPresenter);

    void inject(RecommPresenter recommPresenter);

    void inject(ChatPresenter chatPresenter);

}