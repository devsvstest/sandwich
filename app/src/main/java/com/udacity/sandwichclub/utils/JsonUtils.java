package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static String mainName;
    private static List<String> alsoKnownAs = null;
    private static String placeOfOrigin;
    private static String description;
    private static String image;
    private static List<String> ingredients = null;

    public static int extractString(String json, int ind, String stringIdentifier){

        String string = "";

        while( !json.substring(ind, ind+14).equals("\"alsoKnownAs\":") &&
                !json.substring(ind, ind+16).equals("\"placeOfOrigin\":") &&
                !json.substring(ind, ind+14).equals("\"description\":") &&
                !json.substring(ind, ind+8).equals("\"image\":") &&
                !json.substring(ind, ind+14).equals("\"ingredients\":") ){


            if( json.substring(ind, ind+2).equals("\\\"") ){
                string += "\"";
                ind = ind + 2;
            }
            else if( json.substring(ind, ind+1).equals("\"") ) {
                ind++;
            }
            else{
                string += json.substring(ind, ind + 1);
                ind++;
            }
        }

        string = string.substring(0, string.length() - 1);

        switch (stringIdentifier){

            case "mainName": mainName = string;
            break;

            case "placeOfOrigin": placeOfOrigin = string;
            break;

            case "description": description = string;
            break;

            case "image": image = string;
            break;

        }
        //Log.d("JsonUtils", string + "\n" );

        return ind;

    }

    public static int extractList(String json, int ind, String listIdentifier){

        List<String> list = new ArrayList<>();
        String tmp = "";

        while( !json.substring(ind, ind+1).equals("]") ){

            if( json.substring(ind, ind+1).equals("\"") ){
                ind ++;
            }
            else if( json.substring(ind, ind+1).equals(",") ){
                list.add(tmp);
                tmp = "";
                ind++;
            }
            else{
                tmp += json.substring(ind, ind+1);
                ind++;
            }

        }

        if( tmp.length() > 0 )
            list.add(tmp);

        if( listIdentifier.equals("alsoKnownAs") )
            alsoKnownAs = list;
        else
            ingredients = list;

        ind = ind++;
        return ind;

    }

    public static Sandwich parseSandwichJson(String json) {

        int i=0, len = json.length();

        while( i<len  ){

            if( i + 7 < len && json.substring(i, i + 8).equals("mainName") )
                i = extractString(json, i + 11, "mainName");
            else if( i + 10 < len && json.substring(i, i + 11 ).equals("alsoKnownAs") )
                i = extractList(json, i + 14, "alsoKnownAs");
            else if( i + 12 < len && json.substring(i, i + 13).equals("placeOfOrigin") )
                i = extractString(json, i + 16, "placeOfOrigin");
            else if( i + 10 < len && json.substring(i, i + 11).equals("description") )
                i = extractString(json, i + 14, "description");
            else if( i + 4 < len && json.substring(i, i + 5 ).equals("image") )
                i = extractString(json, i + 8, "image");
            else if( i + 10 < len && json.substring(i, i + 11 ).equals("ingredients") )
                i = extractList(json, i + 14, "ingredients");
            else i++;

        }

        return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);

    }

}
