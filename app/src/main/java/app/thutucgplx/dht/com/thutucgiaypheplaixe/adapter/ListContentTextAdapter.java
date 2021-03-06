package app.thutucgplx.dht.com.thutucgiaypheplaixe.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import app.thutucgplx.dht.com.thutucgiaypheplaixe.Common.CommonUtils;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.R;
import app.thutucgplx.dht.com.thutucgiaypheplaixe.models.ContentDetailRule;


/**
 * Created by taypham on 06/12/2016.
 */
public class ListContentTextAdapter extends BaseAdapter {

    public List<ContentDetailRule> contentDetailRules;
    private LayoutInflater mInflater;
    private Context context;

    public ListContentTextAdapter(Context context, List<ContentDetailRule> contentDetailRules){
        this.context = context;
        this.contentDetailRules = contentDetailRules;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return contentDetailRules.size();
    }

    @Override
    public Object getItem(int i) {
        return contentDetailRules.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.adapter_list_content_text, null);
            holder.title = (TextView) view.findViewById(R.id.content_title);
            holder.content = (TextView) view.findViewById(R.id.content_txt);
            holder.contentWv = (WebView) view.findViewById(R.id.content_webview);
            holder.image = (ImageView) view.findViewById(R.id.content_image);
            holder.titleLayout = (RelativeLayout) view.findViewById(R.id.content_title_layout);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if(!contentDetailRules.get(i).title.trim().equals("khong tieu de")) {
            holder.title.setText(contentDetailRules.get(i).title);
            holder.titleLayout.setVisibility(View.VISIBLE);
        }
        else{
            holder.titleLayout.setVisibility(View.GONE);
        }

        if(contentDetailRules.get(i).detail.contains("table")){
            holder.content.setVisibility(View.GONE);
            holder.contentWv.loadDataWithBaseURL(null, contentDetailRules.get(i).detail, "text/html", "utf-8", null);
            holder.contentWv.setVisibility(View.VISIBLE);
            holder.contentWv.getSettings().setJavaScriptEnabled(true);
            holder.contentWv.getSettings().setSupportZoom(true);
            holder.contentWv.getSettings().setBuiltInZoomControls(true);
            holder.contentWv.getSettings().setDisplayZoomControls(false);
            holder.contentWv.getSettings().setLoadWithOverviewMode(true);
            holder.contentWv.requestFocusFromTouch();
            holder.contentWv.setVerticalScrollBarEnabled(true);
            holder.contentWv.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    Log.d("TayPVS" , "TayPVS onPageFinished - " + view.getContentHeight());
                    if(view.getContentHeight() > 0)
                        setHeightView(view, view.getContentHeight() );
                }


            });
        } else {
            holder.content.setVisibility(View.VISIBLE);
            holder.contentWv.setVisibility(View.GONE);
            holder.content.setText(Html.fromHtml(contentDetailRules.get(i).detail));
        }
        Log.d("TayPVS" , "TayPVS Image - " + contentDetailRules.get(i).image);
        if(contentDetailRules.get(i).image!=null&&!contentDetailRules.get(i).image.equals("")) {
            if(CommonUtils.getDrawableResourceByName(context, contentDetailRules.get(i).image.trim().toLowerCase())!=null) {
                holder.image.setVisibility(View.VISIBLE);
                holder.image.setImageDrawable(CommonUtils.getDrawableResourceByName(context, contentDetailRules.get(i).image.trim().toLowerCase()));
            }
        }
        else
            holder.image.setVisibility(View.GONE);
        return view;
    }

    class ViewHolder {
        TextView title;
        TextView content;
        WebView contentWv;
        ImageView image;
        RelativeLayout titleLayout;
    }

    private void setHeightView(View view, int height){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        view.setLayoutParams(params);
    }
}
