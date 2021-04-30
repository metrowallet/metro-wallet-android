package com.metrowallet.app.interact;

import android.util.Log;

import com.metrowallet.app.BuildConfig;
import com.metrowallet.app.repository.WalletRepositoryType;
import com.metrowallet.app.entity.Wallet;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ExportWalletInteract {

    private final WalletRepositoryType walletRepository;

    public ExportWalletInteract(WalletRepositoryType walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Single<String> export(Wallet wallet, String keystorePassword, String backupPassword) {
        if (BuildConfig.DEBUG) Log.d("RealmDebug", "export + " + wallet.address);
        return walletRepository
                    .exportWallet(wallet, keystorePassword, backupPassword)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
