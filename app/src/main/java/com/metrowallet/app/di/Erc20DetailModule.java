package com.metrowallet.app.di;

import com.metrowallet.app.interact.FetchTransactionsInteract;
import com.metrowallet.app.repository.OnRampRepositoryType;
import com.metrowallet.app.repository.TokenRepositoryType;
import com.metrowallet.app.repository.TransactionRepositoryType;
import com.metrowallet.app.router.MyAddressRouter;
import com.metrowallet.app.service.AssetDefinitionService;
import com.metrowallet.app.service.TokensService;
import com.metrowallet.app.viewmodel.Erc20DetailViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
class Erc20DetailModule {
    @Provides
    Erc20DetailViewModelFactory provideErc20DetailViewModelFactory(MyAddressRouter myAddressRouter,
                                                                   FetchTransactionsInteract fetchTransactionsInteract,
                                                                   AssetDefinitionService assetDefinitionService,
                                                                   TokensService tokensService,
                                                                   OnRampRepositoryType onRampRepository) {
        return new Erc20DetailViewModelFactory(myAddressRouter,
                fetchTransactionsInteract,
                assetDefinitionService,
                tokensService,
                onRampRepository);
    }

    @Provides
    MyAddressRouter provideMyAddressRouter() {
        return new MyAddressRouter();
    }

    @Provides
    FetchTransactionsInteract provideFetchTransactionsInteract(TransactionRepositoryType transactionRepositoryType,
                                                               TokenRepositoryType tokenRepositoryType) {
        return new FetchTransactionsInteract(transactionRepositoryType, tokenRepositoryType);
    }
}
