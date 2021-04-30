package com.metrowallet.app.di;


import com.metrowallet.app.interact.ChangeTokenEnableInteract;
import com.metrowallet.app.interact.FetchTokensInteract;
import com.metrowallet.app.interact.GenericWalletInteract;
import com.metrowallet.app.repository.TokenRepositoryType;
import com.metrowallet.app.repository.WalletRepositoryType;
import com.metrowallet.app.router.AssetDisplayRouter;
import com.metrowallet.app.router.Erc20DetailRouter;
import com.metrowallet.app.router.MyAddressRouter;
import com.metrowallet.app.service.AssetDefinitionService;
import com.metrowallet.app.service.TokensService;
import com.metrowallet.app.viewmodel.WalletViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class WalletModule {
    @Provides
    WalletViewModelFactory provideWalletViewModelFactory(
            FetchTokensInteract fetchTokensInteract,
            Erc20DetailRouter erc20DetailRouter,
            AssetDisplayRouter assetDisplayRouter,
            GenericWalletInteract genericWalletInteract,
            AssetDefinitionService assetDefinitionService,
            TokensService tokensService,
            ChangeTokenEnableInteract changeTokenEnableInteract,
            MyAddressRouter myAddressRouter) {
        return new WalletViewModelFactory(
                fetchTokensInteract,
                erc20DetailRouter,
                assetDisplayRouter,
                genericWalletInteract,
                assetDefinitionService,
                tokensService,
                changeTokenEnableInteract,
                myAddressRouter);
    }

    @Provides
    FetchTokensInteract provideFetchTokensInteract(TokenRepositoryType tokenRepository) {
        return new FetchTokensInteract(tokenRepository);
    }

    @Provides
    Erc20DetailRouter provideErc20DetailRouterRouter() {
        return new Erc20DetailRouter();
    }

    @Provides
    AssetDisplayRouter provideAssetDisplayRouter() {
        return new AssetDisplayRouter();
    }

    @Provides
    GenericWalletInteract provideGenericWalletInteract(WalletRepositoryType walletRepository) {
        return new GenericWalletInteract(walletRepository);
    }

    @Provides
    ChangeTokenEnableInteract provideChangeTokenEnableInteract(TokenRepositoryType tokenRepository) {
        return new ChangeTokenEnableInteract(tokenRepository);
    }

    @Provides
    MyAddressRouter provideMyAddressRouter() {
        return new MyAddressRouter();
    }
}
