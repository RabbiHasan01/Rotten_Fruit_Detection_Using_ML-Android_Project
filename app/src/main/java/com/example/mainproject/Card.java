package com.example.mainproject;


import android.content.Context;
import android.graphics.Color;
import androidx.cardview.widget.CardView;

import android.view.ViewGroup;
import android.widget.TextView;


public class Card extends CardView {

    public static Card newInstance(Context c,int pos)
    {
        Card card=new Card(c);
        card.setCardElevation(10);

        TextView nameTxt=new TextView(c);
        nameTxt.setPadding(5,5,5,5);
        nameTxt.setTextSize(20);
        nameTxt.setTextColor(Color.BLACK);


        if(pos==0) {
            card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            nameTxt.setText(R.string.page_1);
            card.addView(nameTxt);

        }
        else if(pos==1) {
            card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            nameTxt.setText(R.string.page_2);
            card.addView(nameTxt);

        }
        else if(pos==2) {
            card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            nameTxt.setText(R.string.page_3);
            card.addView(nameTxt);

        }
        else if(pos==3) {
            card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            nameTxt.setText(R.string.page_4);
            card.addView(nameTxt);
            //System.out.println("Page: "+pos);

        }
        else if(pos==4) {
            card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            nameTxt.setText(R.string.page_5);
            card.addView(nameTxt);
        }

        return card;
    }

    public Card(Context context) {
        super(context);
    }
}
