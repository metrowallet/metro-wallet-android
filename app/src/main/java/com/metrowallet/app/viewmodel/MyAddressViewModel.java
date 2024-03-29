package com.metrowallet.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.metrowallet.app.repository.EthereumNetworkRepositoryType;
import com.metrowallet.app.repository.TokenRepositoryType;
import com.metrowallet.app.entity.NetworkInfo;
import com.metrowallet.app.interact.FindDefaultNetworkInteract;
import com.metrowallet.app.service.TokensService;

public class MyAddressViewModel extends BaseViewModel {
    private final FindDefaultNetworkInteract findDefaultNetworkInteract;
    private final EthereumNetworkRepositoryType ethereumNetworkRepository;
    private final TokensService tokenService;

    private final MutableLiveData<NetworkInfo> defaultNetwork = new MutableLiveData<>();

    MyAddressViewModel(
            FindDefaultNetworkInteract findDefaultNetworkInteract,
            EthereumNetworkRepositoryType ethereumNetworkRepository,
            TokensService tokensService) {
        this.findDefaultNetworkInteract = findDefaultNetworkInteract;
        this.ethereumNetworkRepository = ethereumNetworkRepository;
        this.tokenService = tokensService;
    }

    public TokensService getTokenService() {
        return tokenService;
    }

    public EthereumNetworkRepositoryType getEthereumNetworkRepository() {
        return ethereumNetworkRepository;
    }

    public void prepare() {
        progress.postValue(true);
        disposable = findDefaultNetworkInteract
                .find()
                .subscribe(this::onDefaultNetwork, this::onError);
    }

    private void onDefaultNetwork(NetworkInfo networkInfo) {
        defaultNetwork.postValue(networkInfo);
    }

    public LiveData<NetworkInfo> defaultNetwork() {
        return defaultNetwork;
    }

    public NetworkInfo setNetwork(int chainId)
    {
        NetworkInfo info = ethereumNetworkRepository.getNetworkByChain(chainId);
        if (info != null)
        {
            ethereumNetworkRepository.setDefaultNetworkInfo(info);
            defaultNetwork.postValue(info);
            return info;
        }

        return null;
    }
}
