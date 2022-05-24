package com.pyo.quizfrontapp.gamescreen.recycleview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pyo.quizfrontapp.R
import com.pyo.quizfrontapp.data.quizInfoDto

class QuizAdapter(val quizInfoDto: List<quizInfoDto>) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizAdapter.QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quizinfo,parent,false)
        return QuizViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPos : Int = adapterPosition
                val quizInfo : quizInfoDto = quizInfoDto[curPos]
                val view2 = LayoutInflater.from(parent.context).inflate(R.layout.alert_quiz_info,null)
                val mScorePopupImage =view2.findViewById<ImageView>(R.id.mScorePopupImage)
                val mScorePopupTitle =view2.findViewById<TextView>(R.id.mScorePopupTitle)
                val mScorePopupExample1 =view2.findViewById<TextView>(R.id.mScorePopupExample1)
                val mScorePopupExample2 =view2.findViewById<TextView>(R.id.mScorePopupExample2)
                val mScorePopupExample3 =view2.findViewById<TextView>(R.id.mScorePopupExample3)
                val mScorePopupExample4 =view2.findViewById<TextView>(R.id.mScorePopupExample4)
                if (quizInfoDto[curPos].filePath.contains("3303ccfa"))
                    mScorePopupImage.setImageResource(R.drawable.no_image)
                else
                    Glide.with(view2).load(quizInfo.filePath).onlyRetrieveFromCache(true).into(mScorePopupImage)
                mScorePopupTitle.text=quizInfo.title
                mScorePopupExample1.text=quizInfo.example1
                mScorePopupExample2.text=quizInfo.example2
                mScorePopupExample3.text=quizInfo.example3
                mScorePopupExample4.text=quizInfo.example4

                setAnswerColor(
                    quizInfo,
                    mScorePopupExample1,
                    mScorePopupExample2,
                    mScorePopupExample3,
                    mScorePopupExample4
                )

                val alertDialog = AlertDialog.Builder(parent.context)
                    .create()

                val butOk = view2.findViewById<Button>(R.id.mScorePopupOkButton)  // 요렇게 써도 됨
                butOk.setOnClickListener {
                    alertDialog.dismiss()
                }
                alertDialog.setView(view2)
                alertDialog.show()
            }
        }
    }

    private fun setAnswerColor(
        quizInfo: quizInfoDto,
        mScorePopupExample1: TextView,
        mScorePopupExample2: TextView,
        mScorePopupExample3: TextView,
        mScorePopupExample4: TextView
    ) {
        //답이다르면 색 둘다칠하고 같으면 빨간색만 칠함
        if (quizInfo.selectedAnswer.toInt()!=quizInfo.answer) {

            if (quizInfo.selectedAnswer.toInt() == 1) {
                mScorePopupExample1.setTextColor(Color.parseColor("#FF0000"))
            } else if (quizInfo.selectedAnswer.toInt() == 2) {
                mScorePopupExample2.setTextColor(Color.parseColor("#FF0000"))
            } else if (quizInfo.selectedAnswer.toInt() == 3) {
                mScorePopupExample3.setTextColor(Color.parseColor("#FF0000"))
            } else {
                mScorePopupExample4.setTextColor(Color.parseColor("#FF0000"))
            }
        }
        if (quizInfo.answer == 1) {
            mScorePopupExample1.setTextColor(Color.parseColor("#2196F3"))
        } else if (quizInfo.answer == 2) {
            mScorePopupExample2.setTextColor(Color.parseColor("#2196F3"))
        } else if (quizInfo.answer == 3) {
            mScorePopupExample3.setTextColor(Color.parseColor("#2196F3"))
        } else {
            mScorePopupExample4.setTextColor(Color.parseColor("#2196F3"))
        }
    }

    override fun onBindViewHolder(holder: QuizAdapter.QuizViewHolder, position: Int) {
        setImage(position, holder)
        holder.title.text=quizInfoDto[position].title
        val selectedAnswer = quizInfoDto[position].selectedAnswer
        if (selectedAnswer.toInt()==0)
            holder.selectedAnswer.text="선택\nX"
        else
            holder.selectedAnswer.text= "선택\n$selectedAnswer"
        holder.answer.text="정답\n"+quizInfoDto[position].answer.toString()
        if (quizInfoDto[position].answer == selectedAnswer.toInt())
        {
            holder.OX.text="O"
            holder.OX.setTextColor(Color.parseColor("#2196F3"))
        }
        else
        {
            holder.OX.text="X"
            holder.OX.setTextColor(Color.parseColor("#FF0000"))
        }

    }

    private fun setImage(
        position: Int,
        holder: QuizViewHolder
    ) {
        if (quizInfoDto.get(position).filePath.contains("3303ccfa"))
            holder.image.setImageResource(R.drawable.no_image)
        else
            Glide.with(holder.itemView).load(quizInfoDto[position].filePath).thumbnail(0.1f)
                .onlyRetrieveFromCache(true)
                .into(holder.image)
    }

    override fun getItemCount(): Int {
        return quizInfoDto.size
    }

    class QuizViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val image =itemView.findViewById<ImageView>(R.id.mScoreImage)
        val title =itemView.findViewById<TextView>(R.id.mScoreTitle)
        val selectedAnswer =itemView.findViewById<TextView>(R.id.mScoreSelectedAnswer)
        val answer =itemView.findViewById<TextView>(R.id.mScoreAnswer)
        val OX =itemView.findViewById<TextView>(R.id.mScoreOX)


    }

}