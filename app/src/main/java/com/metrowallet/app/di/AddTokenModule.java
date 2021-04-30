package com.metrowallet.app.di;

import com.metrowallet.app.interact.AddTokenInteract;
import com.metrowallet.app.interact.FetchTokensInteract;
import com.metrowallet.app.interact.FetchTransactionsInteract;
import com.metrowallet.app.interact.FindDefaultNetworkInteract;
import com.metrowallet.app.interact.GenericWalletInteract;
import com.metrowallet.app.repository.EthereumNetworkRepositoryType;
import com.metrowallet.app.repository.TokenRepositoryType;
import com.metrowallet.app.repository.TransactionRepositoryType;
import com.metrowallet.app.repository.WalletRepositoryType;
import com.metrowallet.app.service.AssetDefinitionService;
import com.metrowallet.app.service.TokensService;
import com.metrowallet.app.viewmodel.AddTokenViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class AddTokenModule {

    @Provides
    AddTokenViewModelFactory addTokenViewModelFactory(
            AddTokenInteract addTokenInteract,
            GenericWalletInteract genericWalletInteract,
            FetchTokensInteract fetchTokensInteract,
            EthereumNetworkRepositoryType ethereumNetworkRepository,
            FetchTransactionsInteract fetchTransactionsInteract,
            AssetDefinitionService assetDefinitionService,
            TokensService tokensService) {
        return new AddTokenViewModelFactory(
                addTokenInteract, genericWalletInteract, fetchTokensInteract, ethereumNetworkRepository, fetchTransactionsInteract, assetDefinitionService, tokensService);
    }

    @Provides
    FindDefaultNetworkInteract provideFindDefaultNetworkInteract(
            EthereumNetworkRepositoryType networkRepository) {
        return new FindDefaultNetworkInteract(networkRepository);
    }

    @Provides
    AddTokenInteract provideAddTokenInteract(
            TokenRepositoryType tokenRepository) {
        return new AddTokenInteract(tokenRepository);
    }

    @Provides
    GenericWalletInteract provideFindDefaultWalletInteract(WalletRepositoryType walletRepository) {
        return new GenericWalletInteract(walletRepository);
    }

    @Provides
    FetchTransactionsInteract provideFetchTransactionsInteract(TransactionRepositoryType transactionRepository,
                                                               TokenRepositoryType tokenRepositoryType) {
        return new FetchTransactionsInteract(transactionRepository, tokenRepositoryType);
    }

    @Provides
    FetchTokensInteract provideFetchTokensInteract(TokenRepositoryType tokenRepository) {
        return new FetchTokensInteract(tokenRepository);
    }
}
