package com.metrowallet.app.router;

import android.content.Context;
import android.content.Intent;

import com.metrowallet.app.C;
import com.metrowallet.app.ui.AddTokenActivity;

public class AddTokenRouter {

    public void open(Context context, String address) {
        Intent intent = new Intent(context, AddTokenActivity.class);
        intent.putExtra(C.EXTRA_CONTRACT_ADDRESS, address);
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.startActivity(intent);
    }
}
