package com.pyo.quizfrontapp.boardscreen

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.kakao.adfit.ads.AdListener
import com.kakao.adfit.ads.ba.BannerAdView
import com.pyo.quizfrontapp.BuildConfig
import com.pyo.quizfrontapp.R
import com.pyo.quizfrontapp.databinding.ActivityBoardScreenBinding
import com.pyo.quizfrontapp.mainscreen.MainScreenActivity
import com.pyo.quizfrontapp.preference.MySharedPreferences

class BoardScreenActivity:AppCompatActivity(), BoardScreenContract.View {
    private lateinit var presenter: BoardScreenPresenter
    private var mBinding : ActivityBoardScreenBinding?=null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_screen)//설정
        presenter= BoardScreenPresenter(this@BoardScreenActivity)
        presenter.view.showAdFit()

        binding.webView.apply {

            binding.webView.webChromeClient = object : WebChromeClient() {
                override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                    result.cancel()
                    return true
                }
            }
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }

        }
        val userId = MySharedPreferences.getUserId(this)
        binding.mBoardGoMainButton.setOnClickListener {
            presenter.view.goMainScreen()
        }
        val url= BuildConfig.SERVER_BOARD_URL+"$userId"
        binding.webView.loadUrl(url)

    }

    override fun initView() {
        mBinding= ActivityBoardScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun onBackPressed() {
        if (binding.webView.canGoBack())
        {
            binding.webView.goBack()
        }
        else
        {
            finish()
        }
    }
    override fun goMainScreen() {
        var intent = Intent(this@BoardScreenActivity, MainScreenActivity::class.java)
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