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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import io.hustler.wallzy.Executors.AppExecutor;
import io.hustler.wallzy.MVVM.CategoryViewModel;
import io.hustler.wallzy.R;
import io.hustler.wallzy.Room.Domains.CategoryTable;
import io.hustler.wallzy.activity.ImagesActivity;
import io.hustler.wallzy.adapters.VerticalImagesAdapter;
import io.hustler.wallzy.constants.Constants;
import io.hustler.wallzy.customviews.VerticalRecyclerView;
import io.hustler.wallzy.utils.MessageUtils;

public class CategoriesFragment extends Fragment {
    private static final String TAG = "CategoriesFragment";
    private VerticalRecyclerView verticalRv;
    private AppExecutor mAppExecutor;
    private VerticalImagesAdapter verticalImagesAdapter;

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
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAppExecutor = AppExecutor.getInstance();
        CategoryViewModel categoryViewModel = new CategoryViewModel(Objects.requireNonNull(getActivity()).getApplication());
        setDatToRv(new ArrayList<>());

        categoryViewModel.getLiveCategoryData().observe(getActivity(), categoryTables -> {
            Log.d(TAG, "ONCHANGE CALLED");
            mAppExecutor.getMainThreadExecutor().execute(() -> {
                verticalImagesAdapter.setData((ArrayList<CategoryTable>) categoryTables);
                CategoriesFragment.this.runLayoutAnimation(verticalRv);
            });

        });
    }

    private void setDatToRv(ArrayList<CategoryTable> categoryTables) {
        verticalImagesAdapter = new VerticalImagesAdapter(categoryTables,
                CategoriesFragment.this.getActivity(),
                (category, imageView) -> {
                    Intent intent = new Intent(CategoriesFragment.this.getActivity(), ImagesActivity.class);
                    intent.putExtra(Constants.INTENT_CAT_NAME, category.getCollectionname());
                    intent.putExtra(Constants.INTENT_CAT_IMAGE, category.getCoverImage());
                    intent.putExtra(Constants.INTENT_CAT_ID, category.getFirebaseId());
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(Objects.requireNonNull(CategoriesFragment.this.getActivity())
                            , imageView, CategoriesFragment.this.getString(R.string.transistion_blur_image));
                    CategoriesFragment.this.startActivity(intent, optionsCompat.toBundle());
                    MessageUtils.showDismissableSnackBar(Objects.requireNonNull(CategoriesFragment.this.getActivity()), verticalRv, category.getCollectionname());
                });
        verticalRv.intiate(verticalImagesAdapter);
        CategoriesFragment.this.runLayoutAnimation(verticalRv);
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
//        final Observer<List<CategoryTable>> categoryDataObserver = new Observer<List<CategoryTable>>() {
//            @Override
//            public void onChanged(List<CategoryTable> categoryTables) {
//                Log.i(TAG, categoryTables.toString());
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        setDatToRv((ArrayList<CategoryTable>) categoryTables);
//                    }
//                });
//            }
//        };
//        viewModelLiveCategoryData.observe(this, categoryDataObserver);
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference databaseReference = firebaseDatabase.getReference(getActivity().getString(R.string.DB_CAT_NODE));
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.i(TAG, dataSnapshot.toString());
//                CategoryImagesDTO categoryImagesDTO = new CategoryImagesDTO();
//                ArrayList<CategoryImagesDTO.Category> categoryArrayList = new ArrayList<>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    CategoryImagesDTO.Category category = new CategoryImagesDTO.Category();
//                    category.setName(snapshot.getKey());
//                    category.setCoverImage(snapshot.getValue(String.class));
//                    categoryArrayList.add(category);
//                }
//                categoryImagesDTO.setCategoryArrayList(categoryArrayList);
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.i(TAG, "Cancelled");
//
//            }
//        });
