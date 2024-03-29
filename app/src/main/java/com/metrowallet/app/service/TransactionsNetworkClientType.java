package com.metrowallet.app.service;

import com.metrowallet.app.entity.NetworkInfo;
import com.metrowallet.app.entity.Transaction;
import com.metrowallet.app.entity.TransactionMeta;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface TransactionsNetworkClientType {
    Single<Transaction[]> storeNewTransactions(String walletAddress, NetworkInfo networkInfo, String tokenAddress, long lastBlock);
    Single<TransactionMeta[]> fetchMoreTransactions(String walletAddress, NetworkInfo network, long lastTxTime);
    Single<Integer> readTransactions(String currentAddress, NetworkInfo networkByChain, TokensService tokensService, boolean nftCheck);
    void checkTransactionsForEmptyFunctions(String currentAddress);
}
