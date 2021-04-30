package com.metrowallet.app.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.metrowallet.app.interact.CreateTransactionInteract;
import com.metrowallet.app.interact.FindDefaultNetworkInteract;
import com.metrowallet.app.interact.GenericWalletInteract;
import com.metrowallet.app.service.AnalyticsServiceType;
import com.metrowallet.app.service.GasService2;
import com.metrowallet.app.service.KeyService;
import com.metrowallet.app.service.RealmManager;
import com.metrowallet.app.service.TokensService;
import com.metrowallet.app.service.WalletConnectService;

import javax.inject.Inject;

public class WalletConnectViewModelFactory implements ViewModelProvider.Factory {
    private final KeyService keyService;
    private final FindDefaultNetworkInteract findDefaultNetworkInteract;
    private final CreateTransactionInteract createTransactionInteract;
    private final GenericWalletInteract genericWalletInteract;
    private final RealmManager realmManager;
    private final GasService2 gasService;
    private final Context context;
    private final TokensService tokensService;
    private final AnalyticsServiceType analyticsService;

    @Inject
    public WalletConnectViewModelFactory(
            KeyService keyService,
            FindDefaultNetworkInteract findDefaultNetworkInteract,
            CreateTransactionInteract createTransactionInteract,
            GenericWalletInteract genericWalletInteract,
            RealmManager realmManager,
            GasService2 gasService,
            TokensService tokensService,
            AnalyticsServiceType analyticsService,
            Context context) {
        this.keyService = keyService;
        this.findDefaultNetworkInteract = findDefaultNetworkInteract;
        this.createTransactionInteract = createTransactionInteract;
        this.genericWalletInteract = genericWalletInteract;
        this.realmManager = realmManager;
        this.gasService = gasService;
        this.tokensService = tokensService;
        this.analyticsService = analyticsService;
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WalletConnectViewModel(
                keyService,
                findDefaultNetworkInteract,
                createTransactionInteract,
                genericWalletInteract,
                realmManager,
                gasService,
                tokensService,
                analyticsService,
                context);
    }
}
