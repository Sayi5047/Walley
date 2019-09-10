package io.hustler.wallzy.customviews;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public abstract class StaggeredGridPaginationScrollListener extends RecyclerView.OnScrollListener {

    StaggeredGridLayoutManager gridLayoutManager;

    public StaggeredGridPaginationScrollListener(StaggeredGridLayoutManager linearLayoutManager) {
        this.gridLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        /*CURRENTLY VISIBLE CHILD COUNT ON SCREEN*/
        int visibleItemCount = gridLayoutManager.getChildCount();
        /*TOTAL AVAILABLE ITEM COUNT*/
        int totalIteCount = gridLayoutManager.getItemCount();
        /*CURRENTLY ON THE SCREEN FIRST VISIBLE ITEM POSITION NUMBER*/
        int[] array=new int[10];
        int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPositions(array)[0];

        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalIteCount && firstVisibleItemPosition >= 0) {
                loadMoreItems();
            }
        }


    }

    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();


}
