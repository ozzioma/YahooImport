package com.ozzy.bcsproject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.stock.StockDividend;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Hello world!
 *
 */
public class StockImporter
{
    public static void SingleStockDemo()
    {
        Stock stock = null;
        try
        {
            stock = YahooFinance.get("IBM");

            BigDecimal price = stock.getQuote().getPrice();
            BigDecimal change = stock.getQuote().getChangeInPercent();
            BigDecimal peg = stock.getStats().getPeg();

            stock.print();
            StockDividend dividend = stock.getDividend();
            System.out.println(dividend);

            //System.out.println( "Hello World!" );

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public static void HistoryStockDemo()
    {
        Stock stock = null;
        String fileName = "F:\\Me Project Docs\\Datasets\\yahoofinance\\IBM-DAILY2.csv";
        try
        {
            stock = YahooFinance.get("IBM");
            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();
            from.add(Calendar.YEAR, -10); // from 5 years ago

            //stock.print();
            List<HistoricalQuote> ibmQuotes = stock.getHistory(from, to, Interval.DAILY);
            File file = new File(fileName);
            Files.write(toCSV(ibmQuotes), file, Charsets.UTF_8);

            //System.out.println(ibmQuotes);
            System.out.println("total quotes->" + ibmQuotes.size());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public static void getPrices(String symbol)
    {
        Stock stock = null;
        //String fileName = "F:\\Me Project Docs\\Datasets\\yahoofinance\\"+symbol+"-monthly.csv";
        //String fileName = "F:\\Me Project Docs\\Datasets\\yahoofinance\\"+symbol+"-weekly.csv";
        String fileName = "F:\\Me Project Docs\\Datasets\\yahoofinance\\"+symbol+".csv";
        try
        {
            stock = YahooFinance.get(symbol);
            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();
            from.add(Calendar.YEAR, -60); // from 5 years ago

            //stock.print();
            List<HistoricalQuote> ibmQuotes = stock.getHistory(from, to, Interval.MONTHLY);
            File file = new File(fileName);
            Files.write(toCSV(ibmQuotes), file, Charsets.UTF_8);

            //System.out.println(ibmQuotes);
            System.out.println("total quotes->" + ibmQuotes.size());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void DownloadCsv()
    {
        Stock stock = null;
        String fileName = "F:\\Me Project Docs\\Datasets\\yahoofinance\\IBM-DAILY.csv";
        try
        {
            List<Ticker2> tickers=loadTickers();
            System.out.println("total tickers->" + tickers.size());

            //System.out.println(tickers);

            for(Ticker2 ticker:tickers)
            {
                System.out.println(ticker);
                getPrices(ticker.Symbol);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String toCSV(List<HistoricalQuote> data)
    {

        String csv = null;
        try
        {
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = mapper.schemaFor(HistoricalQuote.class).withHeader();

            mapper.addMixInAnnotations(HistoricalQuote.class, SingleDayFormat.class);
            csv = mapper.writer(schema).writeValueAsString(data);
        } catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        return csv;
    }

    public static List<Ticker2> loadTickers()
    {
        //String fileName = "F:\\Me Project Docs\\Datasets\\companylist-nyse2.csv";
        String fileName = "F:\\Me Project Docs\\Datasets\\topnyse.csv";

        List<Ticker2> tickers=new ArrayList<Ticker2>();
        try
        {

            File file = new File(fileName);

        CsvMapper mapper = new CsvMapper();
        //CsvSchema schema = mapper.schemaFor(Ticker2.class).withHeader().withColumnSeparator(',');
        CsvSchema schema = mapper.schemaFor(Ticker2.class).withColumnSeparator(',');
        MappingIterator<Ticker2> it = null;
            //it = mapper.reader().with(schema).readValues(file);
            it=mapper.reader(Ticker2.class).with(schema).readValues(file);

            while (it.hasNext()){
                tickers.add(it.next());
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return tickers;
    }
    public abstract class SingleDayFormat
    {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        abstract Calendar getDate();
    }



}



