package com.metrowallet.app.entity.walletconnect;

import com.metrowallet.app.repository.EthereumNetworkBase;
import com.metrowallet.app.ui.WalletConnectActivity;
import com.metrowallet.app.walletconnect.entity.WCEthereumSignMessage;
import com.metrowallet.app.walletconnect.entity.WCEthereumTransaction;
import com.metrowallet.app.walletconnect.entity.WCPeerMeta;

/**
 * Created by JB on 10/09/2020.
 */
public class WCRequest
{
    public final long id;
    public final String sessionId;
    public final WCEthereumTransaction tx;
    public final WCEthereumSignMessage sign;
    public final WCPeerMeta peer;
    public final SignType type;
    public final int chainId;
    public final Throwable throwable;

    public WCRequest(String sessionId, long id, WCEthereumSignMessage msg)
    {
        this.sessionId = sessionId;
        this.id = id;
        this.sign = msg;
        type = SignType.MESSAGE;
        tx = null;
        peer = null;
        chainId = EthereumNetworkBase.MAINNET_ID;
        throwable = null;
    }

    public WCRequest(String sessionId, long id, WCEthereumTransaction tx, boolean signOnly, int chainId)
    {
        this.sessionId = sessionId;
        this.id = id;
        this.sign = null;
        type = signOnly ? SignType.SIGN_TX : SignType.SEND_TX;
        this.tx = tx;
        this.chainId = chainId;
        peer = null;
        throwable = null;
    }

    public WCRequest(String sessionId, long id, WCPeerMeta pm, int chainId)
    {
        this.sessionId = sessionId;
        this.id = id;
        this.sign = null;
        this.chainId = chainId;
        type = SignType.SESSION_REQUEST;
        tx = null;
        peer = pm;
        throwable = null;
    }

    public WCRequest(String sessionId, Throwable t, int chainId)
    {
        this.sessionId = sessionId;
        this.id = 0;
        this.sign = null;
        this.chainId = chainId;
        type = SignType.FAILURE;
        tx = null;
        peer = null;
        throwable = t;
    }
}
