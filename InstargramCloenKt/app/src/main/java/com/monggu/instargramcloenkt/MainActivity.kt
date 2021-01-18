package com.monggu.instargramcloenkt

import android.app.Instrumentation
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    var googleSignInClient: GoogleSignInClient? = null
    var GOOGLE_LOGIN_CODE = 9001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        btn_sign.setOnClickListener {
            signinAndSignup()
        }
        btn_google.setOnClickListener {
            googleLogin()
            Log.d("로그","login btn")
        }
        //구글 사인인 빌드
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    //화면전환 구글로그인 성공값?
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_LOGIN_CODE) {
            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result!!.isSuccess) {
                var account = result.signInAccount
                firebaseAuthWithGoogle(account)
                Log.d("로그","$account")
            }
        }
    }

    //구글 파이어베이스 연동
    fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //로그인 성공시
                    Log.d("로그","$task")
                } else {
                    //로그인 애러 문장
                    Log.d("로그","login error!")
                }
            }
    }
        //구글 로그인 창 팝업?
        fun googleLogin() {
            var signinIntent = googleSignInClient?.signInIntent
            startActivityForResult(signinIntent, GOOGLE_LOGIN_CODE)
        }

        fun signinAndSignup() {
            auth?.createUserWithEmailAndPassword(
                text_edit_id.text.toString(),
                text_edit_password.text.toString()
            )?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 아이디 생성
                    moveMainPage(task.result?.user)
                } else if (task.exception?.message.isNullOrEmpty()) {
                    //에러일때 문장
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                } else {
                    // 로그인 성공?
                    signinEmail()
                }
            }
        }

        fun signinEmail() {
            auth?.createUserWithEmailAndPassword(
                text_edit_id.text.toString(),
                text_edit_password.text.toString()
            )?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //로그인 성공시
                } else {
                    //로그인 애러 문장
                }
            }
        }

        fun moveMainPage(user: FirebaseUser?) {
            if (user != null) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
