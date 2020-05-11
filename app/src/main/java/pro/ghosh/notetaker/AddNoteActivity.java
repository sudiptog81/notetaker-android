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

public class AddNoteActivity extends AppCompatActivity {
  private MaterialToolbar appBar;
  private NoteDatabase notesDatabase;
  EditText noteTitle, noteContent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_note);
    appBar = (MaterialToolbar) findViewById(R.id.appbar_top_add_note);
    noteTitle = (EditText) findViewById(R.id.textfield_new_note_title);
    noteContent = (EditText) findViewById(R.id.textfield_new_note_content);
    setSupportActionBar(appBar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_24dp);
    notesDatabase = new NoteDatabase(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.top_app_bar_menu_new_note, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
      case R.id.menuitem_save_new:
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
          notesDatabase.createNote(note, new DataCallback() {
            @Override
            public void onCallback(List<Note> allNotesList) {
              if (allNotesList == null)
                Toast.makeText(
                  AddNoteActivity.this,
                  "Could Not Add Note",
                  Toast.LENGTH_SHORT
                ).show();
            }
          });
          goToMain();
        }
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

  void goToMain() {
    startActivity(new Intent(this, MainActivity.class));
  }
}
