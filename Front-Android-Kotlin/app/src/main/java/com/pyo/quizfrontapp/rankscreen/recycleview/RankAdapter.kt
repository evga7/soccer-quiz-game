package com.pyo.quizfrontapp.rankscreen.recycleview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pyo.quizfrontapp.R
import com.pyo.quizfrontapp.data.UserRankInfo
import kotlin.math.roundToInt

class RankAdapter(val userRankInfo : List<UserRankInfo>) : RecyclerView.Adapter<RankAdapter.RankViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rankinfo,parent,false)

        return RankViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.rankNubmer.text = (position+1).toString() + '.'
        holder.nickName.text= userRankInfo[position].nickName
        holder.solved.text="맞춘 퀴즈\n"+ userRankInfo[position].solvedQuizCount.toString()
        holder.total.text="총 푼 퀴즈 개수\n"+ userRankInfo[position].totalQuizCount.toString()
        val percentageOfAnswer = userRankInfo[position].percentageOfAnswer*100
        val percentage = ((percentageOfAnswer * 1000).roundToInt() / 10f).roundToInt() / 100f
        holder.percentage.text="퀴즈 맞힐 확률\n"+percentage+'%'
        setTopRankImage(position, holder)
    }
//보니까 리사이클러뷰를 10개씩 가져오는거같음 그래서 11등인가부터 또 1등으로되버림 포지션으로하니까
    // 첫번째시도 모델에 불린배열 3개해서 함 생각해보니 걍 카운트로해도될듯함
    //근데 안됨 보니까 holder를 재활용하는건지 10개 정도 그리면 그다음또 1,2,3등으로 나옴
    //홀더자체를 하면안될거같
    private fun setTopRankImage(
        position: Int,
        holder: RankViewHolder
    ) {
        if (position==0) {
            holder.image.setImageResource(R.drawable.rank1)
        }
        else if (position==1) {
            holder.image.setImageResource(R.drawable.rank2)
        }
        else if (position==2) {
            holder.image.setImageResource(R.drawable.rank3)
        }
    }

    override fun getItemCount(): Int {
        return userRankInfo.size
    }

    class RankViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val rankNubmer=itemView.findViewById<TextView>(R.id.mRankNumber)
        val image =itemView.findViewById<ImageView>(R.id.mMoreTrollImage)
        val nickName =itemView.findViewById<TextView>(R.id.mRankNickName)
        val solved =itemView.findViewById<TextView>(R.id.mRankSolved)
        val total =itemView.findViewById<TextView>(R.id.mRankTotal)
        val percentage =itemView.findViewById<TextView>(R.id.mRankPercentage)


    }
}