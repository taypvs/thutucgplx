package app.thutucgplx.dht.com.thutucgiaypheplaixe.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.CommonUtils;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.Constanst;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.PreferenceUtils;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.VersionChecker;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.R;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.apiservices.LoadFullJsonWebservice;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.models.FullTopics;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.network.ResponseCallbackInterface;

/**
 * Created by taypham on 30/11/2016.
 */
public class SplashActivity extends BaseActivity implements ResponseCallbackInterface {

    LoadFullJsonWebservice loadAllws;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
//        new LoadFileJsonTask().execute("");

    }

    protected void init() {
        Log.d("TayPVS", "TayPVS - Splash");
        if (PreferenceUtils.isFirstTimeLaungh(getBaseContext())) {
//            CommonUtils.clearPreferencesTopics(getBaseContext());

            CommonUtils.clearFolderApp();
            if (CommonUtils.isOnline(getBaseContext())) {
                VersionChecker versionChecker = new VersionChecker();
                try {
                    String latestVersion = versionChecker.execute().get();


                    Log.d("TayPVS", "TayPVS - isHaveNewVersion : " + CommonUtils.isHaveNewVersion(latestVersion, CommonUtils.getAppVersion(getBaseContext())));
                    if(CommonUtils.isHaveNewVersion(latestVersion, CommonUtils.getAppVersion(getBaseContext()))){
                        showDialogUpdate();
                    }
                    else {
                        loadAllws = new LoadFullJsonWebservice(this, this);
                        loadAllws.doLoadAPI();
                    }
                }catch(Exception e){
                    loadAllws = new LoadFullJsonWebservice(this, this);
                    loadAllws.doLoadAPI();
                }
            } else {
                if(CommonUtils.isSavedTopics(getBaseContext())){
                    // Execute some code after 2 seconds have passed
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }
                    }, 3000);

                }
                else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new LoadFileJsonTask().execute("");
                        }
                    }, 3000);


                }
            }
        } else {
//          PreferenceUtils.saveFirstTimeLaungh(getBaseContext());
        }
    }

    @Override
    public void onResultSuccess(Object result, String TAG) {
        Log.d("TayPVS", "TayPVS - onResultSuccess : " + result.toString());
        switch (TAG) {
            case Constanst.TAG_API_GET_FULL_INFO:
                ((TextView) findViewById(R.id.test)).setText(result.toString());
                // Save JSON from server to Internal Files
                FullTopics fullTopics = new FullTopics(getBaseContext(), (JSONArray)result);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onResultFail(Object resultFail, String TAG) {
        Log.d("TayPVS", "TayPVS - onResultFail : " + resultFail.toString());
        switch (TAG) {
            case Constanst.TAG_API_GET_FULL_INFO:
                new LoadFileJsonTask().execute("");
                break;
        }
    }

    // Load File From JSON TEST
    private class LoadFileJsonTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // we use the OkHttp library from https://github.com/square/okhttp
            try {
                // Load JSon From Asset
                JSONArray jsonObject = new JSONArray(CommonUtils.loadJSONFromAsset(getBaseContext(), Constanst.FILE_JSON_TEST));
                FullTopics testTopic = new FullTopics(getBaseContext(), jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }

    }

    public void showDialogUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage("Đã có bản cập nhật mới trên Google Play!!!");
        builder.setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //DO TASK
                arg0.dismiss();
                CommonUtils.openAppRating(getBaseContext());
                System.exit(0);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        //After calling show method, you need to check your condition and
        //enable/ disable buttons of dialog
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false); //BUTTON1 is positive button
    }

}


