package com.mtw.movie_poc_screen.components;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Aspire-V5 on 12/6/2017.
 */

public class SmartVerticalScrollListener extends RecyclerView.OnScrollListener {

    public interface OnSmartVerticalScrollListener {
        void onListEndReached();
    }

    private int visibleItemCount, pastVisibleItems, totalItemCount, lastCompletelyVisibleItem;
    private boolean isListEndReached = false;
    private boolean reachedOnce = false;
    private int previousDy, currentDy;

    private OnSmartVerticalScrollListener mSmartScrollListener;

    public SmartVerticalScrollListener(OnSmartVerticalScrollListener smartScrollListener) {
        this.mSmartScrollListener = smartScrollListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        currentDy = dy;
        if (currentDy > previousDy) {
            //from top to bottom
        } else if (currentDy < previousDy) {
            //from bottom to top
            isListEndReached = false;
        }

        visibleItemCount = recyclerView.getChildCount();

        totalItemCount = recyclerView.getLayoutManager().getItemCount();

        pastVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        lastCompletelyVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

        previousDy = currentDy;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
        super.onScrollStateChanged(recyclerView, scrollState);
        if (scrollState == RecyclerView.SCROLL_STATE_IDLE) {
            if (totalItemCount >= 2) {
                if ((visibleItemCount + pastVisibleItems) >= totalItemCount && !isListEndReached && !reachedOnce) {
                    isListEndReached = true;
                    reachedOnce = true;
                    mSmartScrollListener.onListEndReached();
                } else if ((lastCompletelyVisibleItem == (totalItemCount - 2)) && reachedOnce) {
                    isListEndReached = false;
                    reachedOnce = false;
                }
            } else {
                if ((visibleItemCount + pastVisibleItems) >= totalItemCount && !isListEndReached && !reachedOnce) {
                    isListEndReached = true;
                    reachedOnce = true;
                    mSmartScrollListener.onListEndReached();
                }
            }
        }
    }
}
