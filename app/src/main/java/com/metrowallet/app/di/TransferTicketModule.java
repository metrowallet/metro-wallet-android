package com.metrowallet.app.di;

import com.metrowallet.app.interact.FindDefaultNetworkInteract;
import com.metrowallet.app.interact.GenericWalletInteract;
import com.metrowallet.app.repository.EthereumNetworkRepositoryType;
import com.metrowallet.app.repository.WalletRepositoryType;
import com.metrowallet.app.router.TransferTicketDetailRouter;
import com.metrowallet.app.service.AssetDefinitionService;
import com.metrowallet.app.service.TokensService;
import com.metrowallet.app.viewmodel.TransferTicketViewModelFactory;

import dagger.Module;
import dagger.Provides;

/**
 * Created by James on 16/02/2018.
 */

@Module
public class TransferTicketModule
{
    @Provides
    TransferTicketViewModelFactory transferTicketViewModelFactory(
            TokensService tokensService,
            GenericWalletInteract genericWalletInteract,
            FindDefaultNetworkInteract findDefaultNetworkInteract,
            TransferTicketDetailRouter transferTicketDetailRouter,
            AssetDefinitionService assetDefinitionService) {
        return new TransferTicketViewModelFactory(
                tokensService, genericWalletInteract, findDefaultNetworkInteract, transferTicketDetailRouter, assetDefinitionService);
    }

    @Provides
    FindDefaultNetworkInteract provideFindDefaultNetworkInteract(
            EthereumNetworkRepositoryType networkRepository) {
        return new FindDefaultNetworkInteract(networkRepository);
    }

    @Provides
    GenericWalletInteract provideFindDefaultWalletInteract(WalletRepositoryType walletRepository) {
        return new GenericWalletInteract(walletRepository);
    }

    @Provides
    TransferTicketDetailRouter provideTransferTicketDetailRouter() {
        return new TransferTicketDetailRouter();
    }
}

