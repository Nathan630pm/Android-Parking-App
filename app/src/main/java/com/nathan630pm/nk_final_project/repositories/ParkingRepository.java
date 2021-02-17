package com.nathan630pm.nk_final_project.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.nathan630pm.nk_final_project.models.Parking;
import com.nathan630pm.nk_final_project.models.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Created By: Nathan Kennedy, Student ID: 101333351

public class ParkingRepository {
    private static final String TAG = "ParkingRepository";
    private FirebaseFirestore db;
    private final String COLLECTION_NAME = "AddedParking";
    private final String COLLECTION_PARKING_LIST = "ParkingList";
    private boolean returningValue;

    public MutableLiveData<List<Parking>> parkingList = new MutableLiveData<List<Parking>>();

    public MutableLiveData<Parking> singleParking = new MutableLiveData<Parking>();

    public MutableLiveData<Boolean> uploadSuccess = new MutableLiveData<Boolean>();

    public ParkingRepository() {this.db = FirebaseFirestore.getInstance();}

    public boolean addParking(String userEmail, Parking parking) {


        db.collection(COLLECTION_NAME)
                .document(userEmail)
                .collection(COLLECTION_PARKING_LIST)
                .add(parking)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: ADDED TO DATABASE.");
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("id", FieldValue.delete());

                        documentReference.update(updates);

                        uploadSuccess.setValue(true);

                        returningValue = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: FAILED TO ADD TO DATABASE");
                        returningValue = false;

                        uploadSuccess.setValue(true);
                    }
                });

        return returningValue;

    }

    public void getParkingByID(String userEmail, String id) {
        try {
            db.collection(COLLECTION_NAME)
                    .document(userEmail)
                    .collection(COLLECTION_PARKING_LIST)
                    .document(id)
                    .get()

                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Parking tempParking = documentSnapshot.toObject(Parking.class);
                            singleParking.setValue(tempParking);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Failed to get document: " + e.toString());
                        }
                    });
        } catch (Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
            Log.e(TAG, ex.toString());
        }
    }

    public boolean getAllParkingItems(String userEmail) {
        try {
            db.collection(COLLECTION_NAME)
                    .document(userEmail)
                    .collection(COLLECTION_PARKING_LIST)
                    .orderBy("date", Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                            if(error != null){
                                Log.e(TAG, "Listening to parking list changes failed: " + error);
                                return;
                            }

                            List<Parking> tempParkingList = new ArrayList<>();
                            if(parkingList.getValue() != null){
                                tempParkingList = parkingList.getValue();
                            }




                            if(snapshot != null) {
                                Log.d(TAG, "Current data: " + snapshot.getDocumentChanges());

                                for(DocumentChange documentChange:snapshot.getDocumentChanges()) {
                                    Parking parking = documentChange.getDocument().toObject(Parking.class);
                                    parking.setId(documentChange.getDocument().getId());
                                    switch (documentChange.getType()){
                                        case ADDED:
                                            tempParkingList.add(parking);

                                            break;
                                        case MODIFIED:

                                            break;
                                        case REMOVED:

                                            break;
                                    }
                                }

                                Log.e(TAG, tempParkingList.toString());
                                parkingList.postValue(tempParkingList);
                            }
                            else {
                                Log.e(TAG, "no changes in parking");
                            }
                        }
                    });
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
            Log.e(TAG, ex.toString());
            return false;
        }
        return true;
    }


    public void deleteParking(User user, Parking parking) {
        db.collection(COLLECTION_NAME)
                .document(user.getEmail())
                .collection(COLLECTION_PARKING_LIST)
                .document(parking.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document deleted successfully.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to delete document. Please try again later.");
                    }
                });
    }





}
