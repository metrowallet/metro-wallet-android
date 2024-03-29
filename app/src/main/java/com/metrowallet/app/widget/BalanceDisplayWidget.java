package com.metrowallet.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.metrowallet.app.R;
import com.metrowallet.app.entity.tokens.Token;
import com.metrowallet.app.service.TokensService;
import com.metrowallet.app.util.BalanceUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by Jenny Jingjing Li on 4/3/2021
 */

public class BalanceDisplayWidget extends LinearLayout
{
    public final TextView balance;
    public final TextView newBalance;
    private final ChainName chainName;
    private final TokenIcon chainIcon;

    public BalanceDisplayWidget(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        inflate(context, R.layout.item_balance_display, this);
        balance = findViewById(R.id.text_balance);
        newBalance = findViewById(R.id.text_new_balance);
        chainName = findViewById(R.id.chain_name);
        chainIcon = findViewById(R.id.chain_icon);
    }

    public void setupBalance(Token token, TokensService tokenService)
    {
        chainName.setChainID(token.tokenInfo.chainId);
        chainName.invertChainID(token.tokenInfo.chainId);
        chainIcon.bindData(tokenService.getToken(token.tokenInfo.chainId, tokenService.getCurrentAddress()), null);

        balance.setText(getContext().getString(R.string.total_cost, token.getStringBalance(), token.getSymbol()));
    }

    public void setNewBalanceText(Token token, BigDecimal transactionAmount, BigInteger networkFee, BigInteger balanceAfterTransaction)
    {

        if (token.isEthereum())
        {
            balanceAfterTransaction = balanceAfterTransaction.subtract(networkFee).max(BigInteger.ZERO);
        }
        else
        {
            balanceAfterTransaction = token.getBalanceRaw().subtract(transactionAmount).toBigInteger();
        }

        //convert to ETH amount
        String newBalanceVal = BalanceUtils.getScaledValueScientific(new BigDecimal(balanceAfterTransaction), token.tokenInfo.decimals);
        newBalance.setText(getContext().getString(R.string.new_balance, newBalanceVal, token.getSymbol()));
    }

}