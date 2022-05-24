package com.pyo.quizfrontapp.mainscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
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
import com.pyo.quizfrontapp.boardscreen.BoardScreenActivity
import com.pyo.quizfrontapp.databinding.ActivityMainScreenBinding
import com.pyo.quizfrontapp.gamescreen.GameScreenActivity
import com.pyo.quizfrontapp.morescreen.MoreScreenActivity
import com.pyo.quizfrontapp.preference.MySharedPreferences
import com.pyo.quizfrontapp.rankscreen.RankScreenActivity

class MainScreenActivity:AppCompatActivity(), MainScreenContract.View{
    private lateinit var presenter: MainScreenPresenter
    private var mBinding : ActivityMainScreenBinding?=null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)//설정
        presenter= MainScreenPresenter(this@MainScreenActivity)
        binding.gameStartButton.setOnClickListener {
            presenter.view.selectGameProblemsPopup()
        }
        binding.mStartButtonImage.bringToFront()
        //설정된 닉네임 뿌리기
        binding.viewRankButton.setOnClickListener {
            presenter.view.goRank()
        }
        binding.moreViewButton.setOnClickListener {
            presenter.view.goMore()
        }
        binding.mBoardViewButton.setOnClickListener {
            presenter.view.goBoard()
        }
        presenter.view.showAdFit()
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

    override fun goMore() {
        var intent = Intent(this@MainScreenActivity, MoreScreenActivity::class.java)
        startActivity(intent)
    }

    override fun goBoard() {
        var intent = Intent(this@MainScreenActivity, BoardScreenActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        presenter.view.showPopup("정말 종료하시겠습니까?")

    }

    override fun initView(){
        mBinding= ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.nickNameTextView.text=MySharedPreferences.getUserId(this@MainScreenActivity)+"님 반가워요."
    }
    override fun onGame(number:Int) {
        var intent = Intent(this@MainScreenActivity, GameScreenActivity::class.java)
        intent.putExtra("selectedNumberOfQuiz",number)
        startActivity(intent)
        //finish()
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
            finishAffinity()
        }
        alertDialog.setView(view)
        alertDialog.show()
    }
    override fun selectGameProblemsPopup() {

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.alert_select_game_mode, null)

        val alertDialog = AlertDialog.Builder(this)
            .create()
        val fiveButton = view.findViewById<Button>(R.id.button5Problems)
        val tenButton = view.findViewById<Button>(R.id.button10Problems)
        val fifteenButton = view.findViewById<Button>(R.id.button15Problems)
        val twentyButton = view.findViewById<Button>(R.id.button20Problems)
        fiveButton.setOnClickListener {
            alertDialog.dismiss()
            onGame(5)
        }
        tenButton.setOnClickListener {
            alertDialog.dismiss()
            onGame(10)
        }
        fifteenButton.setOnClickListener {
            alertDialog.dismiss()
            onGame(15)
        }
        twentyButton.setOnClickListener {
            alertDialog.dismiss()
            onGame(20)
        }
        val butSave = view.findViewById<Button>(R.id.cancelButton)  // 요렇게 써도 됨
        butSave.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.setView(view)
        alertDialog.show()

    }

    override fun goRank() {
        var intent = Intent(this@MainScreenActivity, RankScreenActivity::class.java)
        startActivity(intent)
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