package com.ozzy.bcsproject;

import java.io.InputStream;
import java.nio.file.NoSuchFileException;
import java.util.Scanner;
/**
 * Ozioma Ihekwoaba
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        StockImporter.DownloadPrices();
        //DividendImporter.DownloadDividends();
    }
}



