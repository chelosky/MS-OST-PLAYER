package com.chelosky.msostplayer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.chelosky.msostplayer.R;
import com.chelosky.msostplayer.models.itemMainModel;
import com.chelosky.msostplayer.adapter.itemMainAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    ViewPager viewPager;
    itemMainAdapter adapter;
    List<itemMainModel> modelList;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        modelList = new ArrayList<>();
        modelList.add(new itemMainModel(R.drawable.cover, "Metal Slug: Super Vehicle-001", "Composed by: HIYA!, JIM"));
        modelList.add(new itemMainModel(R.drawable.cover2, "Metal Slug 2", "Composed by: HIYA!"));
        modelList.add(new itemMainModel(R.drawable.coverx, "Metal Slug X", "Composed by: HIYA!, Captain Beroou"));
        modelList.add(new itemMainModel(R.drawable.cover3 , "Metal Slug 3", "Composed by: HIYA!, Captain Beroou, Hori_Hori, Q_JIROU, maitaro"));
        modelList.add(new itemMainModel(R.drawable.cover4 , "Metal Slug 4", "Composed by: Toshikazu Tanaka"));
        modelList.add(new itemMainModel(R.drawable.cover5 , "Metal Slug 5", "Composed by: Toshikazu Tanaka"));
        modelList.add(new itemMainModel(R.drawable.cover6 , "Metal Slug 6", "Composed by: Manabu Namiki, Mitsuhiro Kaneda"));
        modelList.add(new itemMainModel(R.drawable.cover7 , "Metal Slug 7", "Composed by: Toshikazu Tanaka"));
        modelList.add(new itemMainModel(R.drawable.cover3d , "Metal Slug 3D", "Composed by: Mitsuhiro Kaneda"));

        adapter = new itemMainAdapter(this, modelList);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130,0,130,0);

        colors = new Integer[]{
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3)
        };



        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (adapter.getCount() - 1) && position < (colors.length - 1)){
                    viewPager.setBackgroundColor(
                            (Integer)argbEvaluator.evaluate(positionOffset,colors[position],colors[position+1])
                    );
                }else{
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void OpenElementOst(int position){
        if(position == viewPager.getCurrentItem()){
            Intent elementIntent = new Intent(MenuActivity.this, ElementActivity.class);
            startActivity(elementIntent);
            overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
            Toast.makeText(MenuActivity.this, modelList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        }else{
            viewPager.setCurrentItem(position);
        }

    }
}
