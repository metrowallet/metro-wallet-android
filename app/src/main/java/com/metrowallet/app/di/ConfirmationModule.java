package com.metrowallet.app.di;


import com.metrowallet.app.interact.CreateTransactionInteract;
import com.metrowallet.app.interact.FindDefaultNetworkInteract;
import com.metrowallet.app.interact.GenericWalletInteract;
import com.metrowallet.app.repository.EthereumNetworkRepositoryType;
import com.metrowallet.app.repository.TransactionRepositoryType;
import com.metrowallet.app.repository.WalletRepositoryType;
import com.metrowallet.app.router.GasSettingsRouter;
import com.metrowallet.app.service.GasService;
import com.metrowallet.app.service.KeyService;
import com.metrowallet.app.service.TokensService;
import com.metrowallet.app.viewmodel.ConfirmationViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class ConfirmationModule {
    @Provides
    public ConfirmationViewModelFactory provideConfirmationViewModelFactory(
            GenericWalletInteract genericWalletInteract,
            GasService gasService,
            CreateTransactionInteract createTransactionInteract,
            GasSettingsRouter gasSettingsRouter,
            TokensService tokensService,
            FindDefaultNetworkInteract findDefaultNetworkInteract,
            KeyService keyService
    ) {
        return new ConfirmationViewModelFactory(
                genericWalletInteract,
                gasService,
                createTransactionInteract,
                gasSettingsRouter,
                tokensService,
                findDefaultNetworkInteract,
                keyService);
    }

    @Provides
    GenericWalletInteract provideFindDefaultWalletInteract(WalletRepositoryType walletRepository) {
        return new GenericWalletInteract(walletRepository);
    }

    @Provides
    CreateTransactionInteract provideCreateTransactionInteract(TransactionRepositoryType transactionRepository) {
        return new CreateTransactionInteract(transactionRepository);
    }

    @Provides
    GasSettingsRouter provideGasSettingsRouter() {
        return new GasSettingsRouter();
    }

    @Provides
    FindDefaultNetworkInteract provideFindDefaultNetworkInteract(
            EthereumNetworkRepositoryType networkRepository) {
        return new FindDefaultNetworkInteract(networkRepository);
    }
}
