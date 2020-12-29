package com.example.four.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.four.R;

public class SplashActivity extends Activity {

    //field
    Animation animation, animation2;
    TextView view;
    ImageView star1, star2;


    //Splash 유지 시간
    private final int SPLASH_DISPLAY_LENGTH = 1000;


    //-------------------------------------------------------
    //------------------onCreate start-----------------------

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        //id 받아오기
        view = findViewById(R.id.mam);
        star1 = findViewById(R.id.star1_splash);
        star2 = findViewById(R.id.star2_splash);

        //animation 시작 위치 및 사용될 layout
        animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.translate);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { //안씀

            }


            //------------------------------------------------------------
            @Override
            public void onAnimationEnd(Animation animation) { //첫 animation 이 끝나고 시작 될 animation

                animation2 = new AlphaAnimation(0.0f, 1.0f); // 괄호 안에 넣어야 하는 숫자는 투명도를 의미
                animation2.setDuration(100); //지속시간을 의미
                animation2.setStartOffset(20); //잠시 대기하는 시간
                animation2.setRepeatMode(Animation.REVERSE); //반복
                animation2.setRepeatCount(10); //반복 횟수
                animation2.setFillAfter(true); // animation끝난 후 위치 그대로 유지
                star1.startAnimation( animation2);
                star2.startAnimation(animation2);

            }

            //------------------------------------------------------------
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animation);


        //------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Splash 끝나고 이동경로
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);


    }
    //------------------------------------------------------------


    //------------------onCreate finish-----------------------
    //--------------------------------------------------------


    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }

}//------------------------------------------------

