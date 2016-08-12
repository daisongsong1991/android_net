package com.example.daisongsong.myapplication.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import com.example.daisongsong.myapplication.R;

/**
 * Created by daisongsong on 16-8-12.
 */
public class PermissionTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_test);


        findViewById(R.id.mButtonTestPermission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkContactsPermission();
            }
        });
    }

    private void checkContactsPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)){
                Toast.makeText(this, "请授权!", Toast.LENGTH_SHORT).show();
            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},1);
            }

        }else {
            Toast.makeText(this, "已经授权!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if(Manifest.permission.READ_CONTACTS.equals(permission)){
                    int result = grantResults[i];
                    switch (result){
                        case PackageManager.PERMISSION_GRANTED:
                            Toast.makeText(this, "授权!", Toast.LENGTH_SHORT).show();
                            break;
                        case PackageManager.PERMISSION_DENIED:
                            Toast.makeText(this, "拒绝!", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(this, "其他!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        }
    }
}
