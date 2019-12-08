package io.hustler.wallzy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

import io.hustler.wallzy.R;
import io.hustler.wallzy.activity.ImagesActivity;
import io.hustler.wallzy.adapters.CategoriesAdapter;
import io.hustler.wallzy.constants.WallZyConstants;
import io.hustler.wallzy.model.base.ResponseImageClass;
import io.hustler.wallzy.model.wallzy.response.BaseCategoryClass;
import io.hustler.wallzy.model.wallzy.response.ResGetAllCategories;
import io.hustler.wallzy.networkhandller.RestUtilities;
import io.hustler.wallzy.utils.MessageUtils;
import io.hustler.wallzy.utils.SharedPrefsUtils;

public class CategoriesFragment extends Fragment {
    private static final String TAG = "CategoriesFragment";
    private RecyclerView verticalRv;
    private CategoriesAdapter categoriesAdapter;

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
        new RestUtilities().getCategory(getActivity(), new RestUtilities.OnSuccessListener() {
            @Override
            public void onSuccess(Object onSuccessResponse) {
                ResGetAllCategories resGetAllCategories = new Gson().fromJson(onSuccessResponse.toString(), ResGetAllCategories.class);
                if (resGetAllCategories.isApiSuccess()) {
                    setDatToRv(resGetAllCategories.getCollections());
                }
            }

            @Override
            public void onError(String error) {
                MessageUtils.showDismissableSnackBar(getActivity(), verticalRv, error);
            }
        });
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        AppExecutor mAppExecutor = AppExecutor.getInstance();
//        CategoryViewModel categoryViewModel = new CategoryViewModel(Objects.requireNonNull(getActivity()).getApplication());
//        setDatToRv(new ArrayList<>());
//
//        categoryViewModel.getLiveCategoryData().observe(getActivity(), categoryTables -> {
//            Log.d(TAG, "ONCHANGE CALLED");
//            mAppExecutor.getMainThreadExecutor().execute(() -> {
//
//                ResGetAllCategories resGetAllCategories = new ResGetAllCategories();
//                ArrayList<BaseCategoryClass> baseCategoryClasses = new ArrayList<>();
//                resGetAllCategories.setCollections(baseCategoryClasses);
//                for (CategoryTable categoryTable : categoryTables) {
//                    BaseCategoryClass baseCategoryClass = new BaseCategoryClass();
//                    baseCategoryClass.setId(categoryTable.getId());
//                    baseCategoryClass.setName(categoryTable.getCollectionname());
//                    baseCategoryClass.setCover(categoryTable.getCoverImage());
//                    baseCategoryClasses.add(baseCategoryClass);
//                }
//                setDatToRv(resGetAllCategories.getCollections());
//                CategoriesFragment.this.runLayoutAnimation(verticalRv);
//            });
//
//        });
    }

    private void setDatToRv(ArrayList<BaseCategoryClass> baseCategoryClasses) {
        for (BaseCategoryClass b : baseCategoryClasses) {
            ResponseImageClass responseImageClass = new ResponseImageClass();
            responseImageClass.setUrl(b.getCover());
            responseImageClass.setName(b.getName());
            responseImageClass.setId(b.getId());
        }

//        ImagesAdapter imagesAdapter = new ImagesAdapter(getActivity(), new ImagesAdapter.OnItemClcikListener() {
//            @Override
//            public void onItemClick(int position) {
//                Intent intent = new Intent(CategoriesFragment.this.getActivity(), ImagesActivity.class);
//                intent.putExtra(WallZyConstants.INTENT_CAT_NAME, responseImageClasses.get(position).getName());
//                intent.putExtra(WallZyConstants.INTENT_CAT_IMAGE, responseImageClasses.get(position).getUrl());
//                intent.putExtra(WallZyConstants.INTENT_CAT_ID, responseImageClasses.get(position).getId());
////                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(Objects.requireNonNull(getActivity()), imageView, getActivity().getString(R.string.transistion_blur_image));
//                Objects.requireNonNull(getActivity()).startActivity(intent);
////                MessageUtils.showDismissableSnackBar(Objects.requireNonNull(CategoriesFragment.this.getActivity()), verticalRv, category.getName());
//
//            }
//        }, responseImageClasses);

        categoriesAdapter = new CategoriesAdapter(baseCategoryClasses, getActivity(), (category, imageView) -> {
            Intent intent = new Intent(CategoriesFragment.this.getActivity(), ImagesActivity.class);
            intent.putExtra(WallZyConstants.INTENT_CAT_NAME, category.getName());
            intent.putExtra(WallZyConstants.INTENT_CAT_IMAGE, category.getCover());
            intent.putExtra(WallZyConstants.INTENT_CAT_ID, category.getId());
            intent.putExtra(WallZyConstants.INTENT_USER_ID, new SharedPrefsUtils(Objects.requireNonNull(getContext())).getUserData().getId());
            intent.putExtra(WallZyConstants.INTENT_TOPIC_ID, category.getTopicId());
            intent.putExtra(WallZyConstants.INTENT_TOPIC_NAME, category.getName());
            intent.putExtra(WallZyConstants.INTENT_IS_CAT, true);
//            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(Objects.requireNonNull(getActivity()),
//                    imageView, getActivity().getString(R.string.transistion_blur_image));
            Objects.requireNonNull(getActivity()).startActivity(intent);
            MessageUtils.showDismissableSnackBar(Objects.requireNonNull(CategoriesFragment.this.getActivity()), getView(), category.getName());
        });
        verticalRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        verticalRv.setAdapter(categoriesAdapter);
        runLayoutAnimation(verticalRv);
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim_climb_up);

        recyclerView.setLayoutAnimation(controller);
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}

