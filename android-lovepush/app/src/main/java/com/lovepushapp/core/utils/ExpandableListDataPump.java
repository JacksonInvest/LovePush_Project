package com.lovepushapp.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> one = new ArrayList<String>();
        one.add("Go Live");
        one.add("Watch Live Stream");


        List<String> sec = new ArrayList<String>();
        sec.add("Post Ad");
        sec.add("Ad Feed");
        sec.add("My Ads");
        List<String> third = new ArrayList<String>();
        third.add("Personality Test");
        third.add("Recommended Profiles");



        List<String> four = new ArrayList<String>();
        four.add("Affiliate Overview");
        four.add("My Team");
        four.add("Commissions");
        four.add("Withdrawal");
        four.add("Tips & Media");



        List<String> five = new ArrayList<String>();
        five.add("Match List");
        five.add("Match Requests");

        List<String> emptylist = new ArrayList<String>();

        expandableListDetail.put("Live Stream", one);
        expandableListDetail.put("Ads", sec);
        expandableListDetail.put("Personal Matching", third);
        expandableListDetail.put("Around", emptylist);
        expandableListDetail.put("Affiliate Center", four);
        expandableListDetail.put("My Matches", five);
        expandableListDetail.put("Shop", emptylist);
        expandableListDetail.put("Settings", emptylist);
        expandableListDetail.put("User Tips", emptylist);
        expandableListDetail.put("FAQ",emptylist);
        expandableListDetail.put("Logout",emptylist);
        return expandableListDetail;
    }

    public static HashMap<String, List<String>> getDataGerman() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> one = new ArrayList<String>();
        one.add("Go Live");
        one.add("Watch Live Stream");


        List<String> sec = new ArrayList<String>();
        sec.add("Postanzeige");
        sec.add("Anzeigen-Feed");
        sec.add("Meine Anzeigen");
        List<String> third = new ArrayList<String>();
        third.add("Persönlichkeitstest");
        third.add("Empfohlene Profile");



        List<String> four = new ArrayList<String>();
        four.add("Partnerübersicht");
        four.add("Mein Team");
        four.add("Provisionen");
        four.add("Rückzug");
        four.add("Tipps & Medien");

        List<String> five = new ArrayList<String>();
        five.add("Übereinstimmungsliste");
        five.add("Match Anfrage");

        List<String> emptylist = new ArrayList<String>();

        expandableListDetail.put("Live Stream", one);
        expandableListDetail.put("Anzeigen", sec);
        expandableListDetail.put("Persönliches Matching", third);
        expandableListDetail.put("Um", emptylist);
        expandableListDetail.put("Partnerzentrum", four);
        expandableListDetail.put("Meine Streichhölzer", five);
        expandableListDetail.put("Geschäft", emptylist);
        expandableListDetail.put("Die Einstellungen", emptylist);
        expandableListDetail.put("Benutzertipps hinzufügen", emptylist);//user tips
        expandableListDetail.put("Häufig gestellte Frage",emptylist);//FAQ
        expandableListDetail.put("Ausloggen",emptylist);//logout
        return expandableListDetail;
    }
}
