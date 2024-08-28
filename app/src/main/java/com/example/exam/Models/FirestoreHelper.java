package com.example.exam.Models;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class FirestoreHelper {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String newCatId;

    public void insertQuizAndCategory(String name, int noOfTests, CompleteListener completeListener) {
        // Get a reference to the "Categories" document
        DocumentReference categoriesRef = db.collection("QUIZES").document("Categories");

        // Update the "COUNT" field
        categoriesRef.update("COUNT", FieldValue.increment(1))
                .addOnSuccessListener(aVoid -> {
                    // Get the current "COUNT" value
                    categoriesRef.get()
                            .addOnSuccessListener(documentSnapshot -> {
                                long currentCount = documentSnapshot.getLong("COUNT");
                                newCatId = "CAT" + currentCount + "_ID";

                                // Create a map with the new category data
                                Map<String, Object> newCategoryData = new HashMap<>();
                                newCategoryData.put("NAME", name);
                                newCategoryData.put("NO_OF_TESTS", noOfTests);

                                // Add the new category document
                                db.collection("QUIZES").document(newCatId)
                                        .set(newCategoryData)
                                        .addOnSuccessListener(aVoid1 -> {
                                            // Update the "Categories" document with the new category ID
                                            categoriesRef.update(newCatId, newCatId)
                                                    .addOnSuccessListener(aVoid2 -> {
                                                        // Create a map with the new quiz data
                                                        Map<String, Object> newQuizData = new HashMap<>();
                                                        newQuizData.put("CAT_ID", newCatId); // Use the auto-generated category ID
                                                        newQuizData.put("NAME", name);
                                                        newQuizData.put("NO_OF_TESTS", noOfTests);

                                                        // Add the new quiz document
                                                        db.collection("QUIZES").document() // Auto-generate document ID
                                                                .set(newQuizData)
                                                                .addOnSuccessListener(aVoid3 -> {
                                                                    completeListener.OnSuccess();
                                                                })
                                                                .addOnFailureListener(e -> completeListener.OnFailure());
                                                    })
                                                    .addOnFailureListener(e -> completeListener.OnFailure());
                                        })
                                        .addOnFailureListener(e -> completeListener.OnFailure());
                            })
                            .addOnFailureListener(e -> completeListener.OnFailure());
                })
                .addOnFailureListener(e -> completeListener.OnFailure());
    }

    // ... other methods

    public interface CompleteListener {
        void OnSuccess();
        void OnFailure();
    }
}