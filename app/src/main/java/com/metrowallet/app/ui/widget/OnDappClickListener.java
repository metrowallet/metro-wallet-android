package com.metrowallet.app.ui.widget;

import java.io.Serializable;

import com.metrowallet.app.entity.DApp;

public interface OnDappClickListener extends Serializable {
    void onDappClick(DApp dapp);
}
