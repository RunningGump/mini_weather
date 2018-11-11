package cn.edu.pku.gengzehao.miniweather;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPager viewPager;
    private SlideAdapter myadapter;

    private LinearLayout dotsLayout;
    private ImageView[] dots;

    private Button btnSkip, btnNext;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(new PreferenceManager(this).checkPreference())
        {
            loadHome();
        }

        // 这个判断条件用于将引导页最上面透明
        if(Build.VERSION.SDK_INT>19)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_welcome);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        myadapter = new SlideAdapter(this);
        viewPager.setAdapter(myadapter);
        dotsLayout = (LinearLayout) findViewById(R.id.dotLayout);
        btnSkip = (Button) findViewById(R.id.btnSkip);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnSkip.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        createDots(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                createDots(i);
                if(i==myadapter.getCount()-1)
                {
                    btnNext.setText("Start");
                    btnSkip.setVisibility(View.INVISIBLE);
                }
                else
                {
                    btnNext.setText("Next");
                    btnSkip.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void createDots(int current_position){
        if(dotsLayout != null)
            dotsLayout.removeAllViews();
        dots = new ImageView[myadapter.getCount()];
        for(int i=0; i<myadapter.getCount(); i++)
        {
            dots[i] = new ImageView(this);
            if(i == current_position)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dot));
            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_dot));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            dotsLayout.addView(dots[i], params);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnNext:
                loadNextSlide();
                break;

            case R.id.btnSkip:
                loadHome();
                new PreferenceManager(this).writePreference();
                break;
        }


    }

    private void loadHome()
    {
        startActivity(new Intent(this, MainActivity.class));
        finish(); // 结束当前的活动
    }

    private void loadNextSlide()
    {
        int next_slide = viewPager.getCurrentItem() + 1;

        if(next_slide<myadapter.getCount())
        {
            viewPager.setCurrentItem(next_slide);
        }
        else
        {
            loadHome();
            new PreferenceManager(this).writePreference();
        }
    }
}
