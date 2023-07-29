package com.example.mainproject;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class AboutUs extends AppCompatActivity {

    ArrayList<Card> cards=new ArrayList<>();
    Button prevBtn,nextBtn;
    LinearLayout layout;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));
        getSupportActionBar().setTitle("About us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        layout= (LinearLayout) findViewById(R.id.content);
        prevBtn= (Button) findViewById(R.id.prevBtn);
        nextBtn= (Button) findViewById(R.id.nextBtn);
        prevBtn.setEnabled(false);

        for(int i=0;i<4;i++)
        {
            cards.add(Card.newInstance(this,i));
        }

        layout.addView(Card.newInstance(this,i));

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i<cards.size())
                {
                    i+=1;
                    layout.removeAllViews();
                    layout.addView(Card.newInstance(AboutUs.this,i));

                    if(i==cards.size())
                    {
                        nextBtn.setEnabled(false);
                        prevBtn.setEnabled(true);

                    }else
                    {
                        nextBtn.setEnabled(true);
                        prevBtn.setEnabled(true);

                    }

                    //Toast.makeText(AboutUs.this, "page:"+(i+1), Toast.LENGTH_SHORT).show();
                }

            }
        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i>0)
                {
                    i-=1;
                    layout.removeAllViews();
                    layout.addView(Card.newInstance(AboutUs.this,i));

                    if(i==0)
                    {
                        prevBtn.setEnabled(false);
                        nextBtn.setEnabled(true);

                    }else
                    {
                        prevBtn.setEnabled(true);
                        nextBtn.setEnabled(true);

                    }
                    //Toast.makeText(AboutUs.this, "page:"+(i+1), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
