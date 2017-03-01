package app.thutucgplx.dht.com.thutucgiaypheplaixe.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.CommonUtils;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.Constanst;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.R;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.adapter.ListContentSignAdapter;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.jsonhandler.JsonParseMachine;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.models.SubTopicObject;

/**
 * Created by taypham on 06/12/2016.
 */
public class ListSignActivity extends BaseActivity {

    private SubTopicObject currentSubTopic;
    private ListView listSignView;
    private ListContentSignAdapter listContentSignAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_sign);

        init();
    }

    protected void init() {
        setBackBtnOnclick();
        if (getIntent().hasExtra("KEY_CONTENT")) {
            try {
                String json = getIntent().getStringExtra("KEY_CONTENT");
                JSONObject jsonObject = new JSONObject(json);
                Log.d("TayPVS", "TayPVS - subtopic - jsonObject " + jsonObject.toString());
                currentSubTopic = JsonParseMachine.parseSubTopic(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ((TextView) findViewById(R.id.title)).setText(currentSubTopic.title);
        listSignView = (ListView) findViewById(R.id.listContentSign);
        listContentSignAdapter = new ListContentSignAdapter(this, currentSubTopic.content);
        listSignView.setAdapter(listContentSignAdapter);

        findViewById(R.id.up_btn).setVisibility(View.VISIBLE);
        findViewById(R.id.up_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listSignView.setSelectionAfterHeaderView();
            }
        });

        AdView mAdView = (AdView) findViewById(R.id.adView);
        if (CommonUtils.isOnline(getBaseContext())) {
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest;
            if(!Constanst.isRelease)
                adRequest = new AdRequest.Builder()
                        .addTestDevice(CommonUtils.getDeviceId(getBaseContext()))
                        .build();
            else
                adRequest = new AdRequest.Builder()
                        .build();
            mAdView.loadAd(adRequest);
        }
        else{
            mAdView.setVisibility(View.GONE);
        }
    }

    public void showDialogContent(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListSignActivity.this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(title);
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //DO TASK
                arg0.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        //After calling show method, you need to check your condition and
        //enable/ disable buttons of dialog
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false); //BUTTON1 is positive button
    }
}