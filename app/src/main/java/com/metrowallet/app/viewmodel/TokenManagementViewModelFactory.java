package com.metrowallet.app.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.metrowallet.app.interact.ChangeTokenEnableInteract;
import com.metrowallet.app.repository.TokenRepositoryType;
import com.metrowallet.app.router.AddTokenRouter;
import com.metrowallet.app.service.AssetDefinitionService;
import com.metrowallet.app.service.TokensService;

import io.reactivex.annotations.NonNull;

public class TokenManagementViewModelFactory implements ViewModelProvider.Factory {

    private final TokenRepositoryType tokenRepository;
    private final ChangeTokenEnableInteract changeTokenEnableInteract;
    private final AddTokenRouter addTokenRouter;
    private final AssetDefinitionService assetDefinitionService;
    private final TokensService tokensService;

    public TokenManagementViewModelFactory(TokenRepositoryType tokenRepository,
                                           ChangeTokenEnableInteract changeTokenEnableInteract,
                                           AddTokenRouter addTokenRouter,
                                           AssetDefinitionService assetDefinitionService,
                                           TokensService tokensService)
    {
        this.tokenRepository = tokenRepository;
        this.changeTokenEnableInteract = changeTokenEnableInteract;
        this.addTokenRouter = addTokenRouter;
        this.assetDefinitionService = assetDefinitionService;
        this.tokensService = tokensService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
    {
        return (T) new TokenManagementViewModel(
                tokenRepository,
                changeTokenEnableInteract,
                addTokenRouter,
                assetDefinitionService,
                tokensService
        );
    }
}
