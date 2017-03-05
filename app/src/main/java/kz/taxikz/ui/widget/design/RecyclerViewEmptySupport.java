package kz.taxikz.ui.widget.design;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class RecyclerViewEmptySupport extends RecyclerView {
    private AdapterDataObserver mEmptyObserver;
    private View mEmptyView;

    public RecyclerViewEmptySupport(Context context) {
        super(context);
        init();
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs) {
        super(context, attrs);
       init();
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mEmptyObserver = new AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                Adapter<?> adapter = RecyclerViewEmptySupport.this.getAdapter();
                if (adapter != null && RecyclerViewEmptySupport.this.mEmptyView != null) {
                    if (adapter.getItemCount() == 0) {
                        RecyclerViewEmptySupport.this.mEmptyView.setVisibility(VISIBLE);
                        RecyclerViewEmptySupport.this.setVisibility(GONE);
                        return;
                    }
                    RecyclerViewEmptySupport.this.mEmptyView.setVisibility(GONE);
                    RecyclerViewEmptySupport.this.setVisibility(VISIBLE);
                }
            }
        };
    }

    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.mEmptyObserver);
        }
        this.mEmptyObserver.onChanged();
    }

    public void setEmptyView(View mEmptyView) {
        this.mEmptyView = mEmptyView;
    }
}
