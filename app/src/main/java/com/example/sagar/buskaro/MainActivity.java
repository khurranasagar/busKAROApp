package com.example.sagar.buskaro;


import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private SliderAdapter sliderAdapter;
    private LinearLayout mDotLayout;
    private TextView[] mDots;

    private Button nxbt;
    private Button prvbt;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSlideViewPager = findViewById(R.id.slideviewpager);
        mDotLayout = (LinearLayout)findViewById(R.id.dotsLayout);
        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);

        nxbt = (Button) findViewById(R.id.nxtbt);
        prvbt = (Button) findViewById((R.id.prevbt));


        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        nxbt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(currentPage+1);

            }
        });

        prvbt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(currentPage - 1);
            }
        });
    }

    public void addDotsIndicator(int position)
    {
        mDots = new TextView[3];
        mDotLayout.removeAllViews();
        for(int i=0 ; i<mDots.length ;i++)
        {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            mDotLayout.addView(mDots[i]);


        }

        if (mDots.length > 0)
        {
            mDots[position].setTextColor(getResources().getColor(R.color.colorSecondary));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);
            currentPage = i ;
            if (i==0)
            {
                nxbt.setEnabled(true);
                prvbt.setEnabled(false);
                prvbt.setVisibility(View.INVISIBLE);
                nxbt.setText("Next");
                prvbt.setText("");
            }else if(i==mDots.length-1)
            {
                nxbt.setEnabled(true);
                prvbt.setEnabled(true);
                prvbt.setVisibility(View.VISIBLE);
                nxbt.setText("");
                prvbt.setText("Back");
            }
            else
            {
                nxbt.setEnabled(true);
                prvbt.setEnabled(true);
                prvbt.setVisibility(View.VISIBLE);
                nxbt.setText("Next");
                prvbt.setText("Back");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}

