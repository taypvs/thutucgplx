package app.thutucgplx.dht.com.thutucgiaypheplaixe.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.CommonUtils;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.Constanst;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.PreferenceUtils;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.R;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.adapter.ListSubTopicAdapter;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.jsonhandler.JsonParseMachine;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.models.Topic;

/**
 * Created by taypham on 05/12/2016.
 */
public class ListSubTopicActivity extends BaseActivity {

    private ListView listViewSTopic;
    private Topic currentTopic;
    private ListSubTopicAdapter listSubTopicAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_topic);
        init();
    }

    protected void init(){
        setBackBtnOnclick();
        if (getIntent().hasExtra("KEY_TOPIC")) {
            try {
                int i = getIntent().getIntExtra("KEY_TOPIC", 0);
                JSONObject jsonObject = new JSONObject(PreferenceUtils.getString(getBaseContext(), PreferenceUtils.TOPIC_NUMBER + i));
                Log.d("TayPVS", "TayPVS - subtopic - jsonObject " + jsonObject.toString());
                currentTopic = JsonParseMachine.parseTopic(jsonObject);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        ((TextView)findViewById(R.id.title)).setText(currentTopic.name);

        listViewSTopic = (ListView) findViewById(R.id.listSubTopic);
        listSubTopicAdapter = new ListSubTopicAdapter(getBaseContext(), currentTopic.small_topic);
        listViewSTopic.setAdapter(listSubTopicAdapter);
        listViewSTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Convert topic to String and save
                Gson gson = new Gson();
                String json = gson.toJson(currentTopic.small_topic.get(i));
                startContentActivity(currentTopic.small_topic.get(i).type_name, json);
//                overridePendingTransition(R.anim.slide_from_right, 0);
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
}
