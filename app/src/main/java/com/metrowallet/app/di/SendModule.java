package com.metrowallet.app.di;

import com.metrowallet.app.interact.AddTokenInteract;
import com.metrowallet.app.interact.CreateTransactionInteract;
import com.metrowallet.app.interact.FetchTransactionsInteract;
import com.metrowallet.app.repository.EthereumNetworkRepositoryType;
import com.metrowallet.app.repository.TokenRepositoryType;
import com.metrowallet.app.repository.TransactionRepositoryType;
import com.metrowallet.app.router.MyAddressRouter;
import com.metrowallet.app.service.AnalyticsServiceType;
import com.metrowallet.app.service.AssetDefinitionService;
import com.metrowallet.app.service.GasService2;
import com.metrowallet.app.service.KeyService;
import com.metrowallet.app.service.TokensService;
import com.metrowallet.app.viewmodel.SendViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
class SendModule {

    @Provides
    SendViewModelFactory provideSendViewModelFactory(MyAddressRouter myAddressRouter,
                                                     EthereumNetworkRepositoryType networkRepositoryType,
                                                     TokensService tokensService,
                                                     FetchTransactionsInteract fetchTransactionsInteract,
                                                     AddTokenInteract addTokenInteract,
                                                     CreateTransactionInteract createTransactionInteract,
                                                     GasService2 gasService,
                                                     AssetDefinitionService assetDefinitionService,
                                                     KeyService keyService,
                                                     AnalyticsServiceType analyticsService) {
        return new SendViewModelFactory(myAddressRouter,
                networkRepositoryType,
                tokensService,
                fetchTransactionsInteract,
                addTokenInteract,
                createTransactionInteract,
                gasService,
                assetDefinitionService,
                keyService,
                analyticsService);
    }

    @Provides
    MyAddressRouter provideMyAddressRouter() {
        return new MyAddressRouter();
    }

    @Provides
    AddTokenInteract provideAddTokenInteract(
            TokenRepositoryType tokenRepository) {
        return new AddTokenInteract(tokenRepository);
    }

    @Provides
    FetchTransactionsInteract provideFetchTransactionsInteract(TransactionRepositoryType transactionRepository,
                                                               TokenRepositoryType tokenRepositoryType) {
        return new FetchTransactionsInteract(transactionRepository, tokenRepositoryType);
    }

    @Provides
    CreateTransactionInteract provideCreateTransactionInteract(TransactionRepositoryType transactionRepository)
    {
        return new CreateTransactionInteract(transactionRepository);
    }
}
