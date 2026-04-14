package com.eduquiz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eduquiz.R;
import com.eduquiz.adapters.SubjectAdapter;
import com.eduquiz.models.PrefsManager;
import com.eduquiz.models.Subject;
import com.eduquiz.services.BackgroundMusicService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SubjectAdapter.OnSubjectClickListener {

    private PrefsManager prefs;
    private List<Subject> subjectList;
    private SubjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new PrefsManager(this);

        // Start background music
        startService(new Intent(this, BackgroundMusicService.class));

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText(getString(R.string.welcome_back, prefs.getPlayerName(), prefs.getPlayerGrade()));

        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBestScores();
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        subjectList = new ArrayList<>();
        loadBestScores();

        RecyclerView rvSubjects = findViewById(R.id.rvSubjects);
        rvSubjects.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new SubjectAdapter(this, subjectList, this);
        rvSubjects.setAdapter(adapter);
    }

    private void loadBestScores() {
        if (subjectList == null) subjectList = new ArrayList<>();
        subjectList.clear();
        subjectList.add(new Subject("Mathematics", "➕", R.color.subject_math,
                prefs.getBestScore("Mathematics", "Foundation"),
                prefs.getBestScore("Mathematics", "Advanced")));
        subjectList.add(new Subject("Science", "🔬", R.color.subject_science,
                prefs.getBestScore("Science", "Foundation"),
                prefs.getBestScore("Science", "Advanced")));
        subjectList.add(new Subject("English", "📖", R.color.subject_english,
                prefs.getBestScore("English", "Foundation"),
                prefs.getBestScore("English", "Advanced")));
        subjectList.add(new Subject("History", "🏛", R.color.subject_history,
                prefs.getBestScore("History", "Foundation"),
                prefs.getBestScore("History", "Advanced")));
    }

    @Override
    public void onSubjectClick(Subject subject) {
        Intent intent = new Intent(this, SubjectActivity.class);
        intent.putExtra("subject_name", subject.getName());
        startActivity(intent);
    }

    //work in progress
}
