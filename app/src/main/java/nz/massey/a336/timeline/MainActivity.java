package nz.massey.a336.timeline;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import nz.massey.a336.timeline.databinding.ActivityMainBinding;
import nz.massey.a336.timeline.databinding.ListYearBinding;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Timeline";
    public static final String EXTRA_YEAR = "YEAR";
    public static final String EXTRA_POS = "POS";
    public static final String EXTRA_NOTE = "NOTE";

    private static final List<String> LIST_NOTE = new ArrayList<>();
    private static final List<Integer> LIST_YEAR = new ArrayList<>();

    private static final int startYear = -3000;
    private static int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    private static boolean installed;
    private static boolean received = false;

    private static int mCurrYearPos;
    private static int mPrevPosition = 0;
    private RecyclerView.LayoutManager mLayoutManager;
    private ActivityMainBinding mMainLayout;
    private static final String filename = "notes.txt";
    private final MyAdapter mAdapter = new MyAdapter();

    static {
        int pos = 0;
        for (int i = 2100; i >= startYear; i--) {
            LIST_NOTE.add("");
            LIST_YEAR.add(i);
            pos++;
            if(i == currentYear) mCurrYearPos = pos;
        }
        Log.i(TAG, "static " + mCurrYearPos + ", " + currentYear + ", " + LIST_YEAR.size());
        installed = true;
    }

    void init(){
        if(installed){
            installed = false;

            // when install, scroll to currYear, wait until adapter finish
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mMainLayout.list.scrollToPosition(mCurrYearPos - 1);
                    Log.i(TAG, "scroll");
                }
            });

            // readFile only once when install, if there is a file, sets LIST_NOTE
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    long startTime = System.currentTimeMillis();
                    //readFile();
                    long estimatedTime = System.currentTimeMillis() - startTime;
                    Log.i(TAG, "time: " + estimatedTime);
                    //mAdapter.notifyDataSetChanged();
                }
            }, 200);
        }

        // searchBox
        mMainLayout.btnGo.setOnClickListener((view) -> {

            int searchYear = 0;
            try {
                searchYear = Integer.parseInt(mMainLayout.tbxGo.getText().toString());
            } catch(NumberFormatException e){
                e.printStackTrace();
                Log.i(TAG, "ignore no number");
                return;
            }
            Log.i(TAG, "goto: " + searchYear);

            if(searchYear > 2100 || searchYear < -3000){
                String message = "range: -3000 to 2100";

                Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            int moveTo = findYearPos(searchYear);
            mMainLayout.list.scrollToPosition(moveTo);
        });

        mMainLayout.btnSettings.setOnClickListener((view) -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });
    }

    public void readFile(){
        Log.i(TAG, "readFile");

        try {
            FileInputStream is = this.openFileInput(filename);
            Scanner scanner = new Scanner(new InputStreamReader(is));
            scanner.useDelimiter(";\n");

            // first entry is currentYear, to not have first entry empty when scanning
            currentYear = scanner.nextInt();
            Log.i(TAG, "first entry: " + currentYear);

            int i = 0;
            String text = "";
            while(scanner.hasNext()){
                Log.i(TAG, "readScanner: " + i + " " + text);

                text = /*"sssssssssssssssssssssssssss" +
                        "dddddddddddddddddddddddddddd" +
                        "ddddddddddddddddddddddddddd" +
                        "d dddddddddddddd" +
                        "ddddddddddddddddddddddddddddd\n" +
                        "dddddddddddkkk";*/
                        scanner.next();
                LIST_NOTE.set(i, text);
                i++;
            }
            scanner.close();
        } catch (IOException e) {
            // if installed first time, no file
            Log.i(TAG, "cannot read file");
        }
    }

    public void writeFile(){
        //Log.i(TAG, "dir:" + context.getFilesDir().toString());
        Log.i(TAG, "writeFile");

        try {
            // create file if not exist
            FileOutputStream fos = this.openFileOutput(filename, Context.MODE_PRIVATE);

            fos.write((currentYear + ";\n").getBytes());
            for (String text : LIST_NOTE) {
                fos.write((text + ";\n").getBytes());
                Log.i(TAG, "read: " + text);
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int findYearPos(int year){
        return mCurrYearPos + (currentYear - year) - 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainLayout = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mMainLayout.getRoot());

        mMainLayout.list.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        mMainLayout.list.setLayoutManager(mLayoutManager);

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        // receive text, pos if changed
        Intent intentReceive = getIntent();
        if(intentReceive.getExtras() != null) {
            received = true;
            String strNote = intentReceive.getStringExtra(NoteActivity.EXTRA_NOTE);
            int pos = intentReceive.getIntExtra(NoteActivity.EXTRA_POS,0);
            LIST_NOTE.set(pos, strNote);

            Log.i(TAG, "received: " + received + ", " + pos +", "+ strNote);
        }
        Log.i(TAG, "oncreate: " + received );

        init();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
        // save the list position
        mPrevPosition = ((LinearLayoutManager)mLayoutManager).findFirstVisibleItemPosition();
        Log.i(TAG,"position: " + mPrevPosition);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainLayout.list.scrollToPosition(mPrevPosition);
    }

    // happens when go away from MainActivity
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");

        // when list_note is changed, save list_note before uninstall
        if(received){
            received = false;
            writeFile();
            Log.i(TAG, "received: writeFile " + received);
        }
    }

    //---------------------------------------------------------------------------------
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        // happens everytime when front page
        public MyAdapter() {
            Log.i(TAG, "constructor");
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            final TextView mYearView;
            final ImageView mBtnCircle;
            final TextView mNoteView;
            final View mLine;
            ConstraintLayout mListBlock;

            public ViewHolder(View v) {
                super(v);
                mYearView = v.findViewById(R.id.year);
                mBtnCircle = v.findViewById(R.id.btnCircle);
                mNoteView = v.findViewById(R.id.note);
                mLine = v.findViewById(R.id.line);
                mListBlock = (ConstraintLayout)v.findViewById(R.id.listBlock);
            }
        }

        // happens every time for views on screen
        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.i(TAG, "onCreateViewHolder");

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_year, parent, false);
            final ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        // happens everytime when dragged
        @Override
        public void onBindViewHolder(final ViewHolder vh, int pos) {

            int year = LIST_YEAR.get(pos);
            String note = LIST_NOTE.get(pos);
            Log.i(TAG, "note:" + note);

            vh.mYearView.setText(("" + year));
            vh.mNoteView.setText(note);

            if(year >= 1900){
                vh.mListBlock.setBackgroundColor(getResources().getColor(R.color.contemporary));
/*
                vh.mYearView.setTextColor(Color.WHITE);
                vh.mNoteView.setTextColor(Color.WHITE);
                vh.mBtnCircle.setColorFilter(Color.WHITE);
                vh.mLine.setBackgroundColor(Color.WHITE);
*/
            } else if (year >= 1700){
                vh.mListBlock.setBackgroundColor(getResources().getColor(R.color.modern1));
            }  else if (year >= 1500){
                vh.mListBlock.setBackgroundColor(getResources().getColor(R.color.modern2));
            }  else if (year >= 500){
                vh.mListBlock.setBackgroundColor(getResources().getColor(R.color.medieval));
            }  else if (year >= -600){
                vh.mListBlock.setBackgroundColor(getResources().getColor(R.color.classical));
            }  else {
                vh.mListBlock.setBackgroundColor(getResources().getColor(R.color.prehistory));
            }

            // click circle, send pos, year, note
            vh.mBtnCircle.setOnClickListener((view) -> {
                Context context = view.getContext();

                Intent intent = new Intent(context, NoteActivity.class);
                intent.putExtra(EXTRA_POS, pos);
                intent.putExtra(EXTRA_YEAR, year);
                intent.putExtra(EXTRA_NOTE, note);

                context.startActivity(intent);
            });
            Log.i(TAG, "onBind");

        }

        // happens for all the views visible on screen, when new views as dragged
        @Override
        public int getItemCount() {
            Log.i(TAG, "yearSize:" + LIST_YEAR.size());
            return LIST_YEAR.size();
        }
    }
}
