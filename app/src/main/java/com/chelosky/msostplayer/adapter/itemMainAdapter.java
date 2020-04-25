package com.chelosky.msostplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chelosky.msostplayer.R;
import com.chelosky.msostplayer.activities.MenuActivity;
import com.chelosky.msostplayer.models.ItemMainModel;

import java.util.List;

public class itemMainAdapter extends PagerAdapter {

    private List<ItemMainModel> listModels;
    private LayoutInflater layoutInflater;
    private Context context;


    public itemMainAdapter(Context context, List<ItemMainModel> listModels) {
        this.listModels = listModels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listModels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_main, container, false);


        CardView cardView;
        ImageView imageView;
        TextView title, desc;

        imageView = (ImageView) view.findViewById(R.id.imgItemMain);
        title = (TextView) view.findViewById(R.id.txtTitle);
        desc = (TextView) view.findViewById(R.id.txtDescription);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        //imageView.setImageResource(listModels.get(position).getImage());
        Glide.with(context)
                .load(listModels.get(position).getImage())
                .placeholder(circularProgressDrawable)
                .skipMemoryCache(true)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
        title.setText(listModels.get(position).getTitle());
        desc.setText(listModels.get(position).getDescription());

        cardView = (CardView) view.findViewById(R.id.cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MenuActivity)context).OpenElementOst(position, listModels.get(position));
            }
        });

        container.addView(view,0);
        return view;
    }



    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
