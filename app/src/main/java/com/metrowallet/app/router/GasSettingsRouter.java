package com.metrowallet.app.router;


import android.app.Activity;
import android.content.Intent;

import com.metrowallet.app.C;
import com.metrowallet.app.ui.GasSettingsActivity;
import com.metrowallet.app.ui.GasSettingsActivityLegacy;
import com.metrowallet.app.viewmodel.GasSettingsViewModel;
import com.metrowallet.app.entity.GasSettings;

public class GasSettingsRouter {
    public void open(Activity context, GasSettings gasSettings, int chainId) {
        Intent intent = new Intent(context, GasSettingsActivityLegacy.class);
        intent.putExtra(C.EXTRA_GAS_PRICE, gasSettings.gasPrice.toString());
        intent.putExtra(C.EXTRA_GAS_LIMIT, gasSettings.gasLimit.toString());
        intent.putExtra(C.EXTRA_NETWORKID, chainId);
        intent.putExtra(C.EXTRA_STATE, false);
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.startActivityForResult(intent, C.SET_GAS_SETTINGS);
    }

    public void openLimit(Activity context, GasSettings gasSettings, int chainId) {
        Intent intent = new Intent(context, GasSettingsActivityLegacy.class);
        intent.putExtra(C.EXTRA_GAS_PRICE, gasSettings.gasPrice.toString());
        intent.putExtra(C.EXTRA_GAS_LIMIT, gasSettings.gasLimit.toString());
        intent.putExtra(C.EXTRA_NETWORKID, chainId);
        intent.putExtra(C.EXTRA_STATE, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.startActivityForResult(intent, C.SET_GAS_SETTINGS);
    }
}
