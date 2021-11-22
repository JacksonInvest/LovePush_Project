package com.lovepushapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lovepushapp.R;
import com.lovepushapp.activities.SubscriptionWebActivity;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.core.utils.SharedStorage;
import com.lovepushapp.model.PlanUrl;
import com.lovepushapp.model.plans.PlansEntity;
import com.lovepushapp.model.response.Profile_Model.GetProfile;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.network.intracter.ResponseListner;
import com.lovepushapp.videoTestJava.utils.Consts;
import com.lovepushapp.videoTestJava.utils.SharedPrefsHelper;

import retrofit2.Response;

public class FragmentFreePlan extends Fragment {
    PlansEntity plansEntity;
    RecyclerView rvPlan;
    ImageView ivChecked;
    GetProfile profile;
    TextView txtAction;
    AlertDialogs progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_free, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPlan = view.findViewById(R.id.rvPlan);
        txtAction = view.findViewById(R.id.txtAction);
        ivChecked = view.findViewById(R.id.ivChecked);
        progressDialog = new AlertDialogs(getActivity());
        profile = getProfileModel();
        plansEntity = SharedPrefsHelper.getInstance().getPlans(Consts.PLANS);
        rvPlan.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rvPlan.setAdapter(new PlansAdapter(plansEntity.getData().get(0)));

        if (profile.getData().getPlan_details() != null) {
            if (profile.getData().getPlan_details().getPlanId() == plansEntity.getData().get(0).getId()) {
                ivChecked.setVisibility(View.VISIBLE);
                txtAction.setText("Cancel");
                txtAction.setBackgroundResource(R.drawable.grey_button);
            } else
                ivChecked.setVisibility(View.GONE);
        } else {
            ivChecked.setVisibility(View.VISIBLE);
            txtAction.setText("Cancel");
            txtAction.setBackgroundResource(R.drawable.grey_button);
        }

        txtAction.setOnClickListener(v -> {
            if (txtAction.getText().toString().equalsIgnoreCase("Subscribe")) {
                cancelSubscription();
            }
        });
    }

    private void cancelSubscription() {
        progressDialog.show();
        new LoginRegisterProfilePresenter(getActivity()).subscriptionOrCancel(profile.getToken(), "", new ResponseListner() {
            @Override
            public <T> void onComplete(Response<T> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    PlanUrl url = (PlanUrl) response.body();
                    if (url.getStatus() == 200)
                        getProfile(url.getMessage());
                    else
                        alertDialog(url.getMessage(), false);
                } else
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public Void onError(String message) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
    }

    private void getProfile(String message) {
        progressDialog.show();
        new LoginRegisterProfilePresenter(getActivity()).hitGetProfile(profile.getData().getId().toString(), new ResponseListner() {
            @Override
            public <T> void onComplete(Response<T> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    GetProfile en = (GetProfile) response.body();
                    en.setToken(profile.getToken());
                    new SharedStorage().setSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.GET_PROFILE, GlobalsVariables.STORAGE.TYPE_STRING, new Gson().toJson(en));
                    alertDialog(message, true);
                }

            }

            @Override
            public Void onError(String message) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
    }
    void alertDialog(String message, boolean b) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, id) -> {
            dialog.dismiss();
            if (b)
                getActivity().finish();
        });

        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    public GetProfile getProfileModel() {
        GetProfile response = null;
        try {
            response = new Gson().fromJson((String) SharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.GET_PROFILE,
                    GlobalsVariables.STORAGE.TYPE_STRING),
                    GetProfile.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

}
