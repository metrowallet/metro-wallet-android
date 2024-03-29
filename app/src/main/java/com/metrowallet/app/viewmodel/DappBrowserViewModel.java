package com.metrowallet.app.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.metrowallet.app.C;
import com.metrowallet.app.R;
import com.metrowallet.app.entity.DApp;
import com.metrowallet.app.entity.DAppFunction;
import com.metrowallet.app.entity.NetworkInfo;
import com.metrowallet.app.entity.Operation;
import com.metrowallet.app.entity.QRResult;
import com.metrowallet.app.entity.SendTransactionInterface;
import com.metrowallet.app.entity.SignAuthenticationCallback;
import com.metrowallet.app.entity.Wallet;
import com.metrowallet.app.entity.tokens.Token;
import com.metrowallet.app.interact.CreateTransactionInteract;
import com.metrowallet.app.interact.FindDefaultNetworkInteract;
import com.metrowallet.app.interact.GenericWalletInteract;
import com.metrowallet.app.repository.EthereumNetworkRepositoryType;
import com.metrowallet.app.service.AssetDefinitionService;
import com.metrowallet.app.service.GasService2;
import com.metrowallet.app.service.KeyService;
import com.metrowallet.app.service.TokensService;
import com.metrowallet.app.ui.AddEditDappActivity;
import com.metrowallet.app.ui.HomeActivity;
import com.metrowallet.app.ui.ImportTokenActivity;
import com.metrowallet.app.ui.MyAddressActivity;
import com.metrowallet.app.ui.SendActivity;
import com.metrowallet.app.ui.WalletConnectActivity;
import com.metrowallet.app.ui.zxing.QRScanningActivity;
import com.metrowallet.app.util.DappBrowserUtils;
import com.metrowallet.app.web3.entity.Web3Transaction;
import com.metrowallet.token.entity.Signable;

import org.web3j.protocol.core.methods.response.EthEstimateGas;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

import static com.metrowallet.app.C.Key.WALLET;

public class DappBrowserViewModel extends BaseViewModel  {
    private final MutableLiveData<NetworkInfo> defaultNetwork = new MutableLiveData<>();
    private final MutableLiveData<Wallet> defaultWallet = new MutableLiveData<>();

    private static final int BALANCE_CHECK_INTERVAL_SECONDS = 20;

    private final FindDefaultNetworkInteract findDefaultNetworkInteract;
    private final GenericWalletInteract genericWalletInteract;
    private final AssetDefinitionService assetDefinitionService;
    private final CreateTransactionInteract createTransactionInteract;
    private final TokensService tokensService;
    private final EthereumNetworkRepositoryType ethereumNetworkRepository;
    private final KeyService keyService;
    private final GasService2 gasService;

    @Nullable
    private Disposable balanceTimerDisposable;

    DappBrowserViewModel(
            FindDefaultNetworkInteract findDefaultNetworkInteract,
            GenericWalletInteract genericWalletInteract,
            AssetDefinitionService assetDefinitionService,
            CreateTransactionInteract createTransactionInteract,
            TokensService tokensService,
            EthereumNetworkRepositoryType ethereumNetworkRepository,
            KeyService keyService,
            GasService2 gasService) {
        this.findDefaultNetworkInteract = findDefaultNetworkInteract;
        this.genericWalletInteract = genericWalletInteract;
        this.assetDefinitionService = assetDefinitionService;
        this.createTransactionInteract = createTransactionInteract;
        this.tokensService = tokensService;
        this.ethereumNetworkRepository = ethereumNetworkRepository;
        this.keyService = keyService;
        this.gasService = gasService;
    }

    public AssetDefinitionService getAssetDefinitionService() {
        return assetDefinitionService;
    }

    public LiveData<NetworkInfo> defaultNetwork() {
        return defaultNetwork;
    }

    public LiveData<Wallet> defaultWallet() {
        return defaultWallet;
    }

    public String getNetworkName()
    {
        if (defaultNetwork.getValue().chainId == 1)
        {
            return "";
        }
        else
        {
            return defaultNetwork.getValue().name;
        }
    }

