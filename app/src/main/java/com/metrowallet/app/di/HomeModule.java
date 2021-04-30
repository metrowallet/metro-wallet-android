package com.metrowallet.app.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import com.metrowallet.app.interact.FetchWalletsInteract;
import com.metrowallet.app.interact.GenericWalletInteract;
import com.metrowallet.app.repository.CurrencyRepository;
import com.metrowallet.app.repository.CurrencyRepositoryType;
import com.metrowallet.app.repository.EthereumNetworkRepositoryType;
import com.metrowallet.app.repository.LocaleRepository;
import com.metrowallet.app.repository.LocaleRepositoryType;
import com.metrowallet.app.repository.PreferenceRepositoryType;
import com.metrowallet.app.repository.WalletRepositoryType;
import com.metrowallet.app.router.AddTokenRouter;
import com.metrowallet.app.router.ImportTokenRouter;
import com.metrowallet.app.router.MyAddressRouter;
import com.metrowallet.app.service.AnalyticsServiceType;
import com.metrowallet.app.service.AssetDefinitionService;
import com.metrowallet.app.service.TickerService;
import com.metrowallet.app.service.TransactionsService;
import com.metrowallet.app.viewmodel.HomeViewModelFactory;

@Module
class HomeModule {
    @Provides
    HomeViewModelFactory provideHomeViewModelFactory(
            PreferenceRepositoryType preferenceRepository,
            LocaleRepositoryType localeRepository,
            ImportTokenRouter importTokenRouter,
            AddTokenRouter addTokenRouter,
            AssetDefinitionService assetDefinitionService,
            GenericWalletInteract genericWalletInteract,
            FetchWalletsInteract fetchWalletsInteract,
            CurrencyRepositoryType currencyRepository,
            EthereumNetworkRepositoryType ethereumNetworkRepository,
            Context context,
            MyAddressRouter myAddressRouter,
            TransactionsService transactionsService,
            TickerService tickerService,
            AnalyticsServiceType analyticsService) {
        return new HomeViewModelFactory(
                preferenceRepository,
                localeRepository,
                importTokenRouter,
                addTokenRouter,
                assetDefinitionService,
                genericWalletInteract,
                fetchWalletsInteract,
                currencyRepository,
                ethereumNetworkRepository,
                context,
                myAddressRouter,
                transactionsService,
                tickerService,
                analyticsService);
    }

    @Provides
    LocaleRepositoryType provideLocaleRepository(PreferenceRepositoryType preferenceRepository) {
        return new LocaleRepository(preferenceRepository);
    }

    @Provides
    AddTokenRouter provideAddTokenRouter() {
        return new AddTokenRouter();
    }

    @Provides
    ImportTokenRouter providesImportTokenRouter() { return new ImportTokenRouter(); }

    @Provides
    GenericWalletInteract provideFindDefaultWalletInteract(WalletRepositoryType walletRepository) {
        return new GenericWalletInteract(walletRepository);
    }

    @Provides
    FetchWalletsInteract provideFetchWalletInteract(WalletRepositoryType walletRepository) {
        return new FetchWalletsInteract(walletRepository);
    }

    @Provides
    CurrencyRepositoryType provideCurrencyRepository(PreferenceRepositoryType preferenceRepository) {
        return new CurrencyRepository(preferenceRepository);
    }

    @Provides
    MyAddressRouter provideMyAddressRouter() {
        return new MyAddressRouter();
    }
}
