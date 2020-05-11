package pro.ghosh.notetaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity {
  private MaterialToolbar appBar;
  private FloatingActionButton addNoteFab;
  private NoteDatabase notesDatabase;
  private RecyclerView recyclerViewNotes;
  SwipeRefreshLayout swipeRefreshLayout;

  List<Note> allNotes;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    appBar = (MaterialToolbar) findViewById(R.id.appbar_top);
    swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
    recyclerViewNotes = (RecyclerView) findViewById(R.id.recyclerview_notes);
    setSupportActionBar(appBar);
    swipeRefreshLayout.setRefreshing(true);
    notesDatabase = new NoteDatabase(this);
    addNoteFab = findViewById(R.id.fab_add_note);
    addNoteFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(v.getContext(), AddNoteActivity.class));
      }
    });
  }

  private void displayList(List<Note> allNotes) {
    recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
    recyclerViewNotes.setAdapter(new NoteAdapter(this, allNotes));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.top_app_bar_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menuitem_logout:
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        return true;
      case R.id.menuitem_feedback:
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"sudipto@ghosh.pro"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "FEEDBACK[Note Taker]");
        intent.putExtra(Intent.EXTRA_TEXT, "Hi!\nThis is regarding your Android app.\n\n\nRegards,\n<YOUR_NAME>");
        startActivity(Intent.createChooser(intent, "Send Feedback"));
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
      startActivity(new Intent(MainActivity.this, LoginActivity.class));
    } else {
      swipeRefreshLayout.setOnRefreshListener(
        new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            notesDatabase.getAllNotes(FirebaseAuth.getInstance().getCurrentUser().getUid(), new DataCallback() {
              @Override
              public void onCallback(List<Note> allNotesList) {
                allNotes = allNotesList;
                displayList(allNotes);
                if (allNotes.size() == 0)
                  findViewById(R.id.textview_no_notes).setVisibility(View.VISIBLE);
                else
                  findViewById(R.id.textview_no_notes).setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);
              }
            });
          }
        }
      );
      notesDatabase.getAllNotes(FirebaseAuth.getInstance().getCurrentUser().getUid(), new DataCallback() {
        @Override
        public void onCallback(List<Note> allNotesList) {
          allNotes = allNotesList;
          displayList(allNotes);
          if (allNotes.size() == 0)
            findViewById(R.id.textview_no_notes).setVisibility(View.VISIBLE);
          else
            findViewById(R.id.textview_no_notes).setVisibility(View.INVISIBLE);
          swipeRefreshLayout.setRefreshing(false);
        }
      });
    }
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    if (FirebaseAuth.getInstance().getCurrentUser() == null)
      startActivity(new Intent(MainActivity.this, LoginActivity.class));
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }
}