    public void prepare(Context context) {

        disposable = findDefaultNetworkInteract
                .find()
                .subscribe(this::onDefaultNetwork, this::onError);
    }

    private void onDefaultNetwork(NetworkInfo networkInfo)
    {
        //are we allowed this network?
        if (!tokensService.getNetworkFilters().contains(networkInfo.chainId))
        {
            //not allowed it. Switch to first entry in filter list
            networkInfo = ethereumNetworkRepository.getNetworkByChain(tokensService.getNetworkFilters().get(0));
        }

        defaultNetwork.postValue(networkInfo);
        disposable = genericWalletInteract
                .find()
                .subscribe(this::onDefaultWallet, this::onError);
    }

    private void onDefaultWallet(final Wallet wallet) {
        defaultWallet.setValue(wallet);
        //get the chain balance
        startBalanceUpdate();
    }

    private void checkBalance(final Wallet wallet)
    {
        if (defaultNetwork.getValue() != null && wallet != null)
        {
            disposable = tokensService.getChainBalance(wallet.address.toLowerCase(), defaultNetwork.getValue().chainId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(w -> { }, e -> { });
        }
    }

    public Observable<Wallet> getWallet() {
        if (defaultWallet().getValue() != null) {
            return Observable.fromCallable(() -> defaultWallet().getValue());
        } else
            return findDefaultNetworkInteract.find()
                    .flatMap(networkInfo -> genericWalletInteract
                            .find()).toObservable();
    }

    public void signMessage(Signable message, DAppFunction dAppFunction) {
        disposable = createTransactionInteract.sign(defaultWallet.getValue(), message,
                defaultNetwork.getValue().chainId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sig -> dAppFunction.DAppReturn(sig.signature, message),
                        error -> dAppFunction.DAppError(error, message));
    }

    public void setLastUrl(Context context, String url) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(C.DAPP_LASTURL_KEY, url).apply();
    }

    public void addToMyDapps(Context context, String title, String url) {
        Intent intent = new Intent(context, AddEditDappActivity.class);
        DApp dapp = new DApp(title, url);
        intent.putExtra(AddEditDappActivity.KEY_DAPP, dapp);
        intent.putExtra(AddEditDappActivity.KEY_MODE, AddEditDappActivity.MODE_ADD);
        context.startActivity(intent);
    }

