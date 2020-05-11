package pro.ghosh.notetaker;

import android.view.*;
import android.content.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
  private LayoutInflater inflater;
  private List<Note> notes;

  NoteAdapter(Context context, List<Note> notes) {
    this.inflater = LayoutInflater.from(context);
    this.notes = notes;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = inflater.inflate(R.layout.fragment_note_item, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
    String id = notes.get(i).getId();
    String title = notes.get(i).getTitle();
    long milliseconds = notes.get(i).getTime().getSeconds() * 1000
      + notes.get(i).getTime().getNanoseconds() / 1000000;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm a");
    String time = LocalDateTime.ofInstant(
      Instant.ofEpochMilli(milliseconds),
      ZoneId.systemDefault()
    ).format(formatter);
    viewHolder.noteId.setText(id);
    viewHolder.noteTitle.setText(title);
    viewHolder.noteSubtitle.setText(time);
  }

  @Override
  public int getItemCount() {
    return notes.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView noteTitle, noteSubtitle, noteId;

    ViewHolder(@NonNull final View v) {
      super(v);
      noteId = (TextView) itemView.findViewById(R.id.id_note);
      noteTitle = (TextView) itemView.findViewById(R.id.title_note);
      noteSubtitle = (TextView) itemView.findViewById(R.id.subtitle_note);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent i = new Intent(v.getContext(), NoteDetailActivity.class)
            .putExtra(
              "id",
              noteId.getText()
            );
          v.getContext().startActivity(i);
        }
      });
    }
  }
}
