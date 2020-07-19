package cs.hku.wallpaper_sdk.model;

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

import cs.hku.wallpaper_sdk.HomepageActivity;
import cs.hku.wallpaper_sdk.R;
import cs.hku.wallpaper_sdk.constant.Var;
import cs.hku.wallpaper_sdk.stl_opengl.GLWallpaperService;
import cs.hku.wallpaper_sdk.stl_opengl.STL_View;
import cs.hku.wallpaper_sdk.stl_opengl.stl.STLObject;
import cs.hku.wallpaper_sdk.stl_opengl.stl.STLRenderer;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ModelAdapter extends BaseAdapter {

    private Context context;
    private List<STLObject> objects = new ArrayList<>();
    private List<String> names = new ArrayList<>();

    public ModelAdapter(Context context, List<STLObject> objects, List<String> names) {
        this.context = context;
        this.objects = objects;
        this.names = names;
    }

    public void setObjects(List<String> urls) {
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public STLObject getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ModelAdapter.ViewHolder vh = null;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.grid_item,null);
            vh = new ModelAdapter.ViewHolder();
            vh.modelView = (STL_View)view.findViewById(R.id.stlItem);
            vh.textView = (TextView) view.findViewById(R.id.img_name);
            view.setTag(vh);
        }else{
            vh = (ViewHolder) view.getTag();
        }

        if(objects.size()>0){
            Log.d(TAG, "getView: " + objects.get(i));
//            Glide.with(context).load(urls.get(i)).centerCrop().into(vh.imageView);
            //Glide.with(context).load(objects.get(i)).into(vh.modelView);
            vh.modelView.getStlRenderer().requestRedraw(objects.get(i));
            vh.textView.setText(names.get(i));
        }
        return view;
    }

    class ViewHolder{
        STL_View modelView;
        TextView textView;
    }
}
