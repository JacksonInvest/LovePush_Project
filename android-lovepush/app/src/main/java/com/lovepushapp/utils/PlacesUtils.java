package com.lovepushapp.utils;

import android.content.Context;
import android.content.Intent;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.LocationRestriction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.lovepushapp.R;

import org.jivesoftware.smack.filter.FlexibleStanzaTypeFilter;

import java.util.Arrays;
import java.util.List;

public class PlacesUtils {

    public static Intent getPlacesIntent(Context context) {
        if (!Places.isInitialized()) {
            Places.initialize(context, context.getString(R.string.google_places_api_key));
        }

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        return new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(context);
    }


    public static Intent getPlacesIntent(Context context, AutocompleteActivityMode autocompleteLayoutMode) {

        if (!Places.isInitialized()) {
            Places.initialize(context, context.getString(R.string.google_places_api_key));
        }

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        return new Autocomplete.IntentBuilder(
                autocompleteLayoutMode, fields)
                .build(context);
    }


    public static Place parsePlacesData(Intent data) {
        return Autocomplete.getPlaceFromIntent(data);
    }
}