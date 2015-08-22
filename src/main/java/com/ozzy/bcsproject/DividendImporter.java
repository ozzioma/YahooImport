package com.ozzy.bcsproject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.*;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.stock.StockDividend;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.ProtocolException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
//import org.json.JSONException;


public final class DividendImporter
{
    private final static String USER_AGENT = "Mozilla/5.0";


    public static  void ImportHistory1()
    {
        String symbol="IBM";
        try
        {
            // String url ="http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22"+from+to+"%22)&env=store://datatables.org/alltableswithkeys";
            String url="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.dividendhistory%20where%20symbol%20%3D%20%22"+symbol+"%22%20and%20startDate%20%3D%20%221962-01-01%22%20and%20endDate%20%3D%20%222014-12-31%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";

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

            // Converting XML to JSON and then JSON to POJO Classes
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.setPrettyPrinting().create();

            System.out.println(response.toString());
            JsonObject json = gson.fromJson(response.toString(), JsonObject.class);

            JsonElement json2 = new JsonPrimitive(response.toString());
            JsonElement json3 = gson.fromJson(response.toString(), JsonElement.class);

//            Gson myGson = new Gson();
//            JsonParser jsonParser = new JsonParser();
//            JsonArray jsonArray =  jsonParser.parse(response.toString()).getAsJsonArray();

            System.out.println(json);
            System.out.println(json2);
            System.out.println(json3);
//            System.out.println(jsonArray);

            System.out.println("\n\n");
            System.out.println(json.get("results"));
            System.out.println(json.getAsJsonArray("results"));
            System.out.println(json.getAsJsonObject("results"));
            System.out.println(json.getAsJsonPrimitive("results"));

            System.out.println("\n\n");

            System.out.println(json.get("$..quote.*"));
            System.out.println(json.getAsJsonArray("quote"));
            System.out.println(json.getAsJsonObject("quote"));
            System.out.println(json.getAsJsonPrimitive("quote"));

            System.out.println("\n\n");



            Response results = gson.fromJson(response.toString(), Response.class);
            List<Quote> quotes=results.getQuery().getResults().getQuote();

            System.out.println("quotes pretty");
            System.out.println(gson.toJson(quotes));
            System.out.println("\n \n quotes \n");
            System.out.println(new Gson().toJson(quotes));
            //System.out.println(gson.toJsonTree(quotes));

//            for(Quote quote:quotes)
//            {
//                System.out.println(quote.getSymbol()+","+quote.getDate()+","+quote.getDividends());
//
//            }


        } catch (ProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public static  void  DownloadJson()
    {
        try
        {
            List<Ticker2> tickers=StockImporter.loadTickers();
            System.out.println("total tickers->" + tickers.size());

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();


            for(Ticker2 ticker:tickers)
            {
                System.out.println(ticker);

                String url="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.dividendhistory%20where%20symbol%20%3D%20%22"+
                    ticker.Symbol+"%22%20and%20startDate%20%3D%20%221962-01-01%22%20and%20endDate%20%3D%20%222014-12-31%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";

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
                System.out.println(rsp);
                //JsonObject json = gson.fromJson(response.toString(), JsonObject.class);

                Response qryRsp = gson.fromJson(rsp, Response.class);
                Results results=qryRsp.getQuery().getResults();

                if(results !=null)
                {
                    List<Quote> quotes=results.getQuote();
                    System.out.println("quotes pretty");
                    String jsonQuotes=gson.toJson(quotes);
                    System.out.println(jsonQuotes);

                    String fileName = "F:\\Me Project Docs\\Datasets\\yahoofinance\\dividends\\"+ticker.Symbol+"-dividends.json";
                    File file = new File(fileName);
                    Files.write(jsonQuotes, file, Charsets.UTF_8);

                }

            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public static  void  DownloadCsv()
    {
        try
        {
            List<Ticker2> tickers=StockImporter.loadTickers();
            System.out.println("total tickers->" + tickers.size());

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();


            for(Ticker2 ticker:tickers)
            {
                System.out.println(ticker);

                String url="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.dividendhistory%20where%20symbol%20%3D%20%22"+
                    ticker.Symbol+"%22%20and%20startDate%20%3D%20%221962-01-01%22%20and%20endDate%20%3D%20%222014-12-31%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";

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
                System.out.println(rsp);

                Response qryRsp = gson.fromJson(rsp, Response.class);
                Results results=qryRsp.getQuery().getResults();

                if(results !=null)
                {
                    List<Quote> quotes=results.getQuote();
                    System.out.println("quotes pretty");
                    String csvQuotes=toCSV(quotes);
                    System.out.println(csvQuotes);

                    String fileName = "F:\\Me Project Docs\\Datasets\\yahoofinance\\dividends\\"+ticker.Symbol+"-dividends.csv";
                    File file = new File(fileName);
                    Files.write(csvQuotes, file, Charsets.UTF_8);

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

        /**
         *
         * @return
         * The quote
         */
        public List<Quote> getQuote() {
            return quote;
        }

        /**
         *
         * @param quote
         * The quote
         */
        public void setQuote(List<Quote> quote) {
            this.quote = quote;
        }

    }

    @AutoProperty
    class Quote
    {
        private String Symbol;
        private String Date;
        private String Dividends;

        /**
         *
         * @return
         * The Symbol
         */
        public String getSymbol() {
            return Symbol;
        }

        /**
         *
         * @param Symbol
         * The Symbol
         */
        public void setSymbol(String Symbol) {
            this.Symbol = Symbol;
        }

        /**
         *
         * @return
         * The Date
         */
        public String getDate() {
            return Date;
        }

        /**
         *
         * @param Date
         * The Date
         */
        public void setDate(String Date) {
            this.Date = Date;
        }

        /**
         *
         * @return
         * The Dividends
         */
        public String getDividends() {
            return Dividends;
        }

        /**
         *
         * @param Dividends
         * The Dividends
         */
        public void setDividends(String Dividends) {
            this.Dividends = Dividends;
        }

//        @Override public boolean equals(Object o) {
//            return Pojomatic.equals(this, o);
//        }
//
//        @Override public int hashCode() {
//            return Pojomatic.hashCode(this);
//        }
//
//        @Override public String toString() {
//            return Pojomatic.toString(this);
//        }
    }
}
