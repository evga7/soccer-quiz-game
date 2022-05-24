package com.pyo.quizfrontapp.rankscreen
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakao.adfit.ads.AdListener
import com.kakao.adfit.ads.ba.BannerAdView
import com.pyo.quizfrontapp.BuildConfig
import com.pyo.quizfrontapp.R
import com.pyo.quizfrontapp.data.UserRankInfo
import com.pyo.quizfrontapp.databinding.ActivityRankScreenBinding
import com.pyo.quizfrontapp.rankscreen.recycleview.RankAdapter
import kotlin.math.roundToInt

class RankScreenActivity:AppCompatActivity(), RankScreenContract.View {
    private lateinit var presenter: RankScreenPresenter
    private var mBinding : ActivityRankScreenBinding?=null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank_screen)//설정
        presenter= RankScreenPresenter(this@RankScreenActivity)
        presenter.getRankAndSave()
        presenter.getPlayerRank(this)
        presenter.view.showAdFit()

    }


    override fun initView() {
        mBinding = ActivityRankScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onBackPressed() {
        presenter.view.showPopup("메인으로 돌아가시겠습니까?")
    }

    override fun showPopup(msg:String) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.alert_popup2, null)
        val textView =view.findViewById<TextView>(R.id.mBackPopupTitle)
        textView.text = msg
        val alertDialog = AlertDialog.Builder(this)
            .create()
        val cancelButton = view.findViewById<Button>(R.id.mBackPopupCancel)  // 요렇게 써도 됨
        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }
        val okButton = view.findViewById<Button>(R.id.mBackPopupOk)
        okButton.setOnClickListener {
            alertDialog.dismiss()
            finish()
        }
        alertDialog.setView(view)
        alertDialog.show()
    }

    override fun showRank() {
        val rv = binding.rankRecycler
        rv.layoutManager =
            LinearLayoutManager(this@RankScreenActivity, LinearLayoutManager.VERTICAL, false)
        rv.setHasFixedSize(true)
        rv.adapter = RankAdapter(presenter.getRankTable())
    }

    override fun showPlayerRank(userRankInfo: UserRankInfo) {
        binding.mRankPlayerNumber.text=(userRankInfo.id+1).toString()+'.'
        binding.mRankPlayerNickName.text=userRankInfo.nickName
        binding.mRankPlayerSolved.text="맞춘 퀴즈\n"+userRankInfo.solvedQuizCount.toString()
        binding.mRankPlayerTotal.text="총 푼 퀴즈 개수\n"+userRankInfo.totalQuizCount.toString()
        val percentageOfAnswer = userRankInfo.percentageOfAnswer*100
        val percentage = Math.round((percentageOfAnswer * 1000).roundToInt() / 10f) / 100f
        binding.mRankPercentage.text= "퀴즈 맞힐 확률\n$percentage%"

    }

    override fun showAdFit() {
        val adView: BannerAdView = binding.adFitView
        adView.setClientId(BuildConfig.KAKAO_AD_ID)  // 할당 받은 광고단위 ID 설정
        adView.setAdListener(object : AdListener {  // optional :: 광고 수신 리스너 설정
            override fun onAdLoaded() {
            }

            override fun onAdFailed(errorCode: Int) {
            }

            override fun onAdClicked() {
            }

        })
        // activity 또는 fragment의 lifecycle에 따라 호출
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                adView.resume()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                adView.pause()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                adView.destroy()
            }

        })
        adView.loadAd()
    }

    override fun onResume() {
        super.onResume()

        // lifecycle 사용이 불가능한 경우
        binding.adFitView.resume()
    }

    override fun onPause() {
        super.onPause()

        // lifecycle 사용이 불가능한 경우
        binding.adFitView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()

        // lifecycle 사용이 불가능한 경우
        binding.adFitView.destroy()
    }
}