package app.thutucgplx.dht.com.thutucgiaypheplaixe.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.CommonUtils;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.Constanst;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.PreferenceUtils;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.R;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.adapter.ListTopicAdapter;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.jsonhandler.JsonParseMachine;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.models.Topic;

public class MainActivity extends BaseActivity{

    private ListTopicAdapter listTopicAdapter;
    private List<Topic> topics;
    private GridView topicGridView;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    protected void init(){
        Log.d("TayPVS","TayPVS - Main");
        addTopics();
        topicGridView = (GridView) findViewById(R.id.mainGridLayout);
        listTopicAdapter = new ListTopicAdapter(this, topics);
        listTopicAdapter.notifyDataSetChanged();
        topicGridView.setAdapter(listTopicAdapter);
        topicGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean isTopicXuphat = false;
                if(topics.get(i).name.toLowerCase().contains("xử phạt"))
                    isTopicXuphat = true;
                startSubActivity(topics.get(i).type_name, isTopicXuphat, i);
            }
        });

        mAdView = (AdView) findViewById(R.id.adView);
        if (CommonUtils.isOnline(getBaseContext())) {
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder()
//                    .addTestDevice(CommonUtils.getDeviceId(getBaseContext()))
                    .build();
            mAdView.loadAd(adRequest);
        }
        else{
            mAdView.setVisibility(View.GONE);
        }

    }

    private void addTopics(){
        topics = new ArrayList<Topic>();
        try {
            for (int i = 0; i < Constanst.NUM_OF_TOPICS; i++) {
                JSONObject jsonObject = new JSONObject(PreferenceUtils.getString(getBaseContext(), PreferenceUtils.TOPIC_NUMBER + i));
                Topic topic = JsonParseMachine.parseTopic(jsonObject);
                topics.add(topic);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

}
