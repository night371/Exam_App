package com.example.exam;

import android.annotation.SuppressLint;
import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.example.exam.Interface.MyCompleteListener;
import com.example.exam.Models.CategoryModel;
import com.example.exam.Models.ProfileModel;
import com.example.exam.Models.QuestionsModel;
import com.example.exam.Models.RankModel;
import com.example.exam.Models.TestModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbQuery {
    @SuppressLint("StaticFieldLeak")
    public static FirebaseFirestore db;
    public static int g_selected_cat_index;
    public static List<CategoryModel> g_catlist = new ArrayList<>();
    public static List<TestModel> g_testList = new ArrayList<>();
    public static ProfileModel myProfile = new ProfileModel("n/a", null, null, 0);
    public static int g_selected_test_index;
    public static List<QuestionsModel> q_quesList = new ArrayList<>();
    public static List<String> g_BmIdList = new ArrayList<>();
    public static final int NOT_VISITED = 0;
    public static final int ANSWERED = 1;
    public static final int NOT_ANSWERED = 2;
    public static final int REVIEW = 3;
    public static RankModel rankModel = new RankModel(-1, 0, "null");
    public static List<RankModel> g_userList = new ArrayList<>();
    public static int user_count = 0;
    public static boolean amIonTop20 = false;
    public static List<QuestionsModel> g_bookmarks_list = new ArrayList<>();
    public static int temp = 0;


    public static void loadBookMarks(MyCompleteListener completeListener) {
        g_bookmarks_list.clear();
        if (g_BmIdList.size() == 0)
            completeListener.OnSuccess();

        for (int i = 0; i < g_BmIdList.size(); i++) {
            String docId = g_BmIdList.get(i);
            db.collection("Questions").document(docId).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                g_bookmarks_list.add(new QuestionsModel(
                                        documentSnapshot.getId(),
                                        documentSnapshot.getString("QUESTION"),
                                        documentSnapshot.getString("A"),
                                        documentSnapshot.getString("B"),
                                        documentSnapshot.getString("C"),
                                        documentSnapshot.getString("D"),
                                        documentSnapshot.getLong("ANSWER").intValue(),
                                        -1,
                                        0,
                                        false
                                ));


                            }
                            temp++;

                            if (temp == g_BmIdList.size()) {
                                completeListener.OnSuccess();
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            completeListener.OnFailure();
                        }
                    });


        }


    }

    public static void loadBmIds(MyCompleteListener completeListener) {
        g_BmIdList.clear();
        db.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("BOOKMARK")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int count = myProfile.getBookMarkCount();
                        for (int i = 0; i < count; i++) {
                            String bmId = documentSnapshot.getString("BM" + String.valueOf(i + 1) + "_ID");
                            g_BmIdList.add(bmId);

                        }
                        completeListener.OnSuccess();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();
                    }
                });

    }

    public static void getTopScores(MyCompleteListener completeListener) {
        g_userList.clear();
        String myuserId = FirebaseAuth.getInstance().getUid();
        db.collection("USERS").whereGreaterThan("TOTAL_SCORE", 0)
                .orderBy("TOTAL_SCORE", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int rank = 1;
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            g_userList.add(new RankModel(
                                    rank,
                                    doc.getLong("TOTAL_SCORE").intValue(),
                                    doc.getString("NAME")
                            ));

                            if (myuserId.compareTo(doc.getId()) == 0) {
                                amIonTop20 = true;
                                rankModel.setRank(rank);
                            }

                            rank++;

                        }
                        completeListener.OnSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();
                    }
                });


    }

    public static void getUsersCount(MyCompleteListener completeListener) {
        db.collection("USERS").document("TOTAL_USERS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user_count = documentSnapshot.getLong("COUNT").intValue();
                        completeListener.OnSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();
                    }
                });

    }

    public static void saveMyProfileData(String name, String phone, MyCompleteListener completeListener) {
        Map<String, Object> objectMap = new ArrayMap<>();
        objectMap.put("NAME", name);
        if (phone != null)
            objectMap.put("PHONE", phone);

        db.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .update(objectMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        myProfile.setName(name);
                        if (phone != null)
                            myProfile.setPhoneNo(phone);
                        completeListener.OnSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();
                    }
                });


    }

    public static void loadMyscores(MyCompleteListener completeListener) {
        db.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_SCORES")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for (int i = 0; i < g_testList.size(); i++) {
                            int top = 0;
                            if (documentSnapshot.get(g_testList.get(i).getId()) != null) {
                                top = documentSnapshot.getLong(g_testList.get(i).getId()).intValue();
                            }
                            g_testList.get(i).setTopScore(top);
                        }
                        completeListener.OnSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();
                    }
                });


    }

    public static void saveResult(int score, MyCompleteListener completeListener) {
        WriteBatch batch = db.batch();
        Map<String, Object> bmList = new ArrayMap<>();
        for (int i = 0; i < g_BmIdList.size(); i++) {
            bmList.put("BM" + String.valueOf(i + 1) + "_ID", g_BmIdList.get(i));
        }
        DocumentReference bmdoc = db.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("BOOKMARK");
        batch.set(bmdoc, bmList);


        DocumentReference userDoc = db.collection("USERS").document(FirebaseAuth.getInstance().getUid());
        Map<String, Object> userData = new ArrayMap<>();
        userData.put("TOTAL_SCORE", score);
        userData.put("BOOKMARK", g_BmIdList.size());
        batch.update(userDoc, userData);
        if (score > g_testList.get(g_selected_test_index).getTopScore()) {
            DocumentReference scoreDoc = userDoc.collection("USER_DATA").document("MY_SCORES");

            Map<String, Object> testData = new ArrayMap<>();
            testData.put(g_testList.get(g_selected_test_index).getId(), score);
            batch.set(scoreDoc, testData, SetOptions.merge());
        }
        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (score > g_testList.get(g_selected_test_index).getTopScore())
                            g_testList.get(g_selected_test_index).setTopScore(score);
                        rankModel.setScore(score);
                        completeListener.OnSuccess();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();
                    }
                });


    }

    public static void loadQuestions(MyCompleteListener completeListener) {
        q_quesList.clear();
        db.collection("Questions").
                whereEqualTo("CATEGORY", g_catlist.get(g_selected_cat_index).getDocId())
                .whereEqualTo("TEST", g_testList.get(g_selected_test_index).getId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            boolean ismarked = false;
                            if (g_BmIdList.contains(doc.getId()))
                                ismarked = true;
                            q_quesList.add(new QuestionsModel(
                                    doc.getId(),
                                    doc.getString("QUESTION"),
                                    doc.getString("A"),
                                    doc.getString("B"),
                                    doc.getString("C"),
                                    doc.getString("D"),
                                    doc.getLong("ANSWER").intValue(),
                                    -1,
                                    NOT_VISITED,
                                    ismarked
                            ));
                        }

                        completeListener.OnSuccess();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();
                    }
                });


    }

    public static void getUserData(MyCompleteListener completeListener) {
        db.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        myProfile.setName(documentSnapshot.getString("NAME"));
                        myProfile.setEmail(documentSnapshot.getString("EMAIL_ID"));
                        rankModel.setScore(documentSnapshot.getLong("TOTAL_SCORE").intValue());
                        rankModel.setName(documentSnapshot.getString("NAME"));
                        if (documentSnapshot.getString("PHONE") != null) {
                            myProfile.setPhoneNo(documentSnapshot.getString("PHONE"));
                        }
                        if (documentSnapshot.get("BOOKMARK") != null) {
                            myProfile.setBookMarkCount(documentSnapshot.getLong("BOOKMARK").intValue());
                        }
                        completeListener.OnSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();
                    }
                });
    }

    public static void loadCategoryData(MyCompleteListener completeListener) {
        g_catlist.clear();
        db.collection("QUIZES").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots
                        ) {
                            docList.put(doc.getId(), doc);
                        }
                        QueryDocumentSnapshot catListDocs = docList.get("Categories");
                        long catCount = catListDocs.getLong("COUNT");

                        for (int i = 1; i <= catCount; i++) {
                            String catId = catListDocs.getString("CAT"+String.valueOf(i)+"_ID");
                            QueryDocumentSnapshot catDoc = docList.get(catId);
                            int noOFTest = catDoc.getLong("NO_OF_TESTS").intValue();
                            String name = catDoc.getString("NAME");
                            g_catlist.add(new CategoryModel(catId, name, noOFTest));
                        }
                        completeListener.OnSuccess();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();

                    }
                });

    }

    public static void loadModelTestData(MyCompleteListener completeListener) {
        g_testList.clear();
        db.collection("QUIZES").document(g_catlist.get(g_selected_cat_index).getDocId())
                .collection("TESTS_LIST").document("TESTS_INFO")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int noOFTests = g_catlist.get(g_selected_cat_index).getNumOfTests();
                        for (int i = 1; i <= noOFTests; i++) {
                            g_testList.add(new TestModel(
                                    documentSnapshot.getString("TEST" + String.valueOf(i) + "_ID"),
                                    0,
                                    documentSnapshot.getLong("TEST" + String.valueOf(i) + "_TIME").intValue()

                            ));
                        }


                        completeListener.OnSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.OnFailure();
                    }
                });


    }
    public static void loadData(MyCompleteListener completeListener) {

        loadCategoryData(new MyCompleteListener() {
            @Override
            public void OnSuccess() {
                getUserData(new MyCompleteListener() {
                    @Override
                    public void OnSuccess() {
                        getUsersCount(new MyCompleteListener() {
                            @Override
                            public void OnSuccess() {
                                loadBmIds(completeListener);
                            }

                            @Override
                            public void OnFailure() {
                                completeListener.OnFailure();
                            }
                        });
                    }

                    @Override
                    public void OnFailure() {
                        completeListener.OnFailure();
                    }
                });
            }

            @Override
            public void OnFailure() {
                completeListener.OnFailure();
            }
        });


    }


}




