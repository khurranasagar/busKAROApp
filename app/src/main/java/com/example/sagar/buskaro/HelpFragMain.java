package com.example.sagar.buskaro;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpFragMain extends Fragment implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.help_fragmain, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }
    private void initView(View view){
        ImageView imageView=(ImageView)view.findViewById(R.id.help2);
        imageView.setOnClickListener(this);
        TextView textView=(TextView)view.findViewById(R.id.help3);
        textView.setOnClickListener(this);

        ImageView imageView2=(ImageView)view.findViewById(R.id.help4);
        imageView2.setOnClickListener(this);
        TextView textView2=(TextView)view.findViewById(R.id.help5);
        textView2.setOnClickListener(this);

        ImageView imageView3=(ImageView)view.findViewById(R.id.help6);
        imageView3.setOnClickListener(this);
        TextView textView3=(TextView)view.findViewById(R.id.help7);
        textView3.setOnClickListener(this);

        ImageView imageView4=(ImageView)view.findViewById(R.id.help8);
        imageView4.setOnClickListener(this);
        TextView textView4=(TextView)view.findViewById(R.id.help9);
        textView4.setOnClickListener(this);

        ImageView imageView5=(ImageView)view.findViewById(R.id.help10);
        imageView5.setOnClickListener(this);
        TextView textView5=(TextView)view.findViewById(R.id.help11);
        textView5.setOnClickListener(this);

        ImageView imageView6=(ImageView)view.findViewById(R.id.help12);
        imageView6.setOnClickListener(this);
        TextView textView6=(TextView)view.findViewById(R.id.help13);
        textView6.setOnClickListener(this);

    }

    private void changeFragment(){
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_up,0);
        ft.replace(R.id.main,new HelpFrag1()).addToBackStack(null).commit();
    }

    public void onClick(View view)
    {
        switch (view.getId()){
            case R.id.help2:
                changeFragment();
                break;
            case R.id.help3:
                changeFragment();

                break;
            case R.id.help4:
                changeFragment2();
                break;
            case R.id.help5:
                changeFragment2();
                break;
            case R.id.help7:
                changeFragment3();
                break;
            case R.id.help6:
                changeFragment3();
                break;
            case R.id.help8:
                changeFragment4();
                break;
            case R.id.help9:
                changeFragment4();
                break;
            case R.id.help10:
                changeFragment5();
                break;
            case R.id.help11:
                changeFragment5();
                break;
            case R.id.help12:
                int permissionCheck = ContextCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.CALL_PHONE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                } else {
                    callPhone();
                }
                break;
            case R.id.help13:
                int permissionCheck2 = ContextCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.CALL_PHONE);
                if (permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                } else {
                    callPhone();
                }
        }

    }

    private void changeFragment5() {
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_up,0);
        ft.replace(R.id.main,new HelpFrag5()).addToBackStack(null).commit();
    }

    private void changeFragment4() {
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_up,0);
        ft.replace(R.id.main,new HelpFrag4()).addToBackStack(null).commit();
    }

    private void changeFragment3() {
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_up,0);
        ft.replace(R.id.main,new HelpFrag3()).addToBackStack(null).commit();
    }

    private void changeFragment2() {
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_up,0);
        ft.replace(R.id.main,new HelpFrag2()).addToBackStack(null).commit();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                }
            }
        }
    }
    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:9971795216"));
        if (ActivityCompat.checkSelfPermission(getView().getContext(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        }
    }
}