    public void share(Context context, String url) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, url);
            intent.setType("text/plain");
            context.startActivity(intent);
    }

    public void onClearBrowserCacheClicked(Context context) {
        WebView webView = new WebView(context);
        webView.clearCache(true);
        Toast.makeText(context, context.getString(R.string.toast_browser_cache_cleared),
                Toast.LENGTH_SHORT).show();
    }

    public void startScan(Activity activity) {
        Intent intent = new Intent(activity, QRScanningActivity.class);
        activity.startActivityForResult(intent, HomeActivity.DAPP_BARCODE_READER_REQUEST_CODE);
    }

    public List<DApp> getDappsMasterList(Context context) {
        return DappBrowserUtils.getDappsList(context);
    }

    public int getActiveFilterCount() {
        return ethereumNetworkRepository.getFilterNetworkList().size();
    }

    public void setNetwork(int chainId)
    {
        NetworkInfo info = ethereumNetworkRepository.getNetworkByChain(chainId);
        if (info != null)
        {
            ethereumNetworkRepository.setDefaultNetworkInfo(info);
            onDefaultNetwork(info);
            gasService.startGasPriceCycle(chainId);
            defaultNetwork.postValue(info);
        }
    }

    public void showImportLink(Context ctx, String qrCode)
    {
        Intent intent = new Intent(ctx, ImportTokenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(C.IMPORT_STRING, qrCode);
        ctx.startActivity(intent);
    }

    public void getAuthorisation(Wallet wallet, Activity activity, SignAuthenticationCallback callback)
    {
        keyService.getAuthenticationForSignature(wallet, activity, callback);
    }

    public void resetSignDialog()
    {
        keyService.resetSigningDialog();
    }

    public void completeAuthentication(Operation signData)
    {
        keyService.completeAuthentication(signData);
    }

    public void failedAuthentication(Operation signData)
    {
        keyService.failedAuthentication(signData);
    }

    public void showSend(Context ctx, QRResult result)
    {
        Intent intent = new Intent(ctx, SendActivity.class);
        boolean sendingTokens = (result.getFunction() != null && result.getFunction().length() > 0);
        String address = defaultWallet.getValue().address;
        int decimals = 18;

        intent.putExtra(C.EXTRA_SENDING_TOKENS, sendingTokens);
        intent.putExtra(C.EXTRA_CONTRACT_ADDRESS, address);
        intent.putExtra(C.EXTRA_SYMBOL, ethereumNetworkRepository.getNetworkByChain(result.chainId).symbol);
        intent.putExtra(C.EXTRA_DECIMALS, decimals);
        intent.putExtra(C.Key.WALLET, defaultWallet.getValue());
        intent.putExtra(C.EXTRA_TOKEN_ID, (Token)null);
        intent.putExtra(C.EXTRA_AMOUNT, result);
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        ctx.startActivity(intent);
    }

    public void sendTransaction(final Web3Transaction finalTx, int chainId, SendTransactionInterface callback)
    {
        if (finalTx.isConstructor())
        {
            disposable = createTransactionInteract
                    .createWithSig(defaultWallet.getValue(), finalTx.gasPrice, finalTx.gasLimit, finalTx.payload, chainId)
                    .subscribe(txData -> callback.transactionSuccess(finalTx, txData.txHash),
                            error -> callback.transactionError(finalTx.leafPosition, error));
        }
        else
        {
            disposable = createTransactionInteract
                    .createWithSig(defaultWallet.getValue(), finalTx, chainId)
                    .subscribe(txData -> callback.transactionSuccess(finalTx, txData.txHash),
                            error -> callback.transactionError(finalTx.leafPosition, error));
        }
    }

    public void showMyAddress(Context ctx)
    {
        Intent intent = new Intent(ctx, MyAddressActivity.class);
        intent.putExtra(WALLET, defaultWallet.getValue());
        ctx.startActivity(intent);
    }

    public void onDestroy()
    {
        if (balanceTimerDisposable != null && !balanceTimerDisposable.isDisposed()) balanceTimerDisposable.dispose();
    }

    public void updateGasPrice(int chainId)
    {
        gasService.startGasPriceCycle(chainId);
    }

    public Realm getRealmInstance(Wallet wallet)
    {
        return tokensService.getRealmInstance(wallet);
    }

    public void startBalanceUpdate()
    {
        if (balanceTimerDisposable == null || balanceTimerDisposable.isDisposed())
        {
            balanceTimerDisposable = Observable.interval(0, BALANCE_CHECK_INTERVAL_SECONDS, TimeUnit.SECONDS)
                    .doOnNext(l -> checkBalance(defaultWallet.getValue())).subscribe();
        }
    }

    public void stopBalanceUpdate()
    {
        if (balanceTimerDisposable != null && !balanceTimerDisposable.isDisposed()) balanceTimerDisposable.dispose();
        balanceTimerDisposable = null;
    }

    public void handleWalletConnect(Context context, String url)
    {
        String importPassData = WalletConnectActivity.WC_LOCAL_PREFIX + url;
        Intent intent = new Intent(context, WalletConnectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("qrCode", importPassData);
        context.startActivity(intent);
    }

    public TokensService getTokenService()
    {
        return tokensService;
    }

    public Single<EthEstimateGas> calculateGasEstimate(Wallet wallet, byte[] transactionBytes, int chainId, String sendAddress, BigDecimal sendAmount)
    {
        return gasService.calculateGasEstimate(transactionBytes, chainId, sendAddress, sendAmount.toBigInteger(), wallet);
    }

    public String getNetworkNodeRPC(int chainId)
    {
        return ethereumNetworkRepository.getNetworkByChain(chainId).rpcServerUrl;
    }
}
