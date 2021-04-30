package com.metrowallet.app.di;

import com.metrowallet.app.service.AssetDefinitionService;
import com.metrowallet.app.service.TokensService;
import com.metrowallet.app.viewmodel.TokenScriptManagementViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
class TokenScriptManagementModule {
    @Provides
    TokenScriptManagementViewModelFactory tokenScriptManagementViewModelFactory(AssetDefinitionService assetDefinitionService)
    {
        return new TokenScriptManagementViewModelFactory(assetDefinitionService);
    }
}
