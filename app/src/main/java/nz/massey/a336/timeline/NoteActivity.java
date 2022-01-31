package nz.massey.a336.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class NoteActivity extends AppCompatActivity {

    public static final String TAG = "NoteActivity";
    public static final String EXTRA_NOTE = "NOTE";
    public static final String EXTRA_POS = "POS";

    private TextView mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // receive year, pos, note
        Intent intentReceive = getIntent();
        int year = intentReceive.getIntExtra(MainActivity.EXTRA_YEAR,0);
        int pos = intentReceive.getIntExtra(MainActivity.EXTRA_POS,0);
        String note = intentReceive.getStringExtra(MainActivity.EXTRA_NOTE);

        Log.i(TAG, "year:" + year);

        ConstraintLayout mNotePage = findViewById(R.id.notePage);
        if(year >= 1900){
            mNotePage.setBackgroundColor(getResources().getColor(R.color.contemporary));
        } else if (year >= 1700){
            mNotePage.setBackgroundColor(getResources().getColor(R.color.modern1));
        }  else if (year >= 1500){
            mNotePage.setBackgroundColor(getResources().getColor(R.color.modern2));
        }  else if (year >= 500){
            mNotePage.setBackgroundColor(getResources().getColor(R.color.medieval));
        }  else if (year >= -600){
            mNotePage.setBackgroundColor(getResources().getColor(R.color.classical));
        }  else {
            mNotePage.setBackgroundColor(getResources().getColor(R.color.prehistory));
        }


        // set year title, note
        TextView mTitleYear = findViewById(R.id.txtYear);
        mTitleYear.setText(("" + year));

        mNote = findViewById(R.id.txtNoteFull);
        mNote.setText(note);

        // when click save, get text, send pos and text
        TextView mBtnSave = findViewById(R.id.btnSave);
        mBtnSave.setOnClickListener((view) -> {

            String newNote = mNote.getText().toString();
            Log.i(TAG, "newNote:" + newNote);

            Intent intent = new Intent(this, MainActivity.class);

            // if note is changed
            if(!note.equals(newNote)){
                intent.putExtra(EXTRA_NOTE, newNote);
                intent.putExtra(EXTRA_POS, pos);
            }
            startActivity(intent);
        });

        ImageView mBtnBack = findViewById(R.id.btnBack);
        mBtnBack.setOnClickListener((view) -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

    }
}

