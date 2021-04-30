package com.metrowallet.app.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.metrowallet.app.C;
import com.metrowallet.app.R;
import com.metrowallet.app.entity.StandardFunctionInterface;
import com.metrowallet.app.widget.CopyTextView;
import com.metrowallet.app.widget.FunctionButtonBar;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by JB on 4/12/2020.
 */
public class TransactionSuccessActivity extends BaseActivity implements StandardFunctionInterface
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_success);

        String transactionHash = getIntent().getStringExtra(C.EXTRA_TXHASH);
        CopyTextView hashText = findViewById(R.id.tx_hash);
        hashText.setText(transactionHash);

        FunctionButtonBar functionBar = findViewById(R.id.layoutButtons);
        functionBar.setupFunctions(this, new ArrayList<>(Collections.singletonList(R.string.action_show_tx_details)));
        functionBar.revealButtons();
    }

    @Override
    public void handleClick(String action, int actionId)
    {
        finish();
    }
}
