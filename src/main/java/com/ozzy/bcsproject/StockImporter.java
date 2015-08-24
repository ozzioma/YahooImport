package com.ozzy.bcsproject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import org.joda.time.*;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.stock.StockDividend;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
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
    private final static String USER_AGENT = "Mozilla/5.0";

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
            //Files.write(toCSV(ibmQuotes), file, Charsets.UTF_8);

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
            //Files.write(toCSV(ibmQuotes), file, Charsets.UTF_8);

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


    public static  void  DownloadPrices()
    {
        try
        {
            List<Ticker2> tickers=StockImporter.loadTickers();
            System.out.println("total tickers->" + tickers.size());

            List<Integer> years=new ArrayList<>();

            years.add(1980);
            years.add(1981);
            years.add(1982);
            years.add(1983);
            years.add(1984);
            years.add(1985);
            years.add(1986);
            years.add(1987);
            years.add(1988);
            years.add(1989);

            years.add(1990);
            years.add(1991);
            years.add(1992);
            years.add(1993);
            years.add(1994);
            years.add(1995);
            years.add(1996);
            years.add(1997);
            years.add(1998);
            years.add(1999);

            years.add(2000);
            years.add(2001);
            years.add(2002);
            years.add(2003);
            years.add(2004);
            years.add(2005);
            years.add(2006);
            years.add(2007);
            years.add(2008);
            years.add(2009);
            years.add(2010);

            years.add(2011);
            years.add(2012);
            years.add(2013);
            years.add(2014);
            years.add(2015);


            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            first:
            for(int year:years)
            {
                String from=year+"-01-01";
                String to=year+"-12-31";

                second:
                for(Ticker2 ticker:tickers)
                {
                    System.out.println(ticker);

                    String url="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22"+
                        ticker.Symbol+"%22%20and%20startDate%20%3D%20%22"+from+"%22%20and%20endDate%20%3D%20%22"+to+"%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";

                    System.out.println(URLDecoder.decode(url, "UTF-8"));

                    URL obj = new URL(url);

                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    con.setRequestMethod("GET");

                    //add request header
                    con.setRequestProperty("User-Agent", USER_AGENT);

                    // Send request
                    int responseCode = con.getResponseCode();
                    System.out.println("\nSending 'GET' request to URL : " + url);
                    System.out.println("Response Code : " + responseCode + "\n\n");
                    BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                    String outputLine;

                    // reading output from Request
                    StringBuffer response = new StringBuffer();
                    while ((outputLine = in.readLine()) != null) {
                        response.append(outputLine);
                    }
                    in.close();

                    String rsp=response.toString();
                    //System.out.println(rsp);

                    Response qryRsp;
                    Results results;

                    try
                    {
                        qryRsp = gson.fromJson(rsp, Response.class);
                        results=qryRsp.getQuery().getResults();

                    }
                    catch (IllegalStateException ex)
                    {
                       continue second;
                    }
                    catch (JsonSyntaxException ex)
                    {
                        continue second;
                    }

                    if(results !=null)
                    {
                        List<Quote> quotes=results.getQuote();
                        System.out.println("quotes pretty");
                        String csvQuotes=toCSV(quotes);
                        System.out.println(csvQuotes);

                        String fileName = "F:\\Me Project Docs\\Datasets\\yahoofinance\\prices\\"+ticker.Symbol+"-"+year+".csv";
                        File file = new File(fileName);
                        Files.write(csvQuotes, file, Charsets.UTF_8);

                    }

                }


            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static String toCSV(List<Quote> data)
    {

        String csv = null;
        try
        {
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = mapper.schemaFor(Quote.class).withHeader();

            mapper.addMixInAnnotations(Quote.class, SingleDayFormat.class);
            csv = mapper.writer(schema).writeValueAsString(data);
        } catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        return csv;
    }

    public static List<Ticker2> loadTickers()
    {
        String fileName = "./topnyse.csv";
        //String fileName = "F:\\Me Project Docs\\Datasets\\topnyse.csv";

        List<Ticker2> tickers=new ArrayList<Ticker2>();
        try
        {

            //File file = new File(fileName);
            ResourceLoader loader=new ResourceLoader(fileName);

            CsvMapper mapper = new CsvMapper();
            //CsvSchema schema = mapper.schemaFor(Ticker2.class).withHeader().withColumnSeparator(',');
            CsvSchema schema = mapper.schemaFor(Ticker2.class).withColumnSeparator(',');
            MappingIterator<Ticker2> it = null;

            //it=mapper.reader(Ticker2.class).with(schema).readValues(file);
            it=mapper.reader(Ticker2.class).with(schema).readValues(loader.getResourceStream());

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

    public class Response
    {
        private Query query;

        public Query getQuery()
        {
            return query;
        }

        public void setQuery(Query query)
        {
            this.query = query;
        }

    }

    class Query
    {
        private int count;
        private Date created;
        private String lang;
        private Results results;

        public Date getCreated()
        {
            return created;
        }

        public void setCreated(Date created)
        {
            this.created = created;
        }

        public String getLang()
        {
            return lang;
        }

        public void setLang(String lang)
        {
            this.lang = lang;
        }

        public int getCount()
        {
            return count;
        }
        public void setCount(int count)
        {
            this.count = count;
        }
        public Results getResults()
        {
            return results;
        }
        public void setResults(Results results)
        {
            this.results = results;
        }


    }

    class Results
    {
        private List<Quote> quote = new ArrayList<Quote>();

        public List<Quote> getQuote() {
            return quote;
        }


        public void setQuote(List<Quote> quote) {
            this.quote = quote;
        }

    }

    public class Quote {

        private String Symbol;
        private String Date;
        private String Open;
        private String High;
        private String Low;
        private String Close;
        private String Volume;
        @SerializedName("Adj_Close")
        private String AdjClose;


        public String getSymbol() {
            return Symbol;
        }


        public void setSymbol(String Symbol) {
            this.Symbol = Symbol;
        }


        public String getDate() {
            return Date;
        }


        public void setDate(String Date) {
            this.Date = Date;
        }


        public String getOpen() {
            return Open;
        }


        public void setOpen(String Open) {
            this.Open = Open;
        }


        public String getHigh() {
            return High;
        }


        public void setHigh(String High) {
            this.High = High;
        }


        public String getLow() {
            return Low;
        }


        public void setLow(String Low) {
            this.Low = Low;
        }


        public String getClose() {
            return Close;
        }


        public void setClose(String Close) {
            this.Close = Close;
        }


        public String getVolume() {
            return Volume;
        }


        public void setVolume(String Volume) {
            this.Volume = Volume;
        }


        public String getAdjClose() {
            return AdjClose;
        }


        public void setAdjClose(String AdjClose) {
            this.AdjClose = AdjClose;
        }



    }
}



