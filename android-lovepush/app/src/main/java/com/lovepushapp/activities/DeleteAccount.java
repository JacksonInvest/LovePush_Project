package com.lovepushapp.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.lovepushapp.R;
import com.lovepushapp.core.utils.AlertDialogs;
import com.lovepushapp.model.DeleteAccountModel;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;
import com.lovepushapp.network.intracter.ResponseListner;
import com.lovepushapp.quickBloxTest.utils.SharedPrefsHelper;

import java.util.HashMap;

import retrofit2.Response;

public class DeleteAccount extends AppCompatActivity {
CheckBox chFreezing;
CheckBox chBadUser;
CheckBox chAds;
CheckBox chPrivacy;
CheckBox chForcedSocial;
CheckBox chDisinterest;
CheckBox chLittleActivity;
CheckBox chOther;
HashMap<String,String>reasons;
TextView txtContinue;
TextView txtCancel;
EditText etDescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);


        reasons=new HashMap<>();
        etDescription=findViewById(R.id.etDescription);
        chFreezing=findViewById(R.id.chFreezing);
        chBadUser=findViewById(R.id.chBadUser);
        chAds=findViewById(R.id.chAds);
        chPrivacy=findViewById(R.id.chPrivacy);
        chForcedSocial=findViewById(R.id.chForcedSocial);
        chDisinterest=findViewById(R.id.chDisinterest);
        chLittleActivity=findViewById(R.id.chLittleActivity);
        chOther=findViewById(R.id.chOther);
        txtContinue=findViewById(R.id.txtContinue);
        txtCancel=findViewById(R.id.txtCancel);
        
        manageCheced();
        clickListners();
    }

    private void clickListners() {
       TextView view= findViewById(R.id.txtPlease);
        view.setTypeface(null, Typeface.BOLD);
        txtContinue.setOnClickListener(v->{
            if(validate())
                deleteDialog();
        });
        txtCancel.setOnClickListener(v->{
            onBackPressed();
        });
    }

    private void deleteDialog() {
         new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.app_name))
                        .setMessage(getString(R.string.delete_account_alert))

                        .setPositiveButton(getString(R.string.yes), (dialog, which) -> {

                          deleteUser();

                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(getString(R.string.no), null)
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
    }

    private void deleteUser() {
        AlertDialogs dialogs=new AlertDialogs(this);
        dialogs.show();
        LoginRegisterProfilePresenter inttracter= new LoginRegisterProfilePresenter(this);
        inttracter.hit_delete_account(reasons.values().toString().replace("[","").replace("]",""), etDescription.getText().toString(), new ResponseListner() {
            @Override
            public <T> void onComplete(Response<T> response) {
                if (response.isSuccessful())
                {
                    dialogs.dismiss();
                    DeleteAccountModel model=(DeleteAccountModel)response.body();
                    Toast.makeText(DeleteAccount.this,model.getMessage() , Toast.LENGTH_SHORT).show();
                    SharedPrefsHelper.getInstance().clearAllData();
                    Intent intent=new Intent(DeleteAccount.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public Void onError(String message) {
                dialogs.dismiss();
                Toast.makeText(DeleteAccount.this, message, Toast.LENGTH_SHORT).show();
                return null;
            }
        });
    }

    private boolean validate() {
        if (reasons.size()==0)
        {
            Toast.makeText(this, "Select Reasons", Toast.LENGTH_SHORT).show();
            return  false;
        } else if (etDescription.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show();
            return  false;
        }
        return true;
    }

    private void manageCheced() {
        chFreezing.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                reasons.put("freezing","freezing");
            else
                reasons.remove("freezing");
        });
        chBadUser.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                reasons.put("badUSer","Bad User Experience");
            else
                reasons.remove("badUSer");
        });
        chAds.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                reasons.put("instrusiveAdds","Instrusive adds");
            else
                reasons.remove("instrusiveAdds");
        });
        chPrivacy.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                reasons.put("privacyConcers","Privacy Concerns");
            else
                reasons.remove("privacyConcers");
        });
        chForcedSocial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                reasons.put("chForcedSocial","Forced Social logins");
            else
                reasons.remove("chForcedSocial");
        });
        chDisinterest.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                reasons.put("chDisinterest","Disinterest");
            else
                reasons.remove("chDisinterest");
        });
        chLittleActivity.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                reasons.put("chLittleActivity","Little activity in my area");
            else
                reasons.remove("chLittleActivity");
        });
        chOther.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                reasons.put("chOther","Other");
            else
                reasons.remove("chOther");
        });
    }
}
