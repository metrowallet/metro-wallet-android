package com.metrowallet.app.di;

import com.metrowallet.app.interact.CreateTransactionInteract;
import com.metrowallet.app.interact.ENSInteract;
import com.metrowallet.app.interact.FetchTransactionsInteract;
import com.metrowallet.app.interact.GenericWalletInteract;
import com.metrowallet.app.repository.TokenRepositoryType;
import com.metrowallet.app.repository.TransactionRepositoryType;
import com.metrowallet.app.repository.WalletRepositoryType;
import com.metrowallet.app.router.AssetDisplayRouter;
import com.metrowallet.app.router.ConfirmationRouter;
import com.metrowallet.app.router.TransferTicketDetailRouter;
import com.metrowallet.app.service.AssetDefinitionService;
import com.metrowallet.app.service.GasService;
import com.metrowallet.app.service.KeyService;
import com.metrowallet.app.viewmodel.TransferTicketDetailViewModelFactory;

import dagger.Module;
import dagger.Provides;

/**
 * Created by James on 22/02/2018.
 */

@Module
public class TransferTicketDetailModule {

    @Provides
    TransferTicketDetailViewModelFactory transferTicketDetailViewModelFactory(
            GenericWalletInteract genericWalletInteract,
            KeyService keyService,
            CreateTransactionInteract createTransactionInteract,
            TransferTicketDetailRouter transferTicketDetailRouter,
            FetchTransactionsInteract fetchTransactionsInteract,
            AssetDisplayRouter assetDisplayRouter,
            AssetDefinitionService assetDefinitionService,
            GasService gasService,
            ConfirmationRouter confirmationRouter,
            ENSInteract ensInteract) {
        return new TransferTicketDetailViewModelFactory(
                genericWalletInteract, keyService, createTransactionInteract, transferTicketDetailRouter, fetchTransactionsInteract,
                assetDisplayRouter, assetDefinitionService, gasService, confirmationRouter, ensInteract);
    }

    @Provides
    GenericWalletInteract provideFindDefaultWalletInteract(WalletRepositoryType walletRepository) {
        return new GenericWalletInteract(walletRepository);
    }

    @Provides
    TransferTicketDetailRouter provideTransferDetailRouter() {
        return new TransferTicketDetailRouter();
    }

    @Provides
    CreateTransactionInteract provideCreateTransactionInteract(TransactionRepositoryType transactionRepository) {
        return new CreateTransactionInteract(transactionRepository);
    }

    @Provides
    AssetDisplayRouter provideAssetDisplayRouter() {
        return new AssetDisplayRouter();
    }

    @Provides
    ConfirmationRouter provideConfirmationRouter() {
        return new ConfirmationRouter();
    }

    @Provides
    FetchTransactionsInteract provideFetchTransactionsInteract(TransactionRepositoryType transactionRepository,
                                                               TokenRepositoryType tokenRepositoryType) {
        return new FetchTransactionsInteract(transactionRepository, tokenRepositoryType);
    }

    @Provides
    ENSInteract provideENSInteract(TokenRepositoryType tokenRepository) {
        return new ENSInteract(tokenRepository);
    }
}
