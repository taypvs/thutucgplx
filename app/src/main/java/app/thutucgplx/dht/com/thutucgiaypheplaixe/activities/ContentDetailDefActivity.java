package app.thutucgplx.dht.com.thutucgiaypheplaixe.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.CommonUtils;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.R;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.adapter.ListContentDefAdapter;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.jsonhandler.JsonParseMachine;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.models.SubTopicObject;

/**
 * Created by taypham on 08/12/2016.
 */
public class ContentDetailDefActivity extends BaseActivity {

    private ListView listContentView;
    private ListContentDefAdapter listContentDefAdapter;
    private SubTopicObject currentSubTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_def);
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

        listContentView = (ListView) findViewById(R.id.listContentText);
        listContentDefAdapter = new ListContentDefAdapter(getBaseContext(), currentSubTopic.content);
        listContentView.setAdapter(listContentDefAdapter);
        listContentDefAdapter.notifyDataSetChanged();

        findViewById(R.id.up_btn).setVisibility(View.VISIBLE);
        findViewById(R.id.up_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listContentView.setSelectionAfterHeaderView();
            }
        });

        AdView mAdView = (AdView) findViewById(R.id.adView);
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

}
