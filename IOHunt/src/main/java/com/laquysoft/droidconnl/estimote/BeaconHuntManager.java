package com.laquysoft.droidconnl.estimote;

import android.content.Context;

import com.estimote.sdk.Nearable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeaconHuntManager {

    private Listener listener;

    private com.estimote.sdk.BeaconManager beaconManager;
    private String scanId;

    private Map<NearableID, Boolean> nearablesMotionStatus = new HashMap<>();
    private Map<NearableID, Double > nearablesTempStatus = new HashMap<>();

    public BeaconHuntManager(Context context, final Map<NearableID, BeaconClue> products) {
        beaconManager = new com.estimote.sdk.BeaconManager(context);
        beaconManager.setNearableListener(new com.estimote.sdk.BeaconManager.NearableListener() {
            @Override
            public void onNearablesDiscovered(List<Nearable> list) {
                for (Nearable nearable : list) {
                    NearableID nearableID = new NearableID(nearable.identifier);
                    if (!products.keySet().contains(nearableID)) {
                        continue;
                    }


                    double previousTempStatus = 0;
                    if (nearablesTempStatus.containsKey(nearableID)) {
                        previousTempStatus = nearablesTempStatus.get(nearableID);
                    }
                    BeaconClue beaconClue = products.get(nearableID);
                    if (previousTempStatus < nearable.temperature) {
                        listener.onBeaconClueHeatOn(beaconClue);
                    } else {
                        listener.onBeaconClueCoolDown(beaconClue);
                    }
                    nearablesTempStatus.put(nearableID, nearable.temperature);

                    boolean previousStatus = nearablesMotionStatus.containsKey(nearableID) && nearablesMotionStatus.get(nearableID);
                    if (previousStatus != nearable.isMoving) {
                        if (nearable.isMoving) {
                            listener.onBeaconCluePickup(beaconClue);
                        } else {
                            listener.onBeaconCluePutdown(beaconClue);
                        }
                        nearablesMotionStatus.put(nearableID, nearable.isMoving);
                    }
                }
            }
        });
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onBeaconCluePickup(BeaconClue beaconClue);

        void onBeaconCluePutdown(BeaconClue beaconClue);

        void onBeaconClueHeatOn(BeaconClue beaconClue);

        void onBeaconClueCoolDown(BeaconClue beaconClue);
    }


    public void startUpdates() {
        beaconManager.connect(new com.estimote.sdk.BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                scanId = beaconManager.startNearableDiscovery();
            }
        });
    }

    public void stopUpdates() {
        beaconManager.stopNearableDiscovery(scanId);
    }

    public void destroy() {
        beaconManager.disconnect();
    }
}
