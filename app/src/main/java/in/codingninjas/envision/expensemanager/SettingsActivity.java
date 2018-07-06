package in.codingninjas.envision.expensemanager;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.security.Permission;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch switchButton = findViewById(R.id.switch_Settings);

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){

                    if( !(ActivityCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) ){

                        String[] permissions = {Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS};
                        ActivityCompat.requestPermissions(SettingsActivity.this,permissions,1011);
                    }
                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1011){

            int smsReadPermission = grantResults[0];
            int smsReceivePermission = grantResults[1];

            if(smsReadPermission == PackageManager.PERMISSION_GRANTED && smsReceivePermission == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this, "Permissions Granted!", Toast.LENGTH_SHORT).show();
            }else{

                Toast.makeText(this, "Grant Permissions", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
