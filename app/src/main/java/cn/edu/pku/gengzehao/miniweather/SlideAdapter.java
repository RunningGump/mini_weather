package cn.edu.pku.gengzehao.miniweather;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;


    // list of images
    public int[] lst_images = {
            R.drawable.image_1,
            R.drawable.image_2,
            R.drawable.image_3,
            R.drawable.image_4
    };

    // list of titles
    public String[] lst_title = {
            "Title 1",
            "Title 2",
            "Title 3",
            "Title 4"
    };

    // list of description
    public String[] lst_description = {
            "Description 1",
            "Description 2",
            "Description 3",
            "Description 4"
    };

    // list of background colors
    public int[] lst_backgroundcolor = {
            Color.rgb(55, 55, 55),
            Color.rgb(239, 85, 85),
            Color.rgb(110, 49, 89),
            Color.rgb(1, 188, 212)
    };

    public SlideAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return lst_title.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (LinearLayout)o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide,container,false);
        LinearLayout layoutslide = (LinearLayout) view.findViewById(R.id.slideLinearLayout);
        ImageView imageView = (ImageView) view.findViewById(R.id.slideImg);
        TextView txttitle = (TextView) view.findViewById(R.id.txtTitle);
        TextView description = (TextView) view.findViewById(R.id.txtDescription);

        layoutslide.setBackgroundColor(lst_backgroundcolor[position]);
        imageView.setImageResource(lst_images[position]);
        txttitle.setText(lst_title[position]);
        description.setText(lst_description[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);

    }
}
