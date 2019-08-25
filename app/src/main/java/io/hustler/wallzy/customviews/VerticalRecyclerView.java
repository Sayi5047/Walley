package io.hustler.wallzy.customviews;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.hustler.wallzy.R;

public class VerticalRecyclerView extends RecyclerView {
    LayoutManager verticalLayoutManager;

    public VerticalRecyclerView(@NonNull Context context) {
        super(context);
    }

    public VerticalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void intiate(RecyclerView.Adapter adapter) {

        verticalLayoutManager = new GridLayoutManager(getContext(), 2);
        setLayoutManager(verticalLayoutManager);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                scrollToPosition(0);
                onScrollChanged();
                addOnScrollListener(new OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        onScrollChanged();
                    }
                });
            }
        },3000);

        setAdapter(adapter);
    }

    private void onScrollChanged() {
        new Handler().post(() -> {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int childCenterX = (child.getTop() + child.getBottom()) ;
                float scaleValue = getGussianScale(childCenterX);
                child.setScaleY(scaleValue);
                child.setScaleX(scaleValue);
//                translateView(child, scaleValue);

            }
        });
    }

    private void translateView(View child, float scaleValue) {
        ImageView imageView = child.findViewById(R.id.cat_image);
        imageView.setTranslationY(-(float)scaleValue*80f);
    }

    private float getGussianScale(int childCenterX) {
        /*FORMULAE is f(x)= ae ^ ( ((x-b) ^ 2) / 2c ^ 2 )
         * a= scale factor
         * c= spread factor
         * x= childs center
         * b= recyclerviewCenter
         * e= exp val i.e 2.7
         * */

        int recyclerviewCenter = (getWidth() + getHeight()) / 5;
        return (float) (Math.pow
                (Math.E,
                        -Math.pow(childCenterX - recyclerviewCenter, 2)
                                /
                                (2
                                        *
                                        Math.pow(250.0, 2)))
                *
                (float) 0.05+ (float) 1.0);


    }


}
