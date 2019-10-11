package io.hustler.wallzy.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.hustler.wallzy.R;
import io.hustler.wallzy.utils.TextUtils;

public class BottomFeaturesAdapter extends RecyclerView.Adapter<BottomFeaturesAdapter.BottomFeatureViewHolder> {
    private Context context;
    private List<String> featureNamesList=new ArrayList<>();
    private TypedArray featureImagesList;
    public OnFeatureClickListener onFeatureClickListener;

    public interface OnFeatureClickListener {
        void onFeatureClick(String featureName);
    }

    public BottomFeaturesAdapter(Context context, List<String> featureNamesList, TypedArray featureImagesList, OnFeatureClickListener onFeatureClickListener) {
        this.context = context;
        this.featureNamesList = featureNamesList;
        this.featureImagesList = featureImagesList;
        this.onFeatureClickListener = onFeatureClickListener;
    }

    @NonNull
    @Override
    public BottomFeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_feature_layout_rv_item, parent, false);
        return new BottomFeatureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BottomFeatureViewHolder holder, int position) {
        holder.signoutTv.setText(featureNamesList.get(position));
        holder.signoutIv.setImageResource(featureImagesList.getResourceId(position, -1));
        holder.root.setOnClickListener(view -> onFeatureClickListener.onFeatureClick(holder.signoutTv.getText().toString()));

    }

    @Override
    public int getItemCount() {
        return null == featureNamesList ? 0 : featureNamesList.size();
    }

    class BottomFeatureViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout root;
        TextView signoutTv;
        ImageView signoutIv;

        BottomFeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            signoutIv = itemView.findViewById(R.id.signOutIV);
            signoutTv = itemView.findViewById(R.id.Signout_textView);
            root = itemView.findViewById(R.id.Signout_layout);
            TextUtils.findText_and_applyTypeface(root, context);

        }
    }
}
