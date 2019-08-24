package io.hustler.wallzy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import io.hustler.wallzy.R;
import io.hustler.wallzy.activity.ImagesActivity;
import io.hustler.wallzy.adapters.VerticalImagesAdapter;
import io.hustler.wallzy.constants.Constants;
import io.hustler.wallzy.customviews.VerticalRecyclerView;
import io.hustler.wallzy.model.CategoryImagesDTO;
import io.hustler.wallzy.utils.MessageUtils;

public class CategoriesFragment extends Fragment {
    private static final String TAG = "CategoriesFragment";
    VerticalRecyclerView verticalRv;

    public static CategoriesFragment getInstance() {
        return new CategoriesFragment();

    }

    public CategoriesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        verticalRv = view.findViewById(R.id.verticalRv);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(getActivity().getString(R.string.DB_CAT_NODE));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, dataSnapshot.toString());
                CategoryImagesDTO categoryImagesDTO = new CategoryImagesDTO();
                ArrayList<CategoryImagesDTO.Category> categoryArrayList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CategoryImagesDTO.Category category = new CategoryImagesDTO.Category();
                    category.setName(snapshot.getKey());
                    category.setCoverImage(snapshot.getValue(String.class));
                    categoryArrayList.add(category);
                }
                categoryImagesDTO.setCategoryArrayList(categoryArrayList);
                VerticalImagesAdapter verticalImagesAdapter = new VerticalImagesAdapter(categoryArrayList, getActivity(),
                        new VerticalImagesAdapter.OnChildClickListener() {
                            @Override
                            public void onCLick(CategoryImagesDTO.Category category, ImageView imageView) {
                                Intent intent = new Intent(getActivity(), ImagesActivity.class);
                                intent.putExtra(Constants.INTENT_CAT_NAME, category.getName());
                                intent.putExtra(Constants.INTENT_CAT_IMAGE, category.getCoverImage());
                                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(Objects.requireNonNull(getActivity())
                                        , imageView, getString(R.string.transistion_blur_image));
                                startActivity(intent, optionsCompat.toBundle());
                                MessageUtils.showDismissableSnackBar(Objects.requireNonNull(getActivity()), verticalRv, category.getName());
                            }
                        });
                verticalRv.intiate(verticalImagesAdapter);
                runLayoutAnimation(verticalRv);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "Cancelled");

            }
        });
        return view;
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
