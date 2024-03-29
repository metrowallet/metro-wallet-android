package com.metrowallet.app.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.metrowallet.app.C;
import com.metrowallet.app.R;
import com.metrowallet.app.entity.BackupOperationType;
import com.metrowallet.app.entity.ErrorEnvelope;
import com.metrowallet.app.entity.Wallet;
import com.metrowallet.app.entity.WalletType;
import com.metrowallet.app.ui.widget.entity.AddressReadyCallback;
import com.metrowallet.app.util.Blockies;
import com.metrowallet.app.util.Utils;
import com.metrowallet.app.viewmodel.WalletActionsViewModel;
import com.metrowallet.app.viewmodel.WalletActionsViewModelFactory;
import com.metrowallet.app.widget.AWalletAlertDialog;
import com.metrowallet.app.widget.InputAddress;
import com.metrowallet.app.widget.SettingsItemView;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.metrowallet.app.C.BACKUP_WALLET_SUCCESS;
import static com.metrowallet.app.C.Key.WALLET;
import static com.metrowallet.app.C.SHARE_REQUEST_CODE;

public class WalletActionsActivity extends BaseActivity implements Runnable, View.OnClickListener, AddressReadyCallback
{
    @Inject
    WalletActionsViewModelFactory walletActionsViewModelFactory;
    WalletActionsViewModel viewModel;

    private ImageView walletIcon;
    private TextView walletBalance;
    private TextView walletBalanceCurrency;
    private TextView walletNameText;
    private TextView walletAddressSeparator;
    private TextView walletAddressText;
    private ImageView walletSelectedIcon;
    private SettingsItemView deleteWalletSetting;
    private SettingsItemView backUpSetting;
    private InputAddress inputAddress;
    private LinearLayout successOverlay;
    private AWalletAlertDialog aDialog;
    private final Handler handler = new Handler();

