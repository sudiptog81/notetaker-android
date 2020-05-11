package pro.ghosh.notetaker;

import java.util.List;

interface DataCallback {
  void onCallback(List<Note> notes);
}