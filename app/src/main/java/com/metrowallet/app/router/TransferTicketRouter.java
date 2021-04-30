package com.metrowallet.app.router;

import android.content.Context;
import android.content.Intent;

import com.metrowallet.app.C;
import com.metrowallet.app.ui.TransferTicketActivity;
import com.metrowallet.app.entity.tokens.Token;

/**
 * Created by James on 13/02/2018.
 */

public class TransferTicketRouter
{
    public void open(Context context, Token ticket) {
        Intent intent = new Intent(context, TransferTicketActivity.class);
        intent.putExtra(C.Key.TICKET, ticket);
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.startActivity(intent);
    }
}
