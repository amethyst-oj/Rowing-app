package com.example.row.implementation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;
import java.util.Objects;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Flags {
        private static final String flag_url= "http://m.cucbc.org/";
        private static final String info_url= "https://www.cucbc.org/handbook/flag";
        private static final Map<String,String> flag_meaning= new HashMap<>();
        private static String flag;

    public static String getFlagColour() throws IOException {
        Document doc= Jsoup.connect(flag_url).get();
        Element content= doc.getElementById("status");
        flag = Objects.requireNonNull(content.getElementsByTag("p").first()).text();
        Pattern pattern= Pattern.compile("The flag is (Green|Red/Yellow|Red|Yellow)", Pattern.CASE_INSENSITIVE);
        Matcher matcher= pattern.matcher(flag);
        flag= flag.replaceAll("The flag is ", "");
        flag= flag.replaceAll("\\.", "");
        return flag;
    }

        public static String getFlag(){
            return flag;
        }

        public static void FlagInfo() throws IOException {
            Document doc = Jsoup.connect(info_url).get();
            Elements divContents= doc.select("div.content");
            Element content = divContents.select("table").get(2);
            Elements dataRows = content.select("tr");
            List<Map<String, String>> data = new ArrayList<>();
            for (int r = 1; r < dataRows.size(); r++) {
                Elements columns= dataRows.get(r).select("td");
                flag_meaning.put(columns.get(1).text(),columns.get(2).text());
            }
        }

    public static String getFlagInfo(){
        return flag_meaning.get(flag);
    }
        public static void main(String[] args) throws IOException {
            Flags.getFlagInfo();
        }
    }

