package pro.ghosh.notetaker;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class NoteDatabase {
  FirebaseFirestore db;

  NoteDatabase(Activity activity) {
    db = FirebaseFirestore.getInstance();
  }

  void getNote(final String id, final DataCallback callback) {
    final List<Note> allNotes = new ArrayList<>();
    db.collection("notes")
      .document(id)
      .get()
      .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
          if (task.isSuccessful()) {
            DocumentSnapshot doc = task.getResult();
            allNotes.add(
              new Note(
                id,
                doc.getString("title"),
                doc.getString("content"),
                doc.getTimestamp("timestamp"))
            );
            callback.onCallback(allNotes);
          }
        }
      });
  }

  void getAllNotes(String uid, final DataCallback callback) {
    final List<Note> allNotes = new ArrayList<>();
    db.collection("notes")
      .orderBy("timestamp", Query.Direction.DESCENDING)
      .whereEqualTo("uid", uid)
      .get()
      .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
          if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : task.getResult()) {
              allNotes.add(
                new Note(
                  doc.getId(),
                  doc.getString("title"),
                  doc.getString("content"),
                  doc.getTimestamp("timestamp"))
              );
            }
            callback.onCallback(allNotes);
          }
        }
      });
  }

  void deleteNote(String noteId, final DataCallback callback) {
    db.collection("notes")
      .document(noteId)
      .delete()
      .addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
          callback.onCallback(new ArrayList<Note>());
        }
      });
  }

  void createNote(Note note, final DataCallback callback) {
    Map<String, Object> noteObj = new HashMap<>();
    noteObj.put("title", note.getTitle());
    noteObj.put("content", note.getContent());
    noteObj.put("timestamp", note.getTime());
    noteObj.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());

    db.collection("notes")
      .document()
      .set(noteObj)
      .addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          callback.onCallback(new ArrayList<Note>());
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          callback.onCallback(null);
        }
      });
  }

  void updateNote(String id, Note note, final DataCallback callback) {
    Map<String, Object> noteObj = new HashMap<>();
    noteObj.put("title", note.getTitle());
    noteObj.put("content", note.getContent());

    db.collection("notes")
      .document(id)
      .update(noteObj)
      .addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void v) {
          callback.onCallback(new ArrayList<Note>());
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          callback.onCallback(null);
        }
      });
  }
}
