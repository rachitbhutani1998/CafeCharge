package com.cafedroid.android.cafecharge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText cardEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardEditText =findViewById(R.id.card_no);
        cardEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String cardText=cardEditText.getText().toString();
                Intent goToMenu=new Intent(MainActivity.this,MenuActivity.class);
                goToMenu.putExtra("card_no",cardText);
                startActivity(goToMenu);
                return true;
            }
        });
    }

    public void workerActivity(View view) {
        startActivity(new Intent(MainActivity.this,WorkerAuthActivity.class));
    }
}
