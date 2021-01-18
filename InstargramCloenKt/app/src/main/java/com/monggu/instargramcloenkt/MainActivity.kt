package com.monggu.instargramcloenkt

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class MainActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    var googleSignInClient: GoogleSignInClient? = null
    var GOOGLE_LOGIN_CODE = 9001
    var callbackManager : CallbackManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        btn_sign.setOnClickListener {
            signinAndSignup()
        }
        //3
        btn_google.setOnClickListener {
            googleLogin()
            Log.d("로그", "login btn")
        }
        btn_facebook.setOnClickListener {
            facebookLogin()
        }
        //구글 signin code 1
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        printHashKey()
        //페이스북 콜백
        callbackManager = CallbackManager.Factory.create()

    }
    //facebook 해쉬키 생성
    fun printHashKey() {
        try {
            val info = packageManager.getPackageInfo(packageName,PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey: String = String(Base64.encode(md.digest(), 0))
                Log.i("로그", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("로그", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("로그", "printHashKey()", e)
        }
    }
    //페이스북 로그인
    fun facebookLogin() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("public_profile","email"))
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
                override fun onSuccess(result: LoginResult?) {
                    //페이스북 스텝 2
                    handleFacebookAccessToken(result?.accessToken)
                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException?) {

                }

            })
    }
    fun handleFacebookAccessToken(token : AccessToken?){
        val credential = FacebookAuthProvider.getCredential(token?.token!!)
    auth?.signInWithCredential(credential)
        ?.addOnCompleteListener {
            task ->
            if (task.isSuccessful){
                moveMainPage(task.result?.user)
            }else{
                Toast.makeText(this, "login error!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //화면전환 구글로그인 성공값? true//4
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //페이스북 콜백
        callbackManager?.onActivityResult(requestCode, resultCode,data)
        if (requestCode == GOOGLE_LOGIN_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            }catch (e: ApiException){

            }
        }

    }
        /*if (requestCode == GOOGLE_LOGIN_CODE) {
            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                var account = result.signInAccount
                firebaseAuthWithGoogle(account)

            }
        }*/


    //구글 파이어베이스 연동5
    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    //로그인 성공시
                        val user = auth?.currentUser
                    Toast.makeText(this, "login success!", Toast.LENGTH_SHORT).show()
                    Log.d("로그", "successful")
                } else {
                    //로그인 애러 문장
                    Toast.makeText(this, "login error!", Toast.LENGTH_SHORT).show()
                    Log.d("로그", "login error!")
                }
            }
    }
        //구글 로그인 창 팝업?2
        fun googleLogin() {
            var signinIntent = googleSignInClient?.getSignInIntent()
            startActivityForResult(signinIntent, GOOGLE_LOGIN_CODE)
        }
        //회원가입 및 로그인
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
            //로그인
        fun signinEmail() {
            auth?.createUserWithEmailAndPassword(
                text_edit_id.text.toString(),
                text_edit_password.text.toString()
            )?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    moveMainPage(task.result?.user)
                    //로그인 성공시
                } else {
                    //로그인 애러 문장
                }
            }
        }

        fun moveMainPage(user: FirebaseUser?) {
            if (user != null) {
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }
}

