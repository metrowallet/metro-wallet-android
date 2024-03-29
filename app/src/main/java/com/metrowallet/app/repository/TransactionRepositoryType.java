package com.metrowallet.app.repository;

import com.metrowallet.app.entity.ActivityMeta;
import com.metrowallet.app.entity.Transaction;
import com.metrowallet.app.entity.TransactionData;
import com.metrowallet.app.entity.Wallet;
import com.metrowallet.app.entity.cryptokeys.SignatureFromKey;
import com.metrowallet.app.repository.entity.RealmAuxData;
import com.metrowallet.app.web3.entity.Web3Transaction;
import com.metrowallet.token.entity.Signable;

import org.web3j.protocol.core.methods.response.EthTransaction;

import java.math.BigInteger;
import java.util.List;

import io.reactivex.Single;
import io.realm.Realm;

public interface TransactionRepositoryType {
	Single<String> createTransaction(Wallet from, String toAddress, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, byte[] data, int chainId);
	Single<TransactionData> createTransactionWithSig(Wallet from, String toAddress, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, long nonce, byte[] data, int chainId);
	Single<TransactionData> createTransactionWithSig(Wallet from, BigInteger gasPrice, BigInteger gasLimit, String data, int chainId);
	Single<TransactionData> getSignatureForTransaction(Wallet wallet, Web3Transaction w3tx, int chainId);
	Single<SignatureFromKey> getSignature(Wallet wallet, Signable message, int chainId);
	Single<byte[]> getSignatureFast(Wallet wallet, String password, byte[] message, int chainId);

    Transaction fetchCachedTransaction(String walletAddr, String hash);
	long fetchTxCompletionTime(String walletAddr, String hash);

	Single<String> resendTransaction(Wallet from, String to, BigInteger subunitAmount, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, byte[] data, int chainId);

	void removeOldTransaction(Wallet wallet, String oldTxHash);

    Single<ActivityMeta[]> fetchCachedTransactionMetas(Wallet wallet, List<Integer> networkFilters, long fetchTime, int fetchLimit);
	Single<ActivityMeta[]> fetchCachedTransactionMetas(Wallet wallet, int chainId, String tokenAddress, int historyCount);
	Single<ActivityMeta[]> fetchEventMetas(Wallet wallet, List<Integer> networkFilters);

	Realm getRealmInstance(Wallet wallet);

	RealmAuxData fetchCachedEvent(String walletAddress, String eventKey);
	Single<Transaction> storeRawTx(Wallet wallet, EthTransaction rawTx, long timeStamp);

    void restartService();
}
