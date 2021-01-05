package com.example;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.Map;

@Controller
public class ApplicationController {

    private final Firestore firestore;

    public ApplicationController() {
        final FirebaseApp firebaseApp = FirebaseApp.initializeApp();
        firestore = FirestoreClient.getFirestore(firebaseApp);
    }

    @Get("/")
    public String root() throws Exception {
        final DocumentReference counterRef =
                firestore.collection("counters").document("happyNewYear");

        long counterValue = firestore.runTransaction(transaction -> {
            final DocumentSnapshot counterSnapshot = transaction.get(counterRef).get();

            final long value =
                    !counterSnapshot.exists() || counterSnapshot.getLong("value") == null ?
                            0 : counterSnapshot.getLong("value");
            transaction.set(counterRef, Map.of("value", value + 1));
            return value;
        }).get();

        return "Happy new year! " + counterValue;
    }

}
