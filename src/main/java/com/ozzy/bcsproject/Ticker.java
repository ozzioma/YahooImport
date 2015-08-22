package com.ozzy.bcsproject;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
@JsonPropertyOrder({ "Symbol", "Name"})
class Ticker2 implements Serializable
{
    public String Symbol;
    public String Name;

    @Override public boolean equals(Object o) {
        return Pojomatic.equals(this, o);
    }

    @Override public int hashCode() {
        return Pojomatic.hashCode(this);
    }

    @Override public String toString() {
        return Pojomatic.toString(this);
    }
}

@AutoProperty
@JsonPropertyOrder({ "Symbol", "Name", "LastSale","MarketCap","IPOYear","Sector","industry" })
public class Ticker implements Serializable
{
    public String Symbol;
    public  String Name;
    public  String LastSale;
    public String MarketCap;
    public String IPOYear;
    public String Sector;
    public String industry;

    public Ticker()
    {

    }

    public String getSymbol()
    {
        return Symbol;
    }

    public void setSymbol(String symbol)
    {
        Symbol = symbol;
    }

    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        Name = name;
    }

    public String getLastSale()
    {
        return LastSale;
    }

    public void setLastSale(String lastSale)
    {
        LastSale = lastSale;
    }

    public String getMarketCap()
    {
        return MarketCap;
    }

    public void setMarketCap(String marketCap)
    {
        MarketCap = marketCap;
    }

    public String getIPOYear()
    {
        return IPOYear;
    }

    public void setIPOYear(String IPOYear)
    {
        this.IPOYear = IPOYear;
    }

    public String getSector()
    {
        return Sector;
    }

    public void setSector(String sector)
    {
        Sector = sector;
    }

    public String getIndustry()
    {
        return industry;
    }

    public void setIndustry(String industry)
    {
        this.industry = industry;
    }

    @Override public boolean equals(Object o) {
        return Pojomatic.equals(this, o);
    }

    @Override public int hashCode() {
        return Pojomatic.hashCode(this);
    }

    @Override public String toString() {
        return Pojomatic.toString(this);
    }
}
