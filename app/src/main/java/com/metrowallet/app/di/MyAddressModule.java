package com.metrowallet.app.di;

import dagger.Module;
import dagger.Provides;
import com.metrowallet.app.interact.FindDefaultNetworkInteract;
import com.metrowallet.app.repository.EthereumNetworkRepositoryType;
import com.metrowallet.app.repository.TokenRepositoryType;
import com.metrowallet.app.service.TokensService;
import com.metrowallet.app.viewmodel.MyAddressViewModelFactory;

@Module
class MyAddressModule {
    @Provides
    MyAddressViewModelFactory provideMyAddressViewModelFactory(
            FindDefaultNetworkInteract findDefaultNetworkInteract,
            EthereumNetworkRepositoryType ethereumNetworkRepository,
            TokensService tokensService) {
        return new MyAddressViewModelFactory(
                findDefaultNetworkInteract,
                ethereumNetworkRepository,
                tokensService);
    }

    @Provides
    FindDefaultNetworkInteract provideFindDefaultNetworkInteract(
            EthereumNetworkRepositoryType ethereumNetworkRepositoryType) {
        return new FindDefaultNetworkInteract(ethereumNetworkRepositoryType);
    }
}
