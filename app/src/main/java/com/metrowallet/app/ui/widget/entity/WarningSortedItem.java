package com.metrowallet.app.ui.widget.entity;

import com.metrowallet.app.ui.widget.holder.WarningHolder;

/**
 * Created by James on 18/07/2019.
 * Stormbird in Sydney
 */
public class WarningSortedItem extends SortedItem<WarningData> {

    public WarningSortedItem(WarningData value, int weight) {
        super(WarningHolder.VIEW_TYPE, value, weight);
    }

    @Override
    public int compare(SortedItem other) {
        return weight - other.weight;
    }

    @Override
    public boolean areContentsTheSame(SortedItem newItem)
    {
        return false;
    }

    @Override
    public boolean areItemsTheSame(SortedItem other)
    {
        return other.viewType == viewType;
    }
}
