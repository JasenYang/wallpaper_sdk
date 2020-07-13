package cs.hku.wallpaper_sdk.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


import cs.hku.wallpaper_sdk.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private List<String> urls = new ArrayList<>();
    private List<String> names = new ArrayList<>();

    public ImageAdapter(Context context, List<String> urls, List<String> names) {
        this.context = context;
        this.urls = urls;
        this.names = names;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public String getItem(int i) {
        return urls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.d(TAG, "I am here!!!!!");
        ViewHolder vh = null;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.grid_item,null);
            vh = new ViewHolder();
            vh.imageView = (ImageView) view.findViewById(R.id.img);
            vh.textView = (TextView) view.findViewById(R.id.img_name);
            view.setTag(vh);
        }
        vh = (ViewHolder) view.getTag();
        if(urls!=null && urls.size()>0){
            Log.d(TAG, "getView: " + urls.get(i));
//            Glide.with(context).load(urls.get(i)).centerCrop().into(vh.imageView);
            Glide.with(context).load(urls.get(i)).into(vh.imageView);
            vh.textView.setText(names.get(i));
        }
        return view;
    }

    class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}