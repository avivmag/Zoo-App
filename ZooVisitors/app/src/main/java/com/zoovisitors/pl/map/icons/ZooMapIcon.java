package com.zoovisitors.pl.map.icons;

import android.widget.ImageView;

import com.zoovisitors.pl.map.MapView;

public class ZooMapIcon extends ImageIcon {
    private final String ZOO_MAP = "zoo_map";

    public ZooMapIcon(MapView mapView, Object[] additionalData, int left, int top) {
        super(mapView, additionalData, left, top, true, true);
    }

    @Override
    void setView() {
        ImageView view = new ImageView(mapView.getContext());
        int resourceId = mapView.getResources().getIdentifier(ZOO_MAP, "mipmap", mapView.getContext()
                .getPackageName());
        view.setImageResource(resourceId);
        this.view = view;


        //TODO: get image from server
//                GlobalVariables.bl.getImage("assets/map/zoo_map.jpg", 5000, 5000, new
// GetObjectInterface() {
//                    @Override
//                    public void onSuccess(Object response) {
//                        view.setImageBitmap((Bitmap) response);
//                    }
//
//                    @Override
//                    public void onFailure(Object response) {
//
//                    }
//                });

    }
}
