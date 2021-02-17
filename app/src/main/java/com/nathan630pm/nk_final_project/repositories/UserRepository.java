package com.nathan630pm.nk_final_project.repositories;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Ignore;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nathan630pm.nk_final_project.models.User;

public class UserRepository {
    private static final String TAG = "UserRepository";
    private final String COLLECTION_NAME = "Parking";
    private final FirebaseFirestore db;
    private final FirebaseAuth mAuth;

    private User tempUser;

    public MutableLiveData<String> signInStatus = new MutableLiveData<String>();
    public MutableLiveData<String> loggedInUserID = new MutableLiveData<String>();
    public MutableLiveData<User> userObject = new MutableLiveData<User>();

    private String userID;

    public UserRepository() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        tempUser = new User();
    }

    public void logout(){
        loggedInUserID.setValue("");


    }



    public void addUser(String email, String password, User user) {
        try {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()) {
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();

                                db.collection(COLLECTION_NAME)
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "Document added with ID of: " + documentReference.getId());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "Failed to add document to the store: " + e);
                                            }
                                        });
                            }
                            else {
                                Log.e(TAG, "Error creating user.");
                                Log.e(TAG, task.getException().toString());

                            }
                        }
                    });
        }catch (Exception ex) {
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }

    public void savedUserGetUser(String email) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            signInStatus.postValue("SUCCESS_SAVE");
                            loggedInUserID.postValue(task.getResult().getDocuments().get(0).getId());
                            userID = task.getResult().getDocuments().get(0).getId();
                            getUserObject();

                            Log.d(TAG, "Logged in user document ID: " + loggedInUserID);
                        }
                        else {
                            signInStatus.postValue("FAILURE");
                        }
                    }
                });
    }

    public void getUser(String email, final String password) {
        this.signInStatus.postValue("LOADING");
        try {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                db.collection(COLLECTION_NAME)
                                        .whereEqualTo("email", email)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()){
                                                    signInStatus.postValue("SUCCESS");
                                                    loggedInUserID.postValue(task.getResult().getDocuments().get(0).getId());
                                                    userID = task.getResult().getDocuments().get(0).getId();
                                                    getUserObject();

                                                    Log.d(TAG, "Logged in user document ID: " + loggedInUserID);
                                                }
                                                else {
                                                    signInStatus.postValue("FAILURE");
                                                }
                                            }
                                        });
                            }
                            else {
                                signInStatus.postValue("FAILURE");
                            }
                        }
                    });


//            db.collection(COLLECTION_NAME)
//                    .whereEqualTo("email", email)
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if(task.isSuccessful()){
//                                for(QueryDocumentSnapshot document:task.getResult()) {
//                                    Log.d(TAG, "Found document with ID: " + document.getId() + "\nDocument Data:\n" + document.getData());
//                                    if(document.toObject(User.class).getPassword().equals(password)){
//                                        Log.d(TAG, "Successful login.");
//                                        signInStatus.postValue("SUCCESS");
//
//                                        //get the ID of currently logged in user
//                                        loggedInUserID.postValue(task.getResult().getDocuments().get(0).getId());
//                                        Log.d(TAG, "Logged in user document ID: " + loggedInUserID);
//                                    }
//                                    else {
//                                        Log.d(TAG, "Unsuccessful login.");
//                                        signInStatus.postValue("FAILURE");
//                                    }
//                                }
//                            }else {
//                                Log.e(TAG, "Error fetching document: " + task.getException());
//                                signInStatus.postValue("FAILURE");
//                            }
//                        }
//                    });

        }catch (Exception ex){
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
            signInStatus.postValue("FAILURE");
        }
    }


    public void makeSureInfoUpToDate() {
        db.collection(COLLECTION_NAME)
                .document(userID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        tempUser.setName(documentSnapshot.getString("name"));
                        tempUser.setEmail(documentSnapshot.getString("email"));
                        tempUser.setCarPlateNumber(documentSnapshot.getString("carPlateNumber"));
                        tempUser.setContactNumber(documentSnapshot.getString("contactNumber"));
                        userObject.setValue(tempUser);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "ERROR getting collection/document: " + e.getLocalizedMessage());
                        Log.e(TAG, e.toString());
                    }
                });
    }


    public User getUserObject(){

        Log.e(TAG, "USER ID: " +  userID);

        db.collection(COLLECTION_NAME)
                .document(userID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        tempUser.setName(documentSnapshot.getString("name"));
                        tempUser.setEmail(documentSnapshot.getString("email"));
                        tempUser.setCarPlateNumber(documentSnapshot.getString("carPlateNumber"));
                        tempUser.setContactNumber(documentSnapshot.getString("contactNumber"));
                        userObject.setValue(tempUser);
                        Log.e(TAG, "Success getting user info");
                        Log.e(TAG, documentSnapshot.toString());
                        Log.e(TAG, "USER NAME: " +  documentSnapshot.getString("name"));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "ERROR getting collection/document: " + e.getLocalizedMessage());
                        Log.e(TAG, e.toString());
                    }
                });

        Log.e(TAG, "RETURNING USER: " + tempUser.getName());

        return tempUser;
    }

    public User returnUserObject(){
        return tempUser;
    }

    public void updateUser(String name, String email, String phone, String plateNo){
        User updateUser = new User(name, email, phone, plateNo);

        db.collection(COLLECTION_NAME)
                .document(loggedInUserID.getValue())
                .update("name", name, "email", email, "contactNumber", phone, "carPlateNumber", plateNo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });



    }

    public void deleteUser(String email, String password) {
        final FirebaseUser userToDelete = mAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);
        userToDelete.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        userToDelete.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User account deleted.");
                                            logout();
                                        }
                                    }
                                });

                    }
                });
    }

    public void checkForActiveUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

        }
    }


}
