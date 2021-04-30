package com.metrowallet.app.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.metrowallet.app.C;
import com.metrowallet.app.R;
import com.metrowallet.app.entity.CustomViewSettings;
import com.metrowallet.app.ui.HomeActivity;

public class EmptyTransactionsView extends FrameLayout {

    public EmptyTransactionsView(@NonNull Context context, OnClickListener onClickListener) {
        super(context);

        LayoutInflater.from(getContext())
                .inflate(R.layout.layout_empty_transactions, this, true);

        /*findViewById(R.id.action_buy).setOnClickListener(onClickListener);

        ((TextView)findViewById(R.id.no_transactions_subtext)).setText(context.getString(R.string.no_recent_transactions_subtext,
                                                                                         CustomViewSettings.primaryNetworkName()));

        Button buyButton = findViewById(R.id.action_buy);
        if (CustomViewSettings.primaryNetworkName().equals(C.ETHEREUM_NETWORK_NAME))
        {
            buyButton.setVisibility(VISIBLE);
            buyButton.setOnClickListener(((HomeActivity) context));
            buyButton.setText(context.getString(R.string.action_buy, CustomViewSettings.primaryNetworkName()));
        }
        else
        {
            buyButton.setVisibility(GONE);
        }*/
    }
}
