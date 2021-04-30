package com.metrowallet.app.di;

import com.metrowallet.app.interact.ChangeTokenEnableInteract;
import com.metrowallet.app.repository.TokenRepositoryType;
import com.metrowallet.app.router.AddTokenRouter;
import com.metrowallet.app.service.AssetDefinitionService;
import com.metrowallet.app.service.TokensService;
import com.metrowallet.app.viewmodel.TokenManagementViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
class TokenManagementModule {
    @Provides
    TokenManagementViewModelFactory provideTokenManagementViewModelFactory(
            TokenRepositoryType tokenRepository,
            ChangeTokenEnableInteract changeTokenEnableInteract,
            AddTokenRouter addTokenRouter,
            AssetDefinitionService assetDefinitionService,
            TokensService tokensService)
    {
        return new TokenManagementViewModelFactory(tokenRepository, changeTokenEnableInteract, addTokenRouter, assetDefinitionService, tokensService);
    }

    @Provides
    ChangeTokenEnableInteract provideChangeTokenEnableInteract(TokenRepositoryType tokenRepository) {
        return new ChangeTokenEnableInteract(tokenRepository);
    }

    @Provides
    AddTokenRouter provideAddTokenRouter() {
        return new AddTokenRouter();
    }
}
