package com.metrowallet.app.di;

import com.metrowallet.app.viewmodel.HelpViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
class HelpModule {
    @Provides
    HelpViewModelFactory provideMarketplaceViewModelFactory() {
        return new HelpViewModelFactory();
    }
}
