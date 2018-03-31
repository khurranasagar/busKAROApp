package com.example.sagar.buskaro;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Harshit Verma on 17-03-2018.
 */

public class SliderAdapter extends android.support.v4.view.PagerAdapter{

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context)
    {
        this.context=context;
    }

    public int[] slides_images = {R.drawable.aa,R.drawable.busstop2,R.drawable.busstop3,R.drawable.busstop3};

    public String[] slides_heading={ "Plan your bus journey with bus route and timings","Use busKaro credits to track your bus with live ETA's ",
            "Share the status of your bus anonymously to earn busKARO credits","Share the status of your bus anonymously to earn busKARO credits"
    };

    public String[] slides_upper = {"PLAN","BUSKARO","BUSKARLI","BUSKARLI"};

    @Override
    public int getCount() {
        return slides_heading.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == (LinearLayout) o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_slide,container,false);



        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideTextView = view.findViewById((R.id.slide_text));
        TextView slideTextView1 = view.findViewById((R.id.slide_upper));

        Button signInButton = (Button) view.findViewById(R.id.signupbt);
        ImageView im = (ImageView) view.findViewById((R.id.slide_image));

        slideImageView.setImageResource(slides_images[position]);
        slideTextView.setText(slides_heading[position]);
        slideTextView1.setText(slides_upper[position]);
//        bt.setText("");

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("clicked","clicked");
                Intent intent = new Intent(view.getContext(),SignIn.class);
                context.startActivity(intent);
            }
        });

            signInButton.setVisibility(View.GONE);




        container.addView(view);

        return  view;

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
