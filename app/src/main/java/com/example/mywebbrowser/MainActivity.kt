package com.example.mywebbrowser

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    //변수 선언
    lateinit var webView: WebView
    lateinit var urlEditText: EditText
    lateinit var btn_Back: Button
    lateinit var btn_Forward: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ID로 연결
        webView = findViewById(R.id.webView)
        urlEditText = findViewById(R.id.urlEditText)
        btn_Back = findViewById<Button>(R.id.button_Back)
        btn_Forward = findViewById<Button>(R.id.button_Forward)


        //웹뷰 사용하기 위한 준비
        webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
        }

        webView.loadUrl("https://www.google.com")

        registerForContextMenu(webView)

        //입력한  url에 접속하도록 하는 코드
        urlEditText.setOnEditorActionListener { v, actionId, event ->
            //입력한 EditText가 검색 가능한 url이라면
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //webView로 로드
                webView.loadUrl(urlEditText.text.toString())
                true
            } else {
                false
            }
        }
        btn_Back.setOnClickListener{
            if(webView.canGoBack()){
                //뒤로 가기 동작
                webView.goBack()
            }

        }

        btn_Forward.setOnClickListener{
            //뒤로 갈 수 있는지 확인
            if(webView.canGoForward()){
                //뒤로 가기 동작
                webView.goForward()
            }
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.action_refresh->{
                webView.reload()
                return true
            }

            R.id.action_google, R.id.action_home->{
                webView.loadUrl("https://www.google.com")
                return true
            }

            R.id.action_naver->{
                webView.loadUrl("https://www.naver.com")
                return true
            }

            R.id.action_daum->{
                webView.loadUrl("https://www.daum.net")
                return true
            }

            //인텐트 이용하기 - 인텐트는 페이지에 명시되어있음, 개발자 문서 이용해서 찾아봐야함.
            R.id.action_call->{
                //인텐트 이용 - 전화하기
                //인텐트 지정
                val intent=Intent(Intent.ACTION_DIAL)
                //인텐트에 필요한 데이터 지정
                intent.data = Uri.parse("tel:012-3456-7890")
                //인텐트 엑티비티가 존재하는지 확인
                if(intent.resolveActivity(packageManager)!=null){
                    //존재한다면 인텐트 실행
                    startActivity(intent)
                }
                return true
            }
            R.id.action_send_text->{
                //인텐트 이용 - 문자하기
                //인텐트 지정
                val intent=Intent(Intent.ACTION_SENDTO)
                //인텐트에 필요한 데이터 지정
                intent.data = Uri.parse("smsto:" + Uri.encode("012-3456-7890"))
                //인텐트 엑티비티가 존재하는지 확인
                if(intent.resolveActivity(packageManager)!=null){
                    //존재한다면 인텐트 실행
                    startActivity(intent)
                }
                return true
            }
            R.id.action_email->{
                //인텐트 이용 - 이메일 보내기
                //인텐트 지정
                val intent=Intent(Intent.ACTION_SENDTO)
                //인텐트에 필요한 데이터 지정
                intent.data = Uri.parse("mailto:example@example.com")
                //인텐트 엑티비티가 존재하는지 확인
                if(intent.resolveActivity(packageManager)!=null){
                    //존재한다면 인텐트 실행
                    startActivity(intent)
                }
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context, menu)
    }

    //암시적 인텐트 연동
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.action_share->{
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_TEXT, webView.url.toString())
                intent.setType("text/plain")
                val shareIntent = Intent.createChooser(intent, "공유 페이지")
                startActivity(shareIntent)
                return true
            }
            R.id.action_browser->{
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webView.url))
                startActivity(Intent.createChooser(intent, "Browser"))
                return true
            }
        }

        return super.onContextItemSelected(item)
    }

}