    private Wallet wallet;
    private int walletCount;
    private boolean isNewWallet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_actions);
        toolbar();
        setTitle(getString(R.string.manage_wallet));

        if (getIntent() != null) {
            wallet = (Wallet) getIntent().getExtras().get("wallet");
            walletCount = getIntent().getIntExtra("walletCount", 0);
            walletCount++;
            isNewWallet = getIntent().getBooleanExtra("isNewWallet", false);
            initViews();
        } else {
            finish();
        }

        initViewModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        successOverlay = findViewById(R.id.layout_success_overlay);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this, walletActionsViewModelFactory)
                .get(WalletActionsViewModel.class);

        viewModel.saved().observe(this, this::onSaved);
        viewModel.deleteWalletError().observe(this, this::onDeleteError);
        viewModel.exportWalletError().observe(this, this::onExportError);
        viewModel.deleted().observe(this, this::onDeleteWallet);
        viewModel.exportedStore().observe(this, this::onBackupWallet);
        viewModel.isTaskRunning().observe(this, this::onTaskStatusChanged);

        if (isNewWallet) {
            wallet.name = getString(R.string.wallet_name_template, walletCount);
            viewModel.storeWallet(wallet);
        }
    }

    private void onTaskStatusChanged(Boolean isTaskRunning) {

    }

    private void onSaved(Integer integer)
    {
        //refresh the WalletHolder
        setENSText();
    }

    private void onBackupWallet(String keystore) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Keystore");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, keystore);
        startActivityForResult(
                Intent.createChooser(sharingIntent, "Share via"),
                SHARE_REQUEST_CODE);
    }

    private void onDeleteWallet(Boolean isDeleted) {
        if (isDeleted) {
            showWalletsActivity();
        }
    }

    private void onExportError(ErrorEnvelope errorEnvelope) {
        aDialog = new AWalletAlertDialog(this);
        aDialog.setTitle(R.string.title_dialog_error);
        aDialog.setIcon(AWalletAlertDialog.ERROR);
        aDialog.setMessage(TextUtils.isEmpty(errorEnvelope.message)
                ? getString(R.string.error_export)
                : errorEnvelope.message);
        aDialog.setButtonText(R.string.dialog_ok);
        aDialog.setButtonListener(v -> aDialog.dismiss());
        aDialog.show();
    }

    private void onDeleteError(ErrorEnvelope errorEnvelope) {
        aDialog = new AWalletAlertDialog(this);
        aDialog.setTitle(R.string.title_dialog_error);
        aDialog.setIcon(AWalletAlertDialog.ERROR);
        aDialog.setMessage(TextUtils.isEmpty(errorEnvelope.message)
                ? getString(R.string.error_deleting_account)
                : errorEnvelope.message);
        aDialog.setButtonText(R.string.dialog_ok);
        aDialog.setButtonListener(v -> aDialog.dismiss());
        aDialog.show();
    }

    private void initViews() {
        walletIcon = findViewById(R.id.wallet_icon);
        walletBalance = findViewById(R.id.wallet_balance);
        walletBalanceCurrency = findViewById(R.id.wallet_currency);
        walletNameText = findViewById(R.id.wallet_name);
        walletAddressSeparator = findViewById(R.id.wallet_address_separator);
        walletAddressText = findViewById(R.id.wallet_address);
        deleteWalletSetting = findViewById(R.id.delete);
        backUpSetting = findViewById(R.id.setting_backup);
        walletSelectedIcon = findViewById(R.id.selected_wallet_indicator);
        inputAddress = findViewById(R.id.input_ens);
        walletSelectedIcon.setOnClickListener(this);

        walletIcon.setImageBitmap(Blockies.createIcon(wallet.address.toLowerCase()));

        walletBalance.setText(wallet.balance);
        walletBalanceCurrency.setText(wallet.balanceSymbol);

        setENSText();

        walletAddressText.setText(Utils.formatAddress(wallet.address));

        deleteWalletSetting.setListener(this::onDeleteWalletSettingClicked);

        backUpSetting.setListener(this::onBackUpSettingClicked);

        if (wallet.type == WalletType.KEYSTORE) {
            backUpSetting.setTitle(getString(R.string.export_keystore_json));
            TextView backupDetail = findViewById(R.id.backup_text);
            backupDetail.setText(R.string.export_keystore_detail);
        } else if (wallet.type == WalletType.WATCH) {
            findViewById(R.id.layout_backup_method).setVisibility(View.GONE);
        }

        walletSelectedIcon.setImageResource(R.drawable.ic_copy);

        inputAddress.setAddress(wallet.ENSname);
        inputAddress.setAddressCallback(this);
    }

    private void setENSText()
    {
        if (wallet.ENSname != null && !wallet.ENSname.isEmpty()) {
            walletNameText.setText(wallet.ENSname);
            walletNameText.setVisibility(View.VISIBLE);
            walletAddressSeparator.setVisibility(View.VISIBLE);
        } else {
            walletNameText.setVisibility(View.GONE);
            walletAddressSeparator.setVisibility(View.GONE);
        }
    }

    private void onDeleteWalletSettingClicked() {
        confirmDelete(wallet);
    }

    private void onBackUpSettingClicked() {
        doBackUp();
    }

    private void saveWalletName() {
//        wallet.name = walletNameText.getText().toString();
        viewModel.storeWallet(wallet);
        if (isNewWallet) {
            viewModel.showHome(this);
            finish(); //drop back to home screen, no need to recreate everything
        }
    }

    private void doBackUp() {
        if (wallet.type == WalletType.HDKEY) {
            testSeedPhrase(wallet);
        } else {
            exportJSON(wallet);
        }
    }

    private void testSeedPhrase(Wallet wallet) {
        Intent intent = new Intent(this, BackupKeyActivity.class);
        intent.putExtra(WALLET, wallet);
        intent.putExtra("TYPE", BackupOperationType.SHOW_SEED_PHRASE);
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivityForResult(intent, C.REQUEST_BACKUP_WALLET);
    }

    private void showWalletsActivity() {
        finish();
    }

    private void confirmDelete(Wallet wallet) {
        aDialog = new AWalletAlertDialog(this);
        aDialog.setIcon(AWalletAlertDialog.WARNING);
        aDialog.setTitle(R.string.title_delete_account);
        aDialog.setMessage(R.string.confirm_delete_account);
        aDialog.setButtonText(R.string.dialog_ok);
        aDialog.setButtonListener(v -> {
            viewModel.deleteWallet(wallet);
            aDialog.dismiss();
        });
        aDialog.setSecondaryButtonText(R.string.dialog_cancel_back);
        aDialog.show();
    }

    private void exportJSON(Wallet wallet) {
        Intent intent = new Intent(this, BackupKeyActivity.class);
        intent.putExtra(WALLET, wallet);
        intent.putExtra("TYPE", BackupOperationType.BACKUP_KEYSTORE_KEY);
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivityForResult(intent, C.REQUEST_BACKUP_WALLET);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SHARE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, R.string.toast_message_wallet_exported, Toast.LENGTH_SHORT).show();
                showToolbar();
                hideDialog();
                backupSuccessful();
            } else {
                aDialog = new AWalletAlertDialog(this);
                aDialog.setIcon(AWalletAlertDialog.NONE);
                aDialog.setTitle(R.string.do_manage_make_backup);
                aDialog.setButtonText(R.string.yes_continue);
                aDialog.setButtonListener(v -> {
                    backupSuccessful();
                    hideDialog();
                    showToolbar();
                });
                aDialog.setSecondaryButtonText(R.string.no_repeat);
                aDialog.setSecondaryButtonListener(v -> {
                    onBackupWallet(viewModel.exportedStore().getValue());
                    hideDialog();
                });
                aDialog.show();
            }
        } else if (requestCode == C.REQUEST_BACKUP_WALLET && resultCode == RESULT_OK) {
            successOverlay.setVisibility(View.VISIBLE);
            handler.postDelayed(this, 1000);
            backupSuccessful();
        }
    }

    private void backupSuccessful() {
        Intent intent = new Intent(BACKUP_WALLET_SUCCESS);
        intent.putExtra("Key", wallet.address);
        sendBroadcast(intent);
    }

    private void hideDialog() {
        if (aDialog != null && aDialog.isShowing()) {
            aDialog.dismiss();
            aDialog = null;
        }
    }

    @Override
    public void run() {
        if (successOverlay.getAlpha() > 0) {
            successOverlay.animate().alpha(0.0f).setDuration(500);
            handler.postDelayed(this, 750);
        } else {
            successOverlay.setVisibility(View.GONE);
            successOverlay.setAlpha(1.0f);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selected_wallet_indicator:
                copyToClipboard();
                break;
        }
    }

    private void copyToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("walletAddress", wallet.address);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void resolvedAddress(String address, String ensName)
    {
        if (!TextUtils.isEmpty(address)
                && wallet.address.equalsIgnoreCase(address)
                && !TextUtils.isEmpty(ensName)
                && (TextUtils.isEmpty(wallet.ENSname) || !ensName.equalsIgnoreCase(wallet.ENSname))) //Wallet ENS currently empty or new ENS name is different
        {
            wallet.ENSname = ensName;
            //update database
            viewModel.storeWallet(wallet);
            successOverlay.setVisibility(View.VISIBLE);
            handler.postDelayed(this, 1000);
        }
        else if (TextUtils.isEmpty(wallet.ENSname) || !ensName.equalsIgnoreCase(wallet.ENSname))
        {
            Toast.makeText(this, R.string.ens_not_match_wallet, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void addressReady(String address, String ensName)
    {

    }
}
