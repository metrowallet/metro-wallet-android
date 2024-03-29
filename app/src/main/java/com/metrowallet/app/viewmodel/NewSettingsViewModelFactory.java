package com.metrowallet.app.viewmodel;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.metrowallet.app.interact.GenericWalletInteract;
import com.metrowallet.app.repository.EthereumNetworkRepositoryType;
import com.metrowallet.app.repository.PreferenceRepositoryType;
import com.metrowallet.app.router.ManageWalletsRouter;
import com.metrowallet.app.router.MyAddressRouter;

public class NewSettingsViewModelFactory implements ViewModelProvider.Factory {
    private final MyAddressRouter myAddressRouter;
    private final GenericWalletInteract genericWalletInteract;
    private final EthereumNetworkRepositoryType ethereumNetworkRepository;
    private final ManageWalletsRouter manageWalletsRouter;
    private final PreferenceRepositoryType preferenceRepository;

    public NewSettingsViewModelFactory(
            GenericWalletInteract genericWalletInteract,
            MyAddressRouter myAddressRouter,
            EthereumNetworkRepositoryType ethereumNetworkRepository,
            ManageWalletsRouter manageWalletsRouter,
            PreferenceRepositoryType preferenceRepository) {
        this.genericWalletInteract = genericWalletInteract;
        this.myAddressRouter = myAddressRouter;
        this.ethereumNetworkRepository = ethereumNetworkRepository;
        this.manageWalletsRouter = manageWalletsRouter;
        this.preferenceRepository = preferenceRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NewSettingsViewModel(
                genericWalletInteract,
                myAddressRouter,
                ethereumNetworkRepository,
                manageWalletsRouter,
                preferenceRepository
        );
    }
}
