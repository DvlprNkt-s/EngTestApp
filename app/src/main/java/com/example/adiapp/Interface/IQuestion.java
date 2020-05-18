package com.example.adiapp.Interface;

import com.example.adiapp.Models.CurrentQuestion;

public interface IQuestion {
    CurrentQuestion getSelectedAnswer();
    void showCorrectAnswer();
    void disableAnswer ();
    void resetQuestion();
}
