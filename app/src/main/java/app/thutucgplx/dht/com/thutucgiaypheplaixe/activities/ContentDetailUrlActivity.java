package app.thutucgplx.dht.com.thutucgiaypheplaixe.activities;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.CommonUtils;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.Constanst;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.R;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.adapter.ListContentUrlAdapter;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.jsonhandler.JsonParseMachine;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.models.SubTopicObject;

/**
 * Created by taypham on 06/12/2016.
 */
public class ContentDetailUrlActivity extends BaseActivity {

    private ListView contentListview;
    private ListContentUrlAdapter listContentTextAdapter;
    private SubTopicObject currentSubTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_url);

        init();
    }

    protected void init(){
        setBackBtnOnclick();
        if (getIntent().hasExtra("KEY_CONTENT")) {
            try {
                String json = getIntent().getStringExtra("KEY_CONTENT");
                JSONObject jsonObject = new JSONObject(json);
                Log.d("TayPVS", "TayPVS - subtopic - jsonObject " + jsonObject.toString());
                currentSubTopic = JsonParseMachine.parseSubTopic(jsonObject);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        ((TextView)findViewById(R.id.title)).setText(currentSubTopic.title);
        contentListview = (ListView)findViewById(R.id.listContentUrl);
        listContentTextAdapter = new ListContentUrlAdapter(getBaseContext(), currentSubTopic.content);
        contentListview.setAdapter(listContentTextAdapter);
        listContentTextAdapter.notifyDataSetChanged();
        contentListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CommonUtils.openWebPage(getBaseContext(), Html.fromHtml(currentSubTopic.content.get(i).detail.trim()).toString());
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

    public void onDestroy() {
        super.onDestroy();
    }
}
