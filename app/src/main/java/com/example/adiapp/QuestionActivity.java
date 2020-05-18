package com.example.adiapp;

import android.os.Bundle;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.adiapp.Adapter.AnswerAdapter;
import com.example.adiapp.Adapter.QuestionFragmentAdapter;
import com.example.adiapp.Common.Common;
import com.example.adiapp.DBHelper.DBAdidas;
import com.example.adiapp.DBHelper.DBAdidas;
import com.example.adiapp.Models.CurrentQuestion;
import com.example.adiapp.Models.Question;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.CountDownTimer;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.TextView;

import java.util.ConcurrentModificationException;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import static com.example.adiapp.Common.Common.answerSheetList;
import static com.example.adiapp.Common.Common.countDownTimer;
import static com.example.adiapp.Common.Common.answerSheetList;
import static com.example.adiapp.Common.Common.fragmentList;
import static com.example.adiapp.Common.Common.questionList;

public class QuestionActivity extends AppCompatActivity {
    private int time_play = Common.TOTAL_TIME;
    private boolean isAnswerModelView = false;
    private RecyclerView answer_sheet_view;
    private AnswerAdapter answerAdapter;
    private AppBarConfiguration mAppBarConfiguration;



    TextView text_right_answer,text_timer;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onDestroy() {
        if(Common.countDownTimer!=null){
            Common.countDownTimer.cancel();
        }

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(Common.selectedCategory.getName());
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);



        takeQuestion();

