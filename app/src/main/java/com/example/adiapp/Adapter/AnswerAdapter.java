package com.example.adiapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adiapp.Common.Common;
import com.example.adiapp.Models.CurrentQuestion;
import com.example.adiapp.R;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.MyViewHolder> {
    Context context;
    List<CurrentQuestion> currentQuestionList;


    public AnswerAdapter(Context context, List<CurrentQuestion> currentQuestionList) {
        this.context = context;
        this.currentQuestionList = currentQuestionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.layout_grid_answer_sheet_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
            if(currentQuestionList.get(i).getType()== Common.ANSWER_TYPE.RIGHT_ANSWER)
                    holder.question_item.setBackgroundResource(R.drawable.grid_right_question);
            else if(currentQuestionList.get(i).getType()== Common.ANSWER_TYPE.WRONG_ANSWER)
                holder.question_item.setBackgroundResource(R.drawable.grid_wrong_question);
            else
                holder.question_item.setBackgroundResource(R.drawable.grid_no_question_no_answer);

    }

    @Override
    public int getItemCount() {
        return currentQuestionList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        View question_item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            question_item=itemView.findViewById(R.id.question_item);
        }
    }
}
