package com.metrowallet.app.viewmodel;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.metrowallet.app.C;
import com.metrowallet.app.repository.EthereumNetworkRepositoryType;
import com.metrowallet.app.repository.PreferenceRepositoryType;
import com.metrowallet.app.ui.SelectNetworkActivity;
import com.metrowallet.app.entity.NetworkInfo;
import com.metrowallet.app.service.TokensService;

public class SelectNetworkViewModel extends BaseViewModel {
    private final EthereumNetworkRepositoryType networkRepository;
    private final TokensService tokensService;
    private final PreferenceRepositoryType preferenceRepository;

    public SelectNetworkViewModel(EthereumNetworkRepositoryType ethereumNetworkRepositoryType,
                                  TokensService tokensService,
                                  PreferenceRepositoryType preferenceRepository) {
        this.networkRepository = ethereumNetworkRepositoryType;
        this.tokensService = tokensService;
        this.preferenceRepository = preferenceRepository;
    }

    public NetworkInfo[] getNetworkList() {
        return networkRepository.getAvailableNetworkList();
    }

    public String getFilterNetworkList() {
        List<Integer> networkIds = networkRepository.getFilterNetworkList();
        StringBuilder sb = new StringBuilder();
        boolean firstValue = true;
        for (int networkId : networkIds) {
            if (!firstValue) sb.append(",");
            sb.append(networkId);
            firstValue = false;
        }
        return sb.toString();
    }

    public void setFilterNetworks(Integer[] selectedItems) {
        int[] selectedIds = new int[selectedItems.length];
        int index = 0;
        for (Integer selectedId : selectedItems) {
            selectedIds[index++] = selectedId;
        }
        networkRepository.setFilterNetworkList(selectedIds);
        tokensService.setupFilter();
    }

    public List<Integer> getActiveNetworks() {
        return networkRepository.getFilterNetworkList();
    }

    public void openFilterSelect(Activity ctx)
    {
        Intent intent = new Intent(ctx, SelectNetworkActivity.class);
        intent.putExtra(C.EXTRA_SINGLE_ITEM, false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public NetworkInfo getDefaultNetwork()
    {
        return networkRepository.getDefaultNetwork();
    }

    public void setActiveNetwork(int networkId)
    {
        NetworkInfo info = networkRepository.getNetworkByChain(networkId);
        if (info != null)
        {
            networkRepository.setDefaultNetworkInfo(info);
        }
    }

    public boolean isActiveMainnet()
    {
        return preferenceRepository.isActiveMainnet();
    }

    public void setActiveMainnet(boolean flag)
    {
        preferenceRepository.setActiveMainnet(flag);
    }

    public boolean hasShownTestNetWarning()
    {
        return preferenceRepository.hasShownTestNetWarning();
    }

    public void setShownTestNetWarning()
    {
        preferenceRepository.setShownTestNetWarning();
    }
}