        if (Common.questionList.size()>0) {

            //Show TextView  right Answer
            text_right_answer = (TextView) findViewById(R.id.text_question_right);
            text_timer = (TextView) findViewById(R.id.text_timer);

            text_right_answer.setVisibility(View.VISIBLE);
            text_timer.setVisibility(View.VISIBLE);

            text_right_answer.setText(new StringBuilder(String.format("%d/%d", Common.right_answer, questionList.size())));

            countTimer();


            //Answer View
            answer_sheet_view = (RecyclerView) findViewById(R.id.grid_answer);
            answer_sheet_view.setHasFixedSize(true);
            if (Common.questionList.size() > 5) {
                answer_sheet_view.setLayoutManager(new GridLayoutManager(this, Common.questionList.size() / 2));
                answerAdapter = new AnswerAdapter(this, Common.answerSheetList);
                answer_sheet_view.setAdapter(answerAdapter);


                viewPager = (ViewPager)findViewById(R.id.viewpager);
                tabLayout=(TabLayout)findViewById(R.id.sliding_tabs);

                genFragmentList();

                QuestionFragmentAdapter questionFragmentAdapter = new QuestionFragmentAdapter(getSupportFragmentManager(),
                        this,
                        Common.fragmentList);
                viewPager.setAdapter(questionFragmentAdapter);

                tabLayout.setupWithViewPager(viewPager);


                //Event
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    int SCROLLING_RIGHT = 0;
                    int SCROLLING_LEFT = 1 ;
                    int SCROLLING_UNDETERMINED=2;

                    int currentScrollDirection = 2 ;

                    private void setScrollingDirection(float positionOffset ){

                        if((1-positionOffset)>=0.5)
                            this.currentScrollDirection=SCROLLING_RIGHT;
                        else  if((1-positionOffset)<=0.5)
                            this.currentScrollDirection=SCROLLING_LEFT;
                    }

                    private boolean isScrollDirectionUndetermined(){
                        return  currentScrollDirection== SCROLLING_UNDETERMINED;
                    }

                    public boolean isScrollingRight(){
                        return currentScrollDirection==SCROLLING_RIGHT;
                    }

                    public boolean isScrollingLeft(){
                        return currentScrollDirection==SCROLLING_LEFT;
                    }



                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        if(isScrollDirectionUndetermined())
                            setScrollingDirection(positionOffset);

                    }

                    @Override
                    public void onPageSelected(int i) {
                        QuestionFragment questionFragment;
                        int position = 0 ;

                        if(i>0 ){
                            if (isScrollingRight())
                            {
                                //if user scroll to right , get previous fragment to calculate result
                              questionFragment= Common.fragmentList.get(i-1);
                              position=i-1;
                            }
                            else if (isScrollingLeft())
                            {
                                //if user scroll to left , get next fragment to calculate result
                                questionFragment= Common.fragmentList.get(i+1);
                                position=i+1;
                            }
                            else
                                {
                                    questionFragment= Common.fragmentList.get(position);
                                }
                        }
                        else
                            {
                                questionFragment=Common.fragmentList.get(0);
                                position=0;
                            }

                        //if you want want to show correct answer , just call function here
                        CurrentQuestion question_state = questionFragment.getSelectedAnswer();
                        Common.answerSheetList.set(position,question_state);//set question answer for answersheet
                        answerAdapter.notifyDataSetChanged();//change color in answersheet

                        countCorrectAnswer();

                        text_right_answer.setText(new StringBuilder(String.format("%d",Common.right_answer))
                                .append("/")
                                .append(String.format("%d", questionList.size())).toString());

                        if(question_state.getType()==Common.ANSWER_TYPE.NO_ANSWER)
                        {
                            questionFragment.showCorrectAnswer();
                            questionFragment.disableAnswer();
                        }

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                         if (state== ViewPager.SCROLL_STATE_IDLE)
                             this.currentScrollDirection=SCROLLING_UNDETERMINED;
                    }
                });
            }




        }
    }

    private void countCorrectAnswer() {
        //reset variable
        Common.right_answer=0;
        Common.wrong_answer=0;
        for (CurrentQuestion item: Common.answerSheetList)
            if(item.getType()==Common.ANSWER_TYPE.RIGHT_ANSWER)
                Common.right_answer++;
            else if(item.getType()==Common.ANSWER_TYPE.WRONG_ANSWER)
                Common.wrong_answer++;
    }

    private void genFragmentList() {
        for(int i = 0 ;i< Common.questionList.size();i++){
            Bundle bundle = new Bundle();
            bundle.putInt("index",i);
            QuestionFragment fragment = new QuestionFragment();
            fragment.setArguments(bundle);

            Common.fragmentList.add(fragment);
        }
    }

    private void countTimer() {

        if(Common.countDownTimer==null){

            countDownTimer=new CountDownTimer(Common.TOTAL_TIME,1000) {
                @Override
                public void onTick(long milli) {
                    text_timer.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(milli),
                            TimeUnit.MILLISECONDS.toSeconds(milli)-
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milli))));
                    time_play-=1000;
                }

                @Override
                public void onFinish() {

                }
            }.start();

        }
        else{
            countDownTimer.cancel();
            countDownTimer=new CountDownTimer(Common.TOTAL_TIME,1000) {
                @Override
                public void onTick(long milli) {
                    text_timer.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(milli),
                            TimeUnit.MILLISECONDS.toSeconds(milli)-
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milli))));
                    time_play-=1000;
                }

                @Override
                public void onFinish() {

                }
            }.start();
        }
    }

    private void takeQuestion(){
       Common.questionList= DBAdidas.getInstance(this).getQuestionByCategory(Common.selectedCategory.getId());
            if(Common.questionList.size()==0){

                new MaterialStyledDialog.Builder(this)
                        .setTitle("Oppps!")
                        .setIcon(R.drawable.ic_sentiment_very_dissatisfied_black_24dp)
                        .setDescription("We don`t have any question in this " + Common.selectedCategory.getName() + " " + "category")
                        .setPositiveText("OK")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).show();
            }
            else{
                    if(Common.answerSheetList.size()>0){
                        Common.answerSheetList.clear();
                    }

                for (int j = 0 ;j< questionList.size();j++)
                        Common.answerSheetList.add(new CurrentQuestion(j, Common.ANSWER_TYPE.NO_ANSWER));

                }


        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.grid_answer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
