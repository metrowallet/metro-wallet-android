package com.metrowallet.app.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import com.metrowallet.app.interact.CreateTransactionInteract;
import com.metrowallet.app.interact.FindDefaultNetworkInteract;
import com.metrowallet.app.interact.GenericWalletInteract;
import com.metrowallet.app.router.GasSettingsRouter;
import com.metrowallet.app.service.GasService;
import com.metrowallet.app.service.KeyService;
import com.metrowallet.app.service.TokensService;

public class ConfirmationViewModelFactory implements ViewModelProvider.Factory {

    private GenericWalletInteract genericWalletInteract;
    private GasService gasService;
    private CreateTransactionInteract createTransactionInteract;
    private GasSettingsRouter gasSettingsRouter;
    private TokensService tokensService;
    private FindDefaultNetworkInteract findDefaultNetworkInteract;
    private KeyService keyService;

    public ConfirmationViewModelFactory(GenericWalletInteract genericWalletInteract,
                                        GasService gasService,
                                        CreateTransactionInteract createTransactionInteract,
                                        GasSettingsRouter gasSettingsRouter,
                                        TokensService tokensService,
                                        FindDefaultNetworkInteract findDefaultNetworkInteract,
                                        KeyService keyService) {
        this.genericWalletInteract = genericWalletInteract;
        this.gasService = gasService;
        this.createTransactionInteract = createTransactionInteract;
        this.gasSettingsRouter = gasSettingsRouter;
        this.tokensService = tokensService;
        this.findDefaultNetworkInteract = findDefaultNetworkInteract;
        this.keyService = keyService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ConfirmationViewModel(
                genericWalletInteract,
                gasService,
                createTransactionInteract,
                gasSettingsRouter,
                tokensService,
                findDefaultNetworkInteract,
                keyService);
    }
}
