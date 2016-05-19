package com.example.sony.citybusmanagement;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RotaTask extends AsyncTask<Void, Integer, Boolean> {

    private static final String TOAST_MSG = "Calculating";
    private static final String TOAST_ERR_MAJ = "Impossible to trace Itinerary";

    private Context context;
    private GoogleMap gMap;
    private String editFrom;
    private String editTo;
    private final ArrayList<LatLng> lstLatLng = new ArrayList<LatLng>();
    String l1,l2,l3,l4;
    public RotaTask(final Context context, final GoogleMap gMap, final String editFrom, final String editTo,String l1,String l2,String l3,String l4) {
        this.context = context;
        this.gMap= gMap;
        this.editFrom = editFrom;
        this.editTo = editTo;
        this.l1=l1;
        this.l2=l2;
        this.l3=l3;
        this.l4=l4;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPreExecute() {
        Toast.makeText(context, TOAST_MSG, Toast.LENGTH_LONG).show();
    }

    /***
     * {@inheritDoc}
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            Log.i("R l1:", "[" +l1+"]");
            Log.i("R l2:", "[" +l2+"]");
            Log.i("R l3:", "[" +l3+"]");
            Log.i("R l4:", "[" +l4+"]");

            LatLng point1 = new LatLng(Double.parseDouble(l1),Double.parseDouble(l2));
            LatLng point2 = new LatLng(Double.parseDouble(l3),Double.parseDouble(l4));

            final StringBuilder url = new StringBuilder("http://maps.googleapis.com/maps/api/directions/xml?sensor=false&language=pt");
            url.append("&origin=");
            url.append(point1);
            url.append("&destination=");
            url.append(point2);

            final InputStream stream = new URL(url.toString()).openStream();

            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setIgnoringComments(true);

            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            final Document document = documentBuilder.parse(stream);
            document.getDocumentElement().normalize();

            final String status = document.getElementsByTagName("status").item(0).getTextContent();
            if(!"OK".equals(status)) {
                return false;
            }

            final Element elementLeg = (Element) document.getElementsByTagName("leg").item(0);
            final NodeList nodeListStep = elementLeg.getElementsByTagName("step");
            final int length = nodeListStep.getLength();

            for(int i=0; i<length; i++) {
                final Node nodeStep = nodeListStep.item(i);

                if(nodeStep.getNodeType() == Node.ELEMENT_NODE) {
                    final Element elementStep = (Element) nodeStep;
                    decodePolylines(elementStep.getElementsByTagName("points").item(0).getTextContent());
                }
            }

            return true;
        }
        catch(final Exception e) {
            return false;
        }
    }

    private void decodePolylines(final String encodedPoints) {
        int index = 0;
        int lat = 0, lng = 0;

        while (index < encodedPoints.length()) {
            int b, shift = 0, result = 0;

            do {
                b = encodedPoints.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;

            do {
                b = encodedPoints.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            lstLatLng.add(new LatLng((double)lat/1E5, (double)lng/1E5));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(final Boolean result) {
        if(!result) {
            Log.i("Rotatask", "[" + result + "]");
            Toast.makeText(context, TOAST_ERR_MAJ, Toast.LENGTH_SHORT).show();
        }
        else {
            final PolylineOptions polylines = new PolylineOptions();
            polylines.color(Color.BLUE);

            for(final LatLng latLng : lstLatLng) {
                polylines.add(latLng);
            }
            Log.i("successTask", "[" + result + "]");
//

            final MarkerOptions markerA = new MarkerOptions();
            markerA.position(lstLatLng.get(0));
            gMap.addMarker(new MarkerOptions().position(lstLatLng.get(0)).title(editFrom.toString()));
            markerA.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            final MarkerOptions markerB = new MarkerOptions();
            markerB.position(lstLatLng.get(lstLatLng.size() - 1));
            gMap.addMarker(new MarkerOptions().position(lstLatLng.get(lstLatLng.size() - 1)).title(editTo.toString()));
            markerB.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//            LatLng ahmedabad = new LatLng(23.0300, 72.5800);
//            gMap.addMarker(new MarkerOptions().position(ahmedabad).title("Ahmedabad"));

            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.0300, 72.5800), 14.0f));

            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lstLatLng.get(0), 10));
            gMap.addMarker(markerA);
            gMap.addPolyline(polylines);
            gMap.addMarker(markerB);
            gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            gMap.setIndoorEnabled(true);
            gMap.getUiSettings().setCompassEnabled(true);
            gMap.getUiSettings().setRotateGesturesEnabled(true);
            gMap.setMyLocationEnabled(true);

        }
    }
}