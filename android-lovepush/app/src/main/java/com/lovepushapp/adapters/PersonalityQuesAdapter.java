package com.lovepushapp.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.lovepushapp.R;
import com.lovepushapp.callbacks.OnItemClickListenerCutom;
import com.lovepushapp.core.utils.DialogUtils;
import com.lovepushapp.core.utils.Util;
import com.lovepushapp.model.response.PersonalitytestModel.GetQuesOption;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonalityQuesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    OnItemClickListenerCutom listener;
    Activity context;
    public List<GetQuesOption> questions;
    Util appUtils;
    DialogUtils dialogUtils;
    int maxans;
    List<Integer> check = new ArrayList<>();


    public PersonalityQuesAdapter(Util appUtils, DialogUtils dialogUtils, Activity context,int maxans, List<GetQuesOption> questions, OnItemClickListenerCutom listener) {
        this.listener = listener;
        this.context = context;
        this.questions = questions;
        this.appUtils = appUtils;
        this.dialogUtils = dialogUtils;
        this.maxans = maxans;





    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_personality_que, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder aa, int pp) {
        final CommonViewHolder holder = (CommonViewHolder) aa;
        final int pos = holder.getAdapterPosition();



       holder.anscheck.setText(questions.get(pos).getGetQuesOptionDesc().getOption());



           holder.anscheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                   if (isChecked)
                   {
                       if (check.size()<maxans)
                       {
                           Log.e("onClick: ",""+"first" );
                           check.add(questions.get(pos).getId());
                           holder.anslayout.setBackground(context.getResources().getDrawable(R.drawable.bg_round_blue));
                           holder.anscheck.setChecked(true);

                       }
                       else
                       {
                           holder.anscheck.setChecked(false);
                           //Toast.makeText(context, "You cann't select more than "+maxans+" answers", Toast.LENGTH_SHORT).show();

                           Toast.makeText(context, context.getString(R.string.maxAnswerError)+" "+maxans, Toast.LENGTH_SHORT).show();
                       }


                   }
                   else
                   {

                       check.remove(questions.get(pos).getId());
                       //holder.anslayout.setBackgroundColor(context.getResources().getColor(R.color.white));
                       holder.anslayout.setBackground(context.getResources().getDrawable(R.drawable.bg_round_et));
                       holder.anscheck.setChecked(false);

                   }

                   Log.e("onClick: ","second"+check );

               }
           });








    }


    public String answeredlist()
    {
        if (!check.isEmpty())
        {
            StringBuilder sb = new StringBuilder();
            for (int i=0;i<check.size();i++)
            {
                sb.append(",");
                sb.append(check.get(i));
            }
            return sb.substring(1).toString();
        }
        else
        {
            return "";
        }

    }

    @Override
    public int getItemCount() {
        try {
            return questions.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ans_layout)
        LinearLayout anslayout;
        @BindView(R.id.ans_checkbox)
        CheckBox anscheck;

        public CommonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
