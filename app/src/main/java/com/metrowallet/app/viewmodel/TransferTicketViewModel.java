package com.metrowallet.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import androidx.annotation.Nullable;

import com.metrowallet.app.entity.NetworkInfo;
import com.metrowallet.app.entity.Wallet;
import com.metrowallet.app.entity.tokens.Token;
import com.metrowallet.app.interact.FindDefaultNetworkInteract;
import com.metrowallet.app.interact.GenericWalletInteract;
import com.metrowallet.app.router.TransferTicketDetailRouter;
import com.metrowallet.app.service.AssetDefinitionService;
import com.metrowallet.app.service.TokensService;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.metrowallet.app.entity.DisplayState.TRANSFER_TO_ADDRESS;

public class TransferTicketViewModel extends BaseViewModel {
    private static final long CHECK_BALANCE_INTERVAL = 10;
    private final FindDefaultNetworkInteract findDefaultNetworkInteract;
    private final TokensService tokensService;
    private final GenericWalletInteract genericWalletInteract;
    private final TransferTicketDetailRouter transferTicketDetailRouter;
    private final AssetDefinitionService assetDefinitionService;

    private final MutableLiveData<NetworkInfo> defaultNetwork = new MutableLiveData<>();
    private final MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<Token> token = new MutableLiveData<>();

    @Nullable
    private Disposable getBalanceDisposable;

    TransferTicketViewModel(
            TokensService tokensService,
            GenericWalletInteract genericWalletInteract,
            FindDefaultNetworkInteract findDefaultNetworkInteract,
            TransferTicketDetailRouter transferTicketDetailRouter,
            AssetDefinitionService assetDefinitionService) {
        this.tokensService = tokensService;
        this.genericWalletInteract = genericWalletInteract;
        this.findDefaultNetworkInteract = findDefaultNetworkInteract;
        this.transferTicketDetailRouter = transferTicketDetailRouter;
        this.assetDefinitionService = assetDefinitionService;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (getBalanceDisposable != null) {
            getBalanceDisposable.dispose();
        }
    }

    public LiveData<Wallet> defaultWallet() {
        return defaultWallet;
    }
    public LiveData<Token> token() {
        return token;
    }

    public void fetchCurrentTicketBalance() {
        getBalanceDisposable = Observable.interval(CHECK_BALANCE_INTERVAL, CHECK_BALANCE_INTERVAL, TimeUnit.SECONDS)
                .doOnNext(l -> {
                    Token t = tokensService.getToken(token.getValue().tokenInfo.chainId, token.getValue().getAddress());
                    onToken(t);
                }).subscribe(l -> {}, t -> {});
    }

    public void prepare(Token t) {
        token.setValue(t);
        disposable = findDefaultNetworkInteract
                .find()
                .subscribe(this::onDefaultNetwork, this::onError);
    }

    private void onToken(Token t)
    {
        token.postValue(t);
    }

    private void onDefaultNetwork(NetworkInfo networkInfo) {
        defaultNetwork.postValue(networkInfo);
        disposable = genericWalletInteract
                .find()
                .subscribe(this::onDefaultWallet, this::onError);
    }

    private void onDefaultWallet(Wallet wallet) {
        //TODO: switch on 'use' button
        progress.postValue(false);
        defaultWallet.setValue(wallet);
        fetchCurrentTicketBalance();
    }

    public void openSellDialog(Context context, String ticketIDs)
    {
        transferTicketDetailRouter.open(context, token.getValue(), ticketIDs, defaultWallet.getValue());
    }

    public void openTransferDirectDialog(Context context, String tokenId)
    {
        transferTicketDetailRouter.openTransfer(context, token.getValue(), tokenId, defaultWallet.getValue(), TRANSFER_TO_ADDRESS.ordinal());
    }

    public AssetDefinitionService getAssetDefinitionService()
    {
        return assetDefinitionService;
    }
}
