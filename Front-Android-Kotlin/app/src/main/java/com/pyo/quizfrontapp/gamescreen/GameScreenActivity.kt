package com.pyo.quizfrontapp.gamescreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kakao.adfit.ads.AdListener
import com.kakao.adfit.ads.ba.BannerAdView
import com.pyo.quizfrontapp.BuildConfig
import com.pyo.quizfrontapp.R
import com.pyo.quizfrontapp.data.FrontUserInfo
import com.pyo.quizfrontapp.data.quizInfoDto
import com.pyo.quizfrontapp.databinding.ActivityGameScreenBinding
import com.pyo.quizfrontapp.gamescreen.recycleview.QuizAdapter
import com.pyo.quizfrontapp.mainscreen.MainScreenActivity
import com.pyo.quizfrontapp.network.UserClient
import com.pyo.quizfrontapp.preference.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GameScreenActivity : AppCompatActivity(), GameScreenContract.View {
    private lateinit var presenter: GameScreenPresenter
    private var mBinding: ActivityGameScreenBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)//설정
        presenter = GameScreenPresenter(this@GameScreenActivity)

        //몇개 선태했는지 가져오기
        if (intent.hasExtra("selectedNumberOfQuiz"))
        {
            val intExtra = intent.getIntExtra("selectedNumberOfQuiz", 5)
            presenter.setSelectedNumberOfQuiz(intExtra)
        }
        presenter.view.setAnswerButtonImpl()
        binding.gameScreenStartButton.setOnClickListener {
            val selectedNumberOfQuiz = presenter.getSelectedNumberOfQuiz()
            presenter.getQuizFromServer(selectedNumberOfQuiz.toLong())

        }
        binding.mScoreBackButton.setOnClickListener {
            presenter.view.goMainScreen()
        }
        presenter.view.showAdFit()
    }
    override fun showProgress(isShow:Boolean) {
        binding.gameScreenStartButton.isVisible = isShow
        binding.readyImage.isVisible = isShow
    }



    override fun setAnswerButtonImpl() {

        binding.quizExample1.setOnClickListener {
            presenter.selectAnswer(1)
            presenter.view.getNextQuiz(1)
        }
        binding.quizExample2.setOnClickListener {
            presenter.selectAnswer(2)
            presenter.view.getNextQuiz(2)
        }
        binding.quizExample3.setOnClickListener {
            presenter.selectAnswer(3)
            presenter.view.getNextQuiz(3)

        }
        binding.quizExample4.setOnClickListener {
            presenter.selectAnswer(4)
            presenter.view.getNextQuiz(4)
        }
    }

    override fun getNextQuiz(answer:Int) {
        presenter.nextQuizIndex()
        presenter.view.showQuizIndex()
        val quizIndex = presenter.getQuizIndex()
        presenter.view.showTimer(quizIndex)
        if (quizIndex>=presenter.getSelectedNumberOfQuiz())
        {
            presenter.nextQuizIndex()
            presenter.view.endGameGoScoreScreen()
            return
        }
            viewQuiz(presenter.getQuizFromIndex(quizIndex))
        return

    }

    override fun endGameGoScoreScreen() {
        presenter.nextQuizIndex()
        val userNickName = MySharedPreferences.getUserId(this)
        val selectedNumberOfQuiz = presenter.getSelectedNumberOfQuiz()
        val answerScore = presenter.getAnswerScore()
        val scoreInfo=FrontUserInfo(userNickName,selectedNumberOfQuiz.toLong(),answerScore.toLong(),0.0)
        UserClient.retrofitService.userUpdate(scoreInfo.nickName,scoreInfo.totalQuizCount,scoreInfo.solvedQuizCount).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                return
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                return
            }
        })
        val rv = binding.rvQuiz
        rv.isVisible = true
        rv.layoutManager =
            LinearLayoutManager(this@GameScreenActivity, LinearLayoutManager.VERTICAL, false)
        rv.setHasFixedSize(true)
        rv.adapter = QuizAdapter(presenter.getQuizAll())
        showQuizUI(false)
        binding.quizImage.isVisible = false
        presenter.getSelectedNumberOfQuiz()
        binding.mScoreText.text="총 "+selectedNumberOfQuiz.toString()+"문제 중 "+answerScore.toString()+"개를 맞추셨어요."
        binding.mScoreBackButton.isVisible = true
        binding.mScoreExplainText.isVisible=true
    }

    override fun goMainScreen() {
        var intent = Intent(this@GameScreenActivity, MainScreenActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
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

    override fun initView() {
        mBinding = ActivityGameScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
    override fun onBackPressed() {

        presenter.view.showPopup("정말 퀴즈를 종료하시겠습니까?")

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
            presenter.nextQuizIndex()
            alertDialog.dismiss()
            finish()
        }
        alertDialog.setView(view)
        alertDialog.show()
    }

    override fun showQuizIndex() {
        binding.mGameInfoIndex.text= "${presenter.getQuizIndex()+1} / ${presenter.getSelectedNumberOfQuiz()}"
    }

    override fun showTimer(index:Int) {

        val timer = object: CountDownTimer(15000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                if (index!=presenter.getQuizIndex())
                    cancel()
                binding.mGameInfoTimer.text=(millisUntilFinished/1000).toString()+'.'+(millisUntilFinished%1000).toString()
            }

            override fun onFinish() {
                presenter.view.getNextQuiz(0)
            }
        }
        timer.start()


    }

    override fun showQuizUI(isShow:Boolean) {
        binding.quizExample1.isVisible = isShow
        binding.quizExample2.isVisible = isShow
        binding.quizExample3.isVisible = isShow
        binding.quizExample4.isVisible = isShow
        binding.quizTitle.isVisible=isShow
        binding.quizImage.isVisible=isShow
        binding.mGameInfoIndex.isVisible=isShow
        binding.mGameInfoLayout.isVisible=isShow
        binding.mGameInfoTimer.isVisible=isShow
    }

    override fun viewQuiz(quizInfoDto: quizInfoDto) {
        //디폴트 이미지 일시 그냥 drawable 에 있는거뿌림
        if (quizInfoDto.filePath.contains("3303ccfa"))
            binding.quizImage.setImageResource(R.drawable.no_image)
        else {
            presenter.view.getQuizImage(quizInfoDto)
        }
        binding.quizTitle.text = quizInfoDto.title
        binding.quizExample1.text = quizInfoDto.example1
        binding.quizExample2.text = quizInfoDto.example2
        binding.quizExample3.text = quizInfoDto.example3
        binding.quizExample4.text = quizInfoDto.example4
    }

    override fun getQuizImage(quizInfoDto: quizInfoDto) {
        //glide 이용 이미지 넣기 개꿀인듯
        Glide.with(this@GameScreenActivity).load(quizInfoDto.filePath).diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(350,250)
            .into(binding.quizImage)

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