package com.metrowallet.app.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import com.metrowallet.app.interact.CreateTransactionInteract;
import com.metrowallet.app.interact.ENSInteract;
import com.metrowallet.app.interact.FetchTransactionsInteract;
import com.metrowallet.app.interact.GenericWalletInteract;
import com.metrowallet.app.router.AssetDisplayRouter;
import com.metrowallet.app.router.ConfirmationRouter;
import com.metrowallet.app.router.TransferTicketDetailRouter;
import com.metrowallet.app.service.AssetDefinitionService;
import com.metrowallet.app.service.GasService;
import com.metrowallet.app.service.KeyService;

/**
 * Created by James on 21/02/2018.
 */

public class TransferTicketDetailViewModelFactory implements ViewModelProvider.Factory {

    private GenericWalletInteract genericWalletInteract;
    private KeyService keyService;
    private CreateTransactionInteract createTransactionInteract;
    private TransferTicketDetailRouter transferTicketDetailRouter;
    private FetchTransactionsInteract fetchTransactionsInteract;
    private AssetDisplayRouter assetDisplayRouter;
    private AssetDefinitionService assetDefinitionService;
    private GasService gasService;
    private ConfirmationRouter confirmationRouter;
    private ENSInteract ensInteract;


    public TransferTicketDetailViewModelFactory(GenericWalletInteract genericWalletInteract,
                                                KeyService keyService,
                                                CreateTransactionInteract createTransactionInteract,
                                                TransferTicketDetailRouter transferTicketDetailRouter,
                                                FetchTransactionsInteract fetchTransactionsInteract,
                                                AssetDisplayRouter assetDisplayRouter,
                                                AssetDefinitionService assetDefinitionService,
                                                GasService gasService,
                                                ConfirmationRouter confirmationRouter,
                                                ENSInteract ensInteract) {
        this.genericWalletInteract = genericWalletInteract;
        this.keyService = keyService;
        this.createTransactionInteract = createTransactionInteract;
        this.transferTicketDetailRouter = transferTicketDetailRouter;
        this.fetchTransactionsInteract = fetchTransactionsInteract;
        this.assetDisplayRouter = assetDisplayRouter;
        this.assetDefinitionService = assetDefinitionService;
        this.gasService = gasService;
        this.confirmationRouter = confirmationRouter;
        this.ensInteract = ensInteract;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TransferTicketDetailViewModel(genericWalletInteract, keyService, createTransactionInteract, transferTicketDetailRouter, fetchTransactionsInteract,
                                                     assetDisplayRouter, assetDefinitionService, gasService, confirmationRouter, ensInteract);
    }
}

