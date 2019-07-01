package com.kernal.plateid.controller;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import com.kernal.plateid.AuthService;
import com.kernal.plateid.PlateAuthParameter;
import com.kernal.plateid.R;


/***
 * @author user
 * 序列号和TF卡授权激活类
 */
public class SNandTFAuth {

    private AuthService.MyBinder authBinder;
    /**Sn序列号*/
    private String Sn;
    private Context context;

    public SNandTFAuth(Context context, String Sn) {
        this.context = context;
        //序列号在此传入。如果是TF卡模式，Sn传null就可以
        this.Sn = Sn;
        Intent authIntent = new Intent(context, AuthService.class);
        context.bindService(authIntent, authConn, Service.BIND_AUTO_CREATE);

    }

    private ServiceConnection authConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            authBinder = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            authBinder = (AuthService.MyBinder) service;
            try {
                PlateAuthParameter pap = new PlateAuthParameter();
                //序列号sn在此传入
                pap.sn = Sn;
                int returnAuthority = authBinder.getAuth(pap);
                if (returnAuthority != 0) {
                    Toast.makeText(context, context.getString(R.string.license_verification_failed) + ":" + returnAuthority, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, R.string.license_verification_success, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(context, R.string.failed_check_failure, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } finally {
                if (authBinder != null) {
                    context.unbindService(authConn);
                }
            }
        }
    };
}
