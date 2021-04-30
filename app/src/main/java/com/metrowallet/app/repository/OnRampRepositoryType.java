package com.metrowallet.app.repository;

import com.metrowallet.app.entity.OnRampContract;
import com.metrowallet.app.entity.tokens.Token;

public interface OnRampRepositoryType {
    String getUri(String address, Token token);

    OnRampContract getContract(Token token);
}
