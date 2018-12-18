package lib.android.timingbar.com.view.recyclerview.section;

import android.support.annotation.LayoutRes;
import android.view.View;
import lib.android.timingbar.com.view.recyclerview.base.ViewHolder;

/**
 * StatelessSection
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/2/28
 */

public abstract class StatelessSection extends Section {

    /**
     * Create a stateless Section object without header and footer
     *
     * @param itemResourceId layout resource for its items
     * @deprecated Replaced by {@link #StatelessSection(SectionParameters)}
     */
    @Deprecated
    public StatelessSection(@LayoutRes int itemResourceId) {
        this (new SectionParameters.Builder (itemResourceId)
                .build ());
    }

    /**
     * Create a stateless Section object, with a custom header but without footer
     *
     * @param headerResourceId layout resource for its header
     * @param itemResourceId   layout resource for its items
     * @deprecated Replaced by {@link #StatelessSection(SectionParameters)}
     */
    @Deprecated
    public StatelessSection(@LayoutRes int headerResourceId, @LayoutRes int itemResourceId) {
        this (new SectionParameters.Builder (itemResourceId)
                .headerResourceId (headerResourceId)
                .build ());
    }

    /**
     * Create a stateless Section object, with a custom header and a custom footer
     *
     * @param headerResourceId layout resource for its header
     * @param footerResourceId layout resource for its footer
     * @param itemResourceId   layout resource for its items
     * @deprecated Replaced by {@link #StatelessSection(SectionParameters)}
     */
    @Deprecated
    public StatelessSection(@LayoutRes int headerResourceId, @LayoutRes int footerResourceId,
                            @LayoutRes int itemResourceId) {
        this (new SectionParameters.Builder (itemResourceId)
                .headerResourceId (headerResourceId)
                .footerResourceId (footerResourceId)
                .build ());
    }

    /**
     * Create a stateless Section object based on {@link SectionParameters}
     *
     * @param sectionParameters section parameters
     */
    public StatelessSection(SectionParameters sectionParameters) {
        super (sectionParameters);

        if (sectionParameters.loadingResourceId != null) {
            throw new IllegalArgumentException ("Stateless section shouldn't have a loading state resource");
        }

        if (sectionParameters.failedResourceId != null) {
            throw new IllegalArgumentException ("Stateless section shouldn't have a failed state resource");
        }

        if (sectionParameters.emptyResourceId != null) {
            throw new IllegalArgumentException ("Stateless section shouldn't have an empty state resource");
        }
    }

    @Override
    public final void onBindLoadingViewHolder(ViewHolder holder) {
        super.onBindLoadingViewHolder (holder);
    }

    @Override
    public final ViewHolder getLoadingViewHolder(View view) {
        return super.getLoadingViewHolder (view);
    }

    @Override
    public final void onBindFailedViewHolder(ViewHolder holder) {
        super.onBindFailedViewHolder (holder);
    }

    @Override
    public final ViewHolder getFailedViewHolder(View view) {
        return super.getFailedViewHolder (view);
    }

    @Override
    public final void onBindEmptyViewHolder(ViewHolder holder) {
        super.onBindEmptyViewHolder (holder);
    }

    @Override
    public final ViewHolder getEmptyViewHolder(View view) {
        return super.getEmptyViewHolder (view);
    }

    @Override
    public void onBindItemViewHolder(ViewHolder holder, int position) {
        super.onBindItemViewHolder (holder, position);
    }

    @Override
    public ViewHolder getItemViewHolder(View view) {
        return super.getItemViewHolder (view);
    }
}