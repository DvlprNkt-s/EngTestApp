package com.example.adiapp;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adiapp.Common.Common;
import com.example.adiapp.Interface.IQuestion;
import com.example.adiapp.Models.CurrentQuestion;
import com.example.adiapp.Models.Question;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment implements IQuestion {

    TextView txt_question_text;
    CheckBox chkbA,chkbB,chkbC,chkbD;

    Question question;
    int questionIndex = -1 ;





    public QuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View itemView = inflater.inflate(R.layout.fragment_question, container, false);

        //Get questions
        questionIndex=getArguments().getInt("index",-1);
        question= Common.questionList.get(questionIndex);

        //View
        txt_question_text=(TextView)itemView.findViewById(R.id.txt_question_text);
        txt_question_text.setText(question.getQuestionText());

        chkbA=(CheckBox)itemView.findViewById(R.id.chkbA);
        chkbA.setText(question.getAnswerA());
        chkbA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Common.selected_values.add(chkbA.getText().toString());
                else
                    Common.selected_values.remove(chkbA.getText().toString());
            }

        });

        chkbB=(CheckBox)itemView.findViewById(R.id.chkbB);
        chkbB.setText(question.getAnswerB());
        chkbB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Common.selected_values.add(chkbB.getText().toString());
                else
                    Common.selected_values.remove(chkbB.getText().toString());
            }

        });

        chkbC=(CheckBox)itemView.findViewById(R.id.chkbC);
        chkbC.setText(question.getAnswerC());
        chkbC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Common.selected_values.add(chkbC.getText().toString());
                else
                    Common.selected_values.remove(chkbC.getText().toString());
            }

        });

        chkbD=(CheckBox)itemView.findViewById(R.id.chkbD);
        chkbD.setText(question.getAnswerD());
        chkbD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Common.selected_values.add(chkbD.getText().toString());
                else
                    Common.selected_values.remove(chkbD.getText().toString());
            }

        });


        return itemView;
    }


    @Override
    public CurrentQuestion getSelectedAnswer() {
        //this method wiil return state of answer
        //right , wrong or normal
        CurrentQuestion currentQuestion= new CurrentQuestion(questionIndex,Common.ANSWER_TYPE.NO_ANSWER);
        StringBuilder result= new StringBuilder();
        if (Common.selected_values.size() >1 ){
            //if multichoice
            Object[] arrayAnswer = Common.selected_values.toArray();
            for(int i = 0 ;i<arrayAnswer.length;i++){
                if(i<arrayAnswer.length-1){
                    result.append(new StringBuilder(((String)arrayAnswer[i]).substring(0,1)).append(" , "));
                }else
                    result.append(new StringBuilder((String)arrayAnswer[i]).substring(0,1));


            }
        }else if (Common.selected_values.size()==1){
            //If only one choice
            Object[] arrayAnswer = Common.selected_values.toArray();
            result.append((String)arrayAnswer[0]).substring(0,1);
        }

        if(question!= null ){

            //Compare correctAnswer with user answer
            if(!TextUtils.isEmpty(result)) {
                if (result.toString().equals(question.getCorrectAnswer()))
                    currentQuestion.setType(Common.ANSWER_TYPE.RIGHT_ANSWER);
                else
                    currentQuestion.setType(Common.ANSWER_TYPE.WRONG_ANSWER);

            }
            else
                currentQuestion.setType(Common.ANSWER_TYPE.NO_ANSWER);
        }
        else{
            Toast.makeText(getContext(), "Cannot get question", Toast.LENGTH_SHORT).show();
            currentQuestion.setType(Common.ANSWER_TYPE.NO_ANSWER);
        }
        Common.selected_values.clear();//Always clear when compare done
        return currentQuestion;
    }

    @Override
    public void showCorrectAnswer() {

        //Bold correct answer
        String[] correctAnswer = question.getCorrectAnswer().split(" , ");
        for(String answer : correctAnswer){
            if(answer.equals("A")){
                chkbA.setTypeface(null,Typeface.BOLD);
                chkbA.setTextColor(Color.RED);
            }else  if(answer.equals("B")) {
                chkbB.setTypeface(null, Typeface.BOLD);
                chkbB.setTextColor(Color.RED);
            }else  if(answer.equals("C")) {
                chkbC.setTypeface(null, Typeface.BOLD);
                chkbC.setTextColor(Color.RED);
            }else  if(answer.equals("D")){
                chkbD.setTypeface(null,Typeface.BOLD);
                chkbD.setTextColor(Color.RED);

            }

        }

    }


    @Override
    public void disableAnswer() {
        chkbA.setEnabled(false);
        chkbB.setEnabled(false);
        chkbC.setEnabled(false);
        chkbD.setEnabled(false);


    }

    @Override
    public void resetQuestion() {
        //Enable checkbox
        chkbA.setEnabled(true);
        chkbB.setEnabled(true);
        chkbC.setEnabled(true);
        chkbD.setEnabled(true);

        //remove all selected
        chkbA.setChecked(false);
        chkbB.setChecked(false);
        chkbC.setChecked(false);
        chkbD.setChecked(false);

        //Remove all bold on text
        chkbA.setTypeface(null, Typeface.NORMAL);
        chkbA.setTextColor(Color.BLACK);
        chkbB.setTypeface(null, Typeface.NORMAL);
        chkbB.setTextColor(Color.BLACK);
        chkbC.setTypeface(null, Typeface.NORMAL);
        chkbC.setTextColor(Color.BLACK);
        chkbD.setTypeface(null, Typeface.NORMAL);
        chkbD.setTextColor(Color.BLACK);


    }
}
