package com.lovepushapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.lovepushapp.R;
import com.lovepushapp.adapters.PersonalityQuesAdapter;
import com.lovepushapp.core.BaseActivity;
import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.core.utils.DialogUtils;
import com.lovepushapp.core.utils.GlobalsVariables;
import com.lovepushapp.model.response.PersonalitytestModel.GetQuesOption;
import com.lovepushapp.model.response.PersonalitytestModel.PersonalitytestModel;
import com.lovepushapp.model.response.SubmitPerQuesResponse;
import com.lovepushapp.model.response.deleteTestResponse;
import com.lovepushapp.modules.Login.LoginMvp;
import com.lovepushapp.modules.Login.LoginRegisterProfilePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalityTestActivity extends BaseActivity implements LoginMvp {

    @BindView(R.id.screenTitleTV)
    AppCompatTextView screenTitleTV;

    @BindView(R.id.questionRV)
    RecyclerView questionRV;
    PersonalityQuesAdapter adap;
    @BindView(R.id.quest_TV)
    AppCompatTextView questTV;
    int itempos = 0;
    PersonalitytestModel res;
    @BindView(R.id.max_ans_TV)
    AppCompatTextView maxAnsTV;
    boolean shortest = false;
    private LoginRegisterProfilePresenter loginRegisterProfilePresenter;


    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.progress_1)
    RoundCornerProgressBar roundCornerProgressBar;


    @Override
    public int getLayoutId() {
        return R.layout.activity_personality_test;
    }

    @Override
    public void manageToolBar() {
        screenTitleTV.setText(getString(R.string.personal_test));
    }

    private Activity context = PersonalityTestActivity.this;

    public static Intent createIntent(Context context) {
        return new Intent(context, PersonalityTestActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginRegisterProfilePresenter = new LoginRegisterProfilePresenter(this);
        loginRegisterProfilePresenter.attachView(this);
        getPersonalityQues();
        roundCornerProgressBar.setMax(10);
//        iconRoundCornerProgressBar.setIconImageResource(R.drawable.logo_img);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getPersonalityQues() {
        loginRegisterProfilePresenter.hitPersonalityQuestions();
    }

    @OnClick({R.id.backIV, R.id.nextTV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backIV:

                DialogUtils.showAlert1(context, getString(R.string.kindlyCompleteTest), () -> {


                });

                //   goback();
                break;

            case R.id.nextTV:
                if (adap != null) {

                    boolean isAllTick = true;


                    if (adap.answeredlist().isEmpty())
                    {
                     appUtils.setSnackbar(maxAnsTV,"Please answer the question to move next!!");
                    }
                    else {
                        Log.e( "onViewClicked: ", itempos+"");

                        if (itempos==1)
                        {
                            switch (adap.answeredlist())
                            {
                                case "5":
                                    shortest= true;
                                    roundCornerProgressBar.setMax(8);
                                    break;
                                case "6":
                                    shortest = true;
                                    roundCornerProgressBar.setMax(8);

                                    break;
                                case "7":
                                    shortest = true;
                                    roundCornerProgressBar.setMax(8);

                                    break;
                                default:
                                    shortest = false;
                                    roundCornerProgressBar.setMax(10);

                                    break;
                            }
                        }

                        Log.e( "onViewClicked: ", adap.answeredlist());

                        String userId = (String) sharedStorage.getSharedData(GlobalsVariables.STORAGE.STORAGE_KEYS.UserId, GlobalsVariables.STORAGE.TYPE_STRING);
                        loginRegisterProfilePresenter.hitSubmitPersonalityTest(userId,String.valueOf(res.getData().get(itempos).getGetQuestionDesc().getQuestionId()),adap.answeredlist());
//
                        if (itempos==0&&(adap.answeredlist().equalsIgnoreCase("1")||adap.answeredlist().equalsIgnoreCase("2")))
                        {
//                            Toast.makeText(context, "pos "+itempos, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(context, "answer "+adap.answeredlist(), Toast.LENGTH_SHORT).show();
                            //roundCornerProgressBar.setMax(8);
                        }


                    }

                    //false 10



                  /*  for (int i = 0; i < adap.questions.size(); i++) {
                        String answer = adap.selectedAnswers.get(i);
                        Log.e("ANsw->", i + " " + answer);
                        if (answer.equalsIgnoreCase("")) {
                            isAllTick = false;
                        } else if (answer.equalsIgnoreCase(getString(R.string.dummy_answer))) {
                            isAllTick = false;
                        }
                    }*/

                    StringBuilder sb = new StringBuilder();
                    ArrayList<String> resStrings = new ArrayList<>();
/*
                    for (int i = 0; i < adap.questions.size(); i++) {
                        Log.e("ID Selected-> ", adap.questions.get(i).getCheckedId() + "");
                        if (TextUtils.isEmpty(adap.questions.get(i).getCheckedId())) {
                            isAllTick = false;
                        } else {
                            if (isAllTick) {
                                resStrings.add(adap.questions.get(i).getCheckedId());
                                if (i == 0) {
                                    sb.append(adap.questions.get(i).getCheckedId());
                                } else {
                                    sb.append(GlobalsVariables.COMMA_SEPERATOR + adap.questions.get(i).getCheckedId());
                                }
                            }
                        }
                    }
*/

                   /* if (isAllTick) {
                        String finalStrArr = GlobalsVariables.ARRAY_START + sb.toString() + GlobalsVariables.ARRAY_END;
                        Log.e("finalStrArr", finalStrArr);
                        //  appUtils.showToast(getString(R.string.next));
                        loginRegisterProfilePresenter.hitSubmitPersonalityTest(resStrings);
                    } else {
                        appUtils.showToast(getString(R.string.all_answers));
                    }*/


                } else {
                    appUtils.showToast(getString(R.string.no_question_found));
                }
                break;
        }
    }

    private void setAdapter(List<GetQuesOption> questions,int maxans) {
        try {
            questionRV.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            adap = new PersonalityQuesAdapter(appUtils, dialogUtils, context,maxans, questions, (view, position, type, o) -> Toast.makeText(context, "on click", Toast.LENGTH_SHORT).show());
            questionRV.setAdapter(adap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> void onApiResponse(T test, String serviceMode) {
        if (test instanceof PersonalitytestModel) {


            res = (PersonalitytestModel) test;


           if (res.getTest_id() != null) {
               new AlertDialog.Builder(context)
                       .setTitle(getString(R.string.app_name))
                       .setCancelable(false)
                       .setMessage(getString(R.string.alreadyAttemptTest))

                       // Specifying a listener allows you to take an action before dismissing the dialog.
                       // The dialog is automatically dismissed when a dialog button is clicked.
                       .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int which) {

                               loginRegisterProfilePresenter.hit_delete_test();

                           }
                       })

                       // A null listener allows the button to dismiss the dialog and take no further action.
                       .setNegativeButton(getString(R.string.no),  new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int which) {
                               finish();

                           }
                       })
                       //.setIcon(android.R.drawable.ic_dialog_alert)
                       .show();
           }else {


               progressBar.setMax(res.getData().size());;
//            roundCornerProgressBar.setMax(res.getData().size());

               Log.e("PROGRESSM", String.valueOf(res.getData().size()));


               if (res.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                   questTV.setText(res.getData().get(itempos).getGetQuestionDesc().getQuestion());
                   if (res.getData().get(itempos).getMaxAnswers()== 1)
                   {
                       maxAnsTV.setText(getString(R.string.mark)+" "+res.getData().get(itempos).getMaxAnswers()+" "+getString(R.string.answerSmall));

                   }
                   else
                   {
                       //maxAnsTV.setText("Maximum Answers: "+res.getData().get(itempos).getMaxAnswers());
                       maxAnsTV.setText(getString(R.string.markMax)+" "+res.getData().get(itempos).getMaxAnswers()+" "+getString(R.string.answers));

                   }
//                roundCornerProgressBar.setMax(res.getData().size());
                   setAdapter(res.getData().get(itempos).getGetQuesOptions(),res.getData().get(itempos).getMaxAnswers());
               } else {
                   appUtils.showToast(getString(R.string.no_question_found));
               }
           }



        }
        if (test instanceof SubmitPerQuesResponse) {
            SubmitPerQuesResponse sub_res = (SubmitPerQuesResponse) test;
            appUtils.showToast(sub_res.getMessage());
            if (sub_res.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {
                itempos += 1;



                Log.e("PROGRESS", String.valueOf(itempos));
                progressBar.setProgress(itempos);
                roundCornerProgressBar.setProgress(itempos);

                Log.e( "onApiResponse: ",shortest+"");
                if (shortest)
                {
                    Log.e( "andr: ","dd");
                    if (itempos <8) {
                        Log.e( "andr: ","dd1");
                        questTV.setText(res.getData().get(itempos).getGetQuestionDesc().getQuestion());
                        if (res.getData().get(itempos).getMaxAnswers()==1)
                        {
                            maxAnsTV.setText(getString(R.string.mark)+" "+res.getData().get(itempos).getMaxAnswers()+" "+getString(R.string.answerSmall));

                        }
                        else
                        {
                            //maxAnsTV.setText("Maximum Answers: "+res.getData().get(itempos).getMaxAnswers());
                            maxAnsTV.setText(getString(R.string.markMax)+" "+res.getData().get(itempos).getMaxAnswers()+" "+getString(R.string.answers));

                        }
                        setAdapter(res.getData().get(itempos).getGetQuesOptions(),res.getData().get(itempos).getMaxAnswers());
//                        roundCornerProgressBar.setMax(res.getData().size());

                    }
                    else
                    {
                        DialogUtils.showAlert1(context, getString(R.string.congratsDone), () -> {

                            appUtils.startActivity(context,
                                    RecommendedProfileActivity.createIntent(context),
                                    false,
                                    GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                                    false,
                                    0
                            );
                            finish();

                        });



                    }
                }
                else
                {

                    Log.e( "baahar: ","dd1");
                    roundCornerProgressBar.setProgress(itempos);
                    if (itempos < 10) {
                        roundCornerProgressBar.setProgress(itempos);
                        questTV.setText(res.getData().get(itempos).getGetQuestionDesc().getQuestion());

                        if (res.getData().get(itempos).getMaxAnswers() == 1)
                        {
                            maxAnsTV.setText(getString(R.string.mark)+" "+res.getData().get(itempos).getMaxAnswers()+" "+getString(R.string.answerSmall));

                        }
                        else
                        {
                            //maxAnsTV.setText("Maximum Answers: "+res.getData().get(itempos).getMaxAnswers());
                            maxAnsTV.setText(getString(R.string.markMax)+" "+res.getData().get(itempos).getMaxAnswers()+" "+getString(R.string.answers));

                        }
                        setAdapter(res.getData().get(itempos).getGetQuesOptions(),res.getData().get(itempos).getMaxAnswers());
                    }
                    else
                    {
                        DialogUtils.showAlert1(context, getString(R.string.congratsDone), () -> {

                            appUtils.startActivity(context,
                                    RecommendedProfileActivity.createIntent(context),
                                    false,
                                    GlobalsVariables.ANIM_TYPE.RIGHT_TO_LEFT,
                                    false,
                                    0
                            );
                            finish();

                        });
                    }
                }

            }
        }

        if (test instanceof deleteTestResponse) {
            deleteTestResponse sub_res = (deleteTestResponse) test;
            if (sub_res.getStatus() == API_GLOBALS.RESPONSE_CODE.SUCCESS) {

                getPersonalityQues();

            }else {
                appUtils.showToast(sub_res.getMessage());
                finish();
            }
        }

    }

    @Override
    public <T> void onApiError(T test) {

    }

    @Override
    public void onException(Exception e) {

    }

    @Override
    public void onBackPressed() {
        DialogUtils.showAlert1(context, getString(R.string.kindlyCompleteTest), () -> {


        });
    }
}
