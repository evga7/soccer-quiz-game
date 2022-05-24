package com.pyo.quizfrontapp.morescreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.kakao.adfit.ads.AdListener
import com.kakao.adfit.ads.ba.BannerAdView
import com.pyo.quizfrontapp.BuildConfig
import com.pyo.quizfrontapp.R
import com.pyo.quizfrontapp.data.ServerInfo
import com.pyo.quizfrontapp.databinding.ActivityMoreScreenBinding
import kotlin.math.roundToInt

class MoreScreenActivity:AppCompatActivity(), MoreScreenContract.View {
    private lateinit var presenter: MoreScreenPresenter
    private var mBinding : ActivityMoreScreenBinding?=null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_screen)//설정
        presenter= MoreScreenPresenter(this@MoreScreenActivity)
        presenter.getServerInfo()
        presenter.view.showAdFit()
        binding.mMorePostMessage.setOnClickListener {
            presenter.view.showPopupMessage()
        }

    }
    override fun initView() {
        mBinding= ActivityMoreScreenBinding.inflate(layoutInflater)
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
    override fun showPopup2(msg:String) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.alert_popup, null)
        val textView =view.findViewById<TextView>(R.id.popupTextView)
        textView.text = msg

        val alertDialog = AlertDialog.Builder(this)
            .create()
        val butSave = view.findViewById<Button>(R.id.buttonOk)  // 요렇게 써도 됨
        butSave.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.setView(view)
        alertDialog.show()
    }
    override fun showPopupMessage() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.alert_popup3, null)
        val editText =view.findViewById<EditText>(R.id.mPopupMessageEditText)
        val alertDialog = AlertDialog.Builder(this)
            .create()
        val cancelButton = view.findViewById<Button>(R.id.mPopupMessageCancelButton)
        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }
        val okButton = view.findViewById<Button>(R.id.mPopupMessagePostButton)
        okButton.setOnClickListener {
            presenter.postMessage(this, editText.text.toString())
            alertDialog.dismiss()
        }
        alertDialog.setView(view)
        alertDialog.show()
    }
    override fun showServerInfo(serverInfo: ServerInfo) {
        binding.mMoreTotalNumberOfQuiz.text=serverInfo.totalNumberOfQuiz.toString()
        binding.mMoreTotalNumberOfUser.text=serverInfo.totalNumberOfUser.toString()
        val topQuizPlayer = serverInfo.topQuizPlayer
        val badQuizPlayer = serverInfo.badQuizPlayer
        val bestQuizPlayer = serverInfo.bestQuizPlayer
        var percentageOfAnswer=0.0

        if (topQuizPlayer!=null) {
            percentageOfAnswer = topQuizPlayer.percentageOfAnswer * 100
            val percentage = (Math.round((percentageOfAnswer * 1000).roundToInt() / 10f) / 100f)
            binding.mMoreTopNickName.text = topQuizPlayer.nickName
            binding.mMoreTopTotal.text = "총 푼 퀴즈 개수\n" + topQuizPlayer.totalQuizCount.toString()
            binding.mMoreTopSolved.text = "맞춘 퀴즈\n" + topQuizPlayer.solvedQuizCount.toString()
            binding.mMoreTopPercentage.text = "퀴즈 맞힐 확률\n$percentage%"

        }
        if (badQuizPlayer!=null)
        {
        percentageOfAnswer = badQuizPlayer.percentageOfAnswer * 100
        val percentage = (Math.round((percentageOfAnswer * 1000).roundToInt() / 10f) / 100f)

        binding.mMoreTrollNickName.text=badQuizPlayer.nickName
        binding.mMoreTrollTotal.text="총 푼 퀴즈 개수\n"+badQuizPlayer.totalQuizCount.toString()
        binding.mMoreTrollSolved.text="맞춘 퀴즈\n"+badQuizPlayer.solvedQuizCount.toString()
        binding.mMoreTrollPercentage.text= "퀴즈 맞힐 확률\n$percentage%"
        }
        if (bestQuizPlayer!=null) {
            percentageOfAnswer = bestQuizPlayer.percentageOfAnswer * 100
            val percentage =
                (Math.round((percentageOfAnswer * 1000).roundToInt() / 10f) / 100f)

            binding.mMoreBestNickName.text = bestQuizPlayer.nickName
            binding.mMoreBestTotal.text = "총 푼 퀴즈 개수\n" + bestQuizPlayer.totalQuizCount.toString()
            binding.mMoreBestSolved.text = "맞춘 퀴즈\n" + bestQuizPlayer.solvedQuizCount.toString()
            binding.mMoreBestPercentage.text = "퀴즈 맞힐 확률\n$percentage%"
        }
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