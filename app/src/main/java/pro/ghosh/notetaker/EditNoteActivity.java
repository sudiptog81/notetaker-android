package pro.ghosh.notetaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.List;

public class EditNoteActivity extends AppCompatActivity {
  private MaterialToolbar appBar;
  private NoteDatabase notesDatabase;
  private Note note;
  private String noteId;
  EditText noteTitle, noteContent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_note);
    appBar = (MaterialToolbar) findViewById(R.id.appbar_top_edit_note);
    noteTitle = (EditText) findViewById(R.id.textfield_edit_note_title);
    noteContent = (EditText) findViewById(R.id.textfield_edit_note_content);
    setSupportActionBar(appBar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_24dp);
    noteId = getIntent().getStringExtra("id");
    notesDatabase = new NoteDatabase(this);
    notesDatabase.getNote(noteId, new DataCallback() {
      @Override
      public void onCallback(List<Note> allNotesList) {
        note = allNotesList.get(0);
        noteTitle.setText(note.getTitle());
        noteContent.setText(note.getContent());
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.top_app_bar_menu_edit_note, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
      case R.id.menuitem_save_edited:
        if (noteTitle.getText().toString().isEmpty()
          && noteContent.getText().toString().isEmpty())
          Toast.makeText(this, "Empty Note", Toast.LENGTH_SHORT).show();
        else if (noteTitle.getText().toString().isEmpty())
          noteTitle.setError("Empty Title");
        else if (noteContent.getText().toString().isEmpty())
          noteContent.setError("Empty Content");
        else {
          Note note = new Note(
            noteTitle.getText().toString().trim(),
            noteContent.getText().toString(),
            new Timestamp(new Date(System.currentTimeMillis()))
          );
          notesDatabase.updateNote(noteId, note, new DataCallback() {
            @Override
            public void onCallback(List<Note> allNotesList) {
              if (allNotesList == null)
                Toast.makeText(
                  EditNoteActivity.this,
                  "Could Not Save Note",
                  Toast.LENGTH_SHORT
                ).show();
            }
          });
          onBackPressed();
        }
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

  @Override
  protected void onResume() {
    super.onResume();
    notesDatabase.getNote(noteId, new DataCallback() {
      @Override
      public void onCallback(List<Note> allNotesList) {
        note = allNotesList.get(0);
        noteTitle.setText(note.getTitle());
        noteContent.setText(note.getContent());
      }
    });
  }
}
