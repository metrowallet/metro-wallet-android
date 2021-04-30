package com.metrowallet.token.entity;

public class BadContract extends Exception
{
    public BadContract()
    {

    }

    public BadContract(String message)
    {
        super(message);
    }
}
