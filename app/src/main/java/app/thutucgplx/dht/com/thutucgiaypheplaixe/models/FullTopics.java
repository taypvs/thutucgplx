package app.thutucgplx.dht.com.thutucgiaypheplaixe.models;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.CommonUtils;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.Constanst;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.PreferenceUtils;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.jsonhandler.JsonParseMachine;

/**
 * Created by taypham on 02/12/2016.
 */
public class FullTopics {
    private List<Topic> listTopics;

    public FullTopics(Context context, JSONArray jsonArray){
        listTopics = new ArrayList<Topic>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Topic topic = JsonParseMachine.parseTopic(jsonObject);

                // Convert topic to String and save
                Gson gson = new Gson();
                String json = gson.toJson(topic);
                PreferenceUtils.saveString(context, PreferenceUtils.TOPIC_NUMBER + i, json);

                // Write topic to Files
                CommonUtils.saveObjectToFile(context, topic, Constanst.FILE_NAME_JSON_TOPIC_PREFIX + i + Constanst.FILE_NAME_JSON_TOPIC_FORMAT);
                listTopics.add(topic);
            }
        }catch (JSONException e){

        }
    }
}
