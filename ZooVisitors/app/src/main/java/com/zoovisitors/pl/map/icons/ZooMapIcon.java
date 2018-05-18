package com.zoovisitors.pl.map.icons;

import android.graphics.Color;
import android.widget.ImageView;

import com.zoovisitors.pl.map.MapView;

public class ZooMapIcon extends ImageIcon {
    private final String ZOO_MAP = "zoo_map";

    public ZooMapIcon(MapView mapView, Object[] additionalData, int left, int top) {
        super(mapView, additionalData, left, top, true);
    }
    @Override
    protected void doOnPost(boolean isVisible) {
        this.left = this.left + width/2;
        this.top = this.top + height/2;
        mapView.SetInitialParameters(width, height);
        super.doOnPost(isVisible);
    }

    @Override
    void setView() {
        ImageView imageView = new ImageView(mapView.getContext());
        int resourceId = mapView.getResources().getIdentifier(ZOO_MAP, "mipmap", mapView.getContext()
                .getPackageName());
        imageView.setImageResource(resourceId);
        imageView.setBackgroundColor(Color.TRANSPARENT);
        this.view = imageView;

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
