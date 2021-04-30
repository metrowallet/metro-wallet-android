package com.metrowallet.app.di;

import android.content.Context;

import com.metrowallet.app.interact.FetchWalletsInteract;
import com.metrowallet.app.interact.FindDefaultNetworkInteract;
import com.metrowallet.app.interact.GenericWalletInteract;
import com.metrowallet.app.interact.SetDefaultWalletInteract;
import com.metrowallet.app.repository.EthereumNetworkRepositoryType;
import com.metrowallet.app.repository.WalletRepositoryType;
import com.metrowallet.app.router.HomeRouter;
import com.metrowallet.app.router.ImportWalletRouter;
import com.metrowallet.app.service.AssetDefinitionService;
import com.metrowallet.app.service.KeyService;
import com.metrowallet.app.service.TokensService;
import com.metrowallet.app.viewmodel.WalletsViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
class AccountsManageModule {

	@Provides
    WalletsViewModelFactory provideAccountsManageViewModelFactory(
			SetDefaultWalletInteract setDefaultWalletInteract,
			FetchWalletsInteract fetchWalletsInteract,
			GenericWalletInteract genericWalletInteract,
			ImportWalletRouter importWalletRouter,
			HomeRouter homeRouter,
			FindDefaultNetworkInteract findDefaultNetworkInteract,
			KeyService keyService,
			TokensService tokensService,
			AssetDefinitionService assetDefinitionService,
			Context context)
	{
		return new WalletsViewModelFactory(setDefaultWalletInteract,
				fetchWalletsInteract,
				genericWalletInteract,
				importWalletRouter,
				homeRouter,
				findDefaultNetworkInteract,
				keyService,
				tokensService,
				assetDefinitionService,
				context);
	}

	@Provides
    SetDefaultWalletInteract provideSetDefaultAccountInteract(WalletRepositoryType accountRepository) {
		return new SetDefaultWalletInteract(accountRepository);
	}

	@Provides
    FetchWalletsInteract provideFetchAccountsInteract(WalletRepositoryType accountRepository) {
		return new FetchWalletsInteract(accountRepository);
	}

	@Provides
    GenericWalletInteract provideFindDefaultAccountInteract(WalletRepositoryType accountRepository) {
		return new GenericWalletInteract(accountRepository);
	}

	@Provides
    ImportWalletRouter provideImportAccountRouter() {
		return new ImportWalletRouter();
	}

	@Provides
    HomeRouter provideHomeRouter() {
	    return new HomeRouter();
    }

	@Provides
	FindDefaultNetworkInteract provideFindDefaultNetworkInteract(
			EthereumNetworkRepositoryType networkRepository) {
		return new FindDefaultNetworkInteract(networkRepository);
	}
}
