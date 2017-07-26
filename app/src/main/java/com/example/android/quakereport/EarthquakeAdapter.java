package com.example.android.quakereport;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by xianwei li on 7/21/2017.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOCATION_SEPARATOR = "of";

    public EarthquakeAdapter(EarthquakeActivity earthquakeActivity, ArrayList<Earthquake> earthquakes) {
        super(earthquakeActivity, 0, earthquakes);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }

        Earthquake currentEarthquake = getItem(position);

        TextView magnitudeView = (TextView) convertView.findViewById(R.id.magnitude);
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMag());
        magnitudeCircle.setColor(magnitudeColor);

        String formattedMag = formatMag(currentEarthquake.getMag());
        magnitudeView.setText(formattedMag);

        TextView distanceView = (TextView) convertView.findViewById(R.id.location_distance);
        String offset = locationOffset(currentEarthquake.getLocation());
        distanceView.setText(offset);

        TextView cityView = (TextView) convertView.findViewById(R.id.location_city);
        String city = locationCity(currentEarthquake.getLocation());
        cityView.setText(city);

        Date dateObject = new Date(currentEarthquake.getTimeInMilliseconds());

        TextView dateView = (TextView) convertView.findViewById(R.id.date);
        String formattedDate = formatDate(dateObject);
        dateView.setText(formattedDate);

        TextView timeView = (TextView) convertView.findViewById(R.id.time);
        String formattedTime = formatTime(dateObject);
        timeView.setText(formattedTime);

        return convertView;
    }

    private int getMagnitudeColor(double mag) {
        int magFloor = (int) Math.floor(mag);
        int magColorId;
        switch (magFloor) {
            case 0:
            case 1:
                magColorId = R.color.magnitude1;
                break;
            case 2:
                magColorId = R.color.magnitude2;
                break;
            case 3:
                magColorId = R.color.magnitude3;
                break;
            case 4:
                magColorId = R.color.magnitude4;
                break;
            case 5:
                magColorId = R.color.magnitude5;
                break;
            case 6:
                magColorId = R.color.magnitude6;
                break;
            case 7:
                magColorId = R.color.magnitude7;
                break;
            case 8:
                magColorId = R.color.magnitude8;
                break;
            case 9:
                magColorId = R.color.magnitude9;
                break;
            default:
                magColorId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magColorId);
    }

    private String formatMag(double mag) {
        DecimalFormat magFormat = new DecimalFormat("0.0");
        return magFormat.format(mag);
    }

    private String locationCity(String location) {
        if(location.contains(LOCATION_SEPARATOR)) {
            String[] str = location.split(LOCATION_SEPARATOR);
            return str[1];
        } else {
            return location;
        }
    }

    private String locationOffset(String location) {
        if(location.contains(LOCATION_SEPARATOR)) {
            String[] str = location.split(LOCATION_SEPARATOR);
            return str[0] + LOCATION_SEPARATOR;
        } else {
            return "Near the";
        }
    }


    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm: a");
        return timeFormat.format(dateObject);
    }
}
