package com.metrowallet.app.di;

import dagger.Module;
import dagger.Provides;
import com.metrowallet.app.interact.FetchWalletsInteract;

import com.metrowallet.app.repository.CurrencyRepository;
import com.metrowallet.app.repository.CurrencyRepositoryType;
import com.metrowallet.app.repository.LocaleRepository;
import com.metrowallet.app.repository.LocaleRepositoryType;
import com.metrowallet.app.repository.PreferenceRepositoryType;
import com.metrowallet.app.repository.WalletRepositoryType;
import com.metrowallet.app.service.AssetDefinitionService;
import com.metrowallet.app.service.KeyService;
import com.metrowallet.app.viewmodel.SplashViewModelFactory;

@Module
public class SplashModule {

    @Provides
    SplashViewModelFactory provideSplashViewModelFactory(FetchWalletsInteract fetchWalletsInteract,
                                                         PreferenceRepositoryType preferenceRepository,
                                                         LocaleRepositoryType localeRepository,
                                                         KeyService keyService,
                                                         AssetDefinitionService assetDefinitionService,
                                                         CurrencyRepositoryType currencyRepository) {
        return new SplashViewModelFactory(
                fetchWalletsInteract,
                preferenceRepository,
                localeRepository,
                keyService,
                assetDefinitionService,
                currencyRepository);
    }

    @Provides
    FetchWalletsInteract provideFetchWalletInteract(WalletRepositoryType walletRepository) {
        return new FetchWalletsInteract(walletRepository);
    }

    @Provides
    LocaleRepositoryType provideLocaleRepositoryType(PreferenceRepositoryType preferenceRepository) {
        return new LocaleRepository(preferenceRepository);
    }

    @Provides
    CurrencyRepositoryType provideCurrencyRepositoryType(PreferenceRepositoryType preferenceRepository) {
        return new CurrencyRepository(preferenceRepository);
    }
}
