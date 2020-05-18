package com.example.adiapp.Common;

import android.os.CountDownTimer;

import com.example.adiapp.Models.Category;
import com.example.adiapp.Models.CurrentQuestion;
import com.example.adiapp.Models.Question;
import com.example.adiapp.QuestionFragment;
import com.google.firebase.database.core.utilities.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Common {
    public static final int TOTAL_TIME = 20*60*1000;
    public static List<Question>questionList=new ArrayList<>();
    public static List<CurrentQuestion>answerSheetList= new ArrayList<>();
    public static CountDownTimer countDownTimer ;
    public static int right_answer =0;
    public static int wrong_answer =0;


    public static Category selectedCategory = new Category();
    public static List<QuestionFragment> fragmentList = new ArrayList<>();
    public static TreeSet<String> selected_values  = new TreeSet<>();

    public enum ANSWER_TYPE{
        NO_ANSWER,
        WRONG_ANSWER,
        RIGHT_ANSWER
    }


}
