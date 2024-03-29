package com.groceryecommerce.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;


import com.bumptech.glide.Glide;
import com.groceryecommerce.R;
import com.groceryecommerce.utils.SessionManager;
import com.groceryecommerce.utils.Utiles;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;

public class FirstActivity extends ActivityManagePermission {
    SessionManager sessionManager;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        image = findViewById(R.id.image_aaa);

        Glide.with(this).load(R.drawable.cart_min).into(image);
        sessionManager = new SessionManager(FirstActivity.this);
        int SPLASH_TIME_OUT = 2000;
        new Handler().postDelayed(() -> {
            if (Utiles.internetChack()) {
                if (sessionManager.getBooleanData(SessionManager.login) || sessionManager.getBooleanData(SessionManager.isopen)) {
                    Intent i = new Intent(FirstActivity.this, HomeActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(FirstActivity.this, InfoActivity.class);
                    startActivity(i);
                }
                finish();
            } else {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(FirstActivity.this);
                builder.setMessage("Please Check Your Internet Connection")
                        .setCancelable(false)
                        .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.e("tem",dialog+""+id);
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        }, SPLASH_TIME_OUT);

    }


}
