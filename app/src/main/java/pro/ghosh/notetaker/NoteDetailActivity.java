package pro.ghosh.notetaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NoteDetailActivity extends AppCompatActivity {
  private MaterialToolbar appBar;
  private NoteDatabase notesDatabase;
  private String noteId;
  private Note note;
  private TextView noteContent;
  SwipeRefreshLayout swipeRefreshLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_note_detail);
    appBar = (MaterialToolbar) findViewById(R.id.appbar_top_note_detail);
    setSupportActionBar(appBar);
    getSupportActionBar().setTitle("");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_24dp);

    noteContent = (TextView) findViewById(R.id.textview_note_content);

    swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_note_detail);
    swipeRefreshLayout.setRefreshing(true);

    noteId = getIntent().getStringExtra("id");
    notesDatabase = new NoteDatabase(this);
    notesDatabase.getNote(noteId, new DataCallback() {
      @Override
      public void onCallback(List<Note> allNotesList) {
        note = allNotesList.get(0);
        getSupportActionBar().setTitle(note.getTitle());
        noteContent.setText(note.getContent());
        swipeRefreshLayout.setRefreshing(false);
      }
    });

    swipeRefreshLayout.setOnRefreshListener(
      new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
          notesDatabase.getNote(noteId, new DataCallback() {
            @Override
            public void onCallback(List<Note> allNotesList) {
              note = allNotesList.get(0);
              getSupportActionBar().setTitle(note.getTitle());
              noteContent.setText(note.getContent());
              swipeRefreshLayout.setRefreshing(false);
            }
          });
        }
      });

    FloatingActionButton fab = findViewById(R.id.fab_edit_note);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        Intent i = new Intent(v.getContext(), EditNoteActivity.class)
          .putExtra(
            "id",
            note.getId()
          );
        v.getContext().startActivity(i);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.top_app_bar_menu_note_detail, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
      case R.id.menuitem_delete:
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("Confirm to delete this note.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            notesDatabase.deleteNote(noteId, new DataCallback() {
              @Override
              public void onCallback(List<Note> allNotesList) {

              }
            });
            onBackPressed();
          }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

          }
        });
        builder.show();
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
    swipeRefreshLayout.setRefreshing(true);
    notesDatabase.getNote(noteId, new DataCallback() {
      @Override
      public void onCallback(List<Note> allNotesList) {
        note = allNotesList.get(0);
        getSupportActionBar().setTitle(note.getTitle());
        noteContent.setText(note.getContent());
        swipeRefreshLayout.setRefreshing(false);
      }
    });
  }
}
