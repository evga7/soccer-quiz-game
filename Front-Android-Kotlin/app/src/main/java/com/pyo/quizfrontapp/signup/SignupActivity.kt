package com.pyo.quizfrontapp.signup

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.pyo.quizfrontapp.R
import com.pyo.quizfrontapp.databinding.ActivitySignUpBinding
import com.pyo.quizfrontapp.mainscreen.MainScreenActivity
import com.pyo.quizfrontapp.network.UserClient
import com.pyo.quizfrontapp.preference.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity(),SignupContract.View {

    private lateinit var presenter: SignupPresenter
    private var mBinding :ActivitySignUpBinding?=null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        presenter= SignupPresenter(this@SignupActivity)
        presenter.updateCheck(this)
        binding.summitButton.setOnClickListener {
            presenter.view.setNickName()
        }
    }

    override fun initView() {
        mBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setNickName() {
        var nickName: String = binding.SignupNicknameText.text.toString()
        val ps : Pattern = Pattern.compile ("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝| ]*")
        if (nickName.isBlank() || nickName.isEmpty()) {
            presenter.view.showPopup("공백닉네임은 만들수 없습니다.")
        } else if (nickName.length>8) {
            presenter.view.showPopup("닉네임은 8자 이하로 만들어주세요")
        }
            else if (nickName.contains(" ")||!ps.matcher(nickName).matches())
        {
                presenter.view.showPopup( "닉네임은 특수문자 및 공백이 불가합니다.")
        }
        else if (nickName.contains("운영자")||nickName.contains("admin"))
        {
            presenter.view.showPopup( "해당 닉네임은 불가능합니다.")
        }
        else
         {
            var nickNameCheck: String = ""
            UserClient.retrofitService.nickNameCheck(nickName).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    nickNameCheck = response.body().toString()
                    if (nickNameCheck == "OK") {
                        UserClient.retrofitService.singUp(nickName)
                            .enqueue(object : Callback<Void> {
                                override fun onResponse(
                                    call: Call<Void>,
                                    response: Response<Void>
                                ) {
                                    if (response.isSuccessful) {
                                        MySharedPreferences.setUserId(this@SignupActivity, nickName)
                                        presenter.view.onLogin()
                                    }
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    presenter.view.showPopup("네트워크 연결 불가")
                                }
                            })
                    } else if (nickNameCheck == "empty") {
                        presenter.view.showPopup("공백닉네임은 만들수 없습니다.")
                    } else if (nickNameCheck == "length") {
                        presenter.view.showPopup("닉네임은 8자 이하로 만들어주세요")
                    } else if (nickNameCheck == "notMatch")
                    {
                        presenter.view.showPopup( "닉네임은 특수문자 및 공백이 불가합니다.")
                    }
                    else if (nickNameCheck=="noNickName")
                    {
                        presenter.view.showPopup( "해당 닉네임은 불가능합니다.")
                    }
                    else {
                        presenter.view.showPopup( "닉네임이 중복 입니다.")
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    presenter.view.showPopup("네트워크 연결 불가")
                }
            }
            )
        }
    }

    override fun showMain() {
        binding.summitButton.isVisible=true
        binding.SignupNicknameText.isVisible=true
        binding.signupBackground.isVisible=true
        binding.signupLayout.setBackgroundColor(Color.WHITE)
    }


    override fun showPopup(msg:String) {
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


    override fun onLogin() {
        var intent = Intent(this@SignupActivity, MainScreenActivity::class.java)
        startActivity(intent)
        finish()
    }
    override fun onDestroy() {
        mBinding=null
        super.onDestroy()
    }
}