package com.metrowallet.app.router;

import android.content.Context;
import android.content.Intent;

import com.metrowallet.app.C;
import com.metrowallet.app.ui.TransferTicketDetailActivity;
import com.metrowallet.app.entity.tokens.Token;
import com.metrowallet.app.entity.Wallet;

/**
 * Created by James on 22/02/2018.
 */

public class TransferTicketDetailRouter {

    public void open(Context context, Token token, String ticketIDs, Wallet wallet) {
        Intent intent = new Intent(context, TransferTicketDetailActivity.class);
        intent.putExtra(C.Key.WALLET, wallet);
        intent.putExtra(C.Key.TICKET, token);
        intent.putExtra(C.EXTRA_TOKENID_LIST, ticketIDs);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public void openTransfer(Context context, Token token, String ticketIDs, Wallet wallet, int state) {
        Intent intent = new Intent(context, TransferTicketDetailActivity.class);
        intent.putExtra(C.Key.WALLET, wallet);
        intent.putExtra(C.Key.TICKET, token);
        intent.putExtra(C.EXTRA_TOKENID_LIST, ticketIDs);
        intent.putExtra(C.EXTRA_STATE, state);
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.startActivity(intent);
    }
}
