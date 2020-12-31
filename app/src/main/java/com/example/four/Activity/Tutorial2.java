package com.example.four.Activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.four.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Tutorial2 extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial2);

        button = findViewById(R.id.btn_next_tutorial1);
        button.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Tutorial2.this, Tutorial3.class);
            startActivity(intent);
        }
    };
}