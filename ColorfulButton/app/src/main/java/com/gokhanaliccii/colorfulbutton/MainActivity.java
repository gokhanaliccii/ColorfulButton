package com.gokhanaliccii.colorfulbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gokhanaliccii.colorfulbutton.view.StackView;

public class MainActivity extends AppCompatActivity {

    StackView stackView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        stackView = (StackView)findViewById(R.id.stack_view);

        stackView.setAdapter(new MainBoardViewAdapter(this));
    }
}
