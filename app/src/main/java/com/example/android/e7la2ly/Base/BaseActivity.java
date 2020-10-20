package com.example.android.e7la2ly.Base;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;


/**
 * Created by Mohamed Nabil Mohamed (Nobel) on 1/24/2019.
 * byte code SA
 * m.nabil.fci2015@gmail.com
 */
public class BaseActivity  extends AppCompatActivity {

    protected AppCompatActivity activity;
    MaterialDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
    }


    public MaterialDialog showConfirmationMessage(String title, String message, String posResText){

        new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .positiveText(posResText)
                .show();

        return dialog;

    }


    public MaterialDialog showConfirmationMessage(String title, String message, String posResText,
                                                  MaterialDialog.SingleButtonCallback onPosAction){

        new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .positiveText(posResText)
                .onPositive(onPosAction)
                .show();

        return dialog;

    }

    public MaterialDialog showMessage(String title,String message,String posText){

        dialog= new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .positiveText(posText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
        return dialog;
    }


    public MaterialDialog showProgressBar(int message){
        dialog=new MaterialDialog.Builder(this)
                .progress(true,0)
                .content(message)
                .cancelable(false)
                .show();

        return dialog;
    }
    public void hideProgressBar(){
        if(dialog!=null&&dialog.isShowing())
            dialog.dismiss();
    }


}
