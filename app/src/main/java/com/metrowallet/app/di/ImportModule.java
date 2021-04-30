package com.metrowallet.app.di;

import com.metrowallet.app.interact.ImportWalletInteract;
import com.metrowallet.app.repository.WalletRepositoryType;
import com.metrowallet.app.service.AnalyticsServiceType;
import com.metrowallet.app.service.GasService;
import com.metrowallet.app.service.KeyService;
import com.metrowallet.app.viewmodel.ImportWalletViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
class ImportModule {
    @Provides
    ImportWalletViewModelFactory provideImportWalletViewModelFactory(
            ImportWalletInteract importWalletInteract,
            KeyService keyService,
            GasService gasService,
            AnalyticsServiceType analyticsService) {
        return new ImportWalletViewModelFactory(importWalletInteract, keyService, gasService, analyticsService);
    }

    @Provides
    ImportWalletInteract provideImportWalletInteract(
            WalletRepositoryType walletRepository) {
        return new ImportWalletInteract(walletRepository);
    }
}
