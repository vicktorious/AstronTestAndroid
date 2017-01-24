package com.example.vick.astrontest;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectStreamException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    /* Variables for the list */
    private static PersonAdapter adapter;
    private static List<Person> people;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            people.clear();
            adapter.notifyDataSetChanged();
            LinearLayout lin = (LinearLayout) findViewById(R.id.pieChart);
            lin.removeAllViews();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            switch(getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    people = new ArrayList<Person>();
                    people.add(new Person(0, "Press ", "Refresh", 0, 0));

                    View v1 = inflater.inflate(R.layout.fragment_list, container, false);

                    ListView listView = (ListView) v1.findViewById(R.id.personListView);
                    adapter = new PersonAdapter(getContext(), people);
                    listView.setAdapter(adapter);
                    registerForContextMenu(listView);

                    FloatingActionButton fab1 = (FloatingActionButton) v1.findViewById(R.id.fab1);
                    fab1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LoadJSONAsync loadJSON = new LoadJSONAsync();
                            loadJSON.execute();
                        }
                    });

                    return v1;
                case 2:
                    final View v2 = inflater.inflate(R.layout.fragment_chart, container, false);

                    FloatingActionButton fab2 = (FloatingActionButton) v2.findViewById(R.id.fab2);
                    fab2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if ((people.size() == 1 && people.get(0).getGender() == 0) || people.size() == 0 ) {
                                LoadJSONAsync loadJSON = new LoadJSONAsync();
                                loadJSON.execute();

                                Toast.makeText(v2.getContext(), "Press again for the pie chart", Toast.LENGTH_SHORT).show();
                            } else {
                                int studentCount = 0;
                                int workerCount = 0;
                                int retiredCount = 0;

                                for (Person person : people) {
                                    switch (person.getAgeGroup()) {
                                        case STUDENT:
                                            studentCount++;
                                            break;
                                        case WORKER:
                                            workerCount++;
                                            break;
                                        case RETIRED:
                                            retiredCount++;
                                            break;
                                        default:
                                            break;
                                    }
                                }

                                float studentDegree = 360 * studentCount / people.size();
                                float workerDegree  = 360 * workerCount / people.size();
                                float retiredDegree = 360 - studentDegree - workerDegree;

                                LinearLayout lin = (LinearLayout) v2.findViewById(R.id.pieChart);
                                lin.removeAllViews();
                                PieChart pie = new PieChart(v2.getContext(), lin,studentDegree, workerDegree, retiredDegree);
                                lin.addView(pie);
                                pie.invalidate();
                            }
                        }
                    });

                    return v2;
                default:
                    return null;
            }
        }
    }

    static class LoadJSONAsync extends AsyncTask<Object, Object, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            try {
                URL url = new URL("http://mash1.astron.hu:23985/recruiting/attendees");

                URLConnection uc = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));

                String line = in.readLine();

                JSONArray jArray = new JSONArray(line);

                people.clear();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);

                    int id = jObject.getInt("id");
                    String firstName = jObject.getJSONObject("name").getString("firstname");
                    String lastName = jObject.getJSONObject("name").getString("lastname");
                    String gender = jObject.getString("gender");
                    int age = jObject.getInt("age");

                    int genderInt = 0;

                    if(gender.equals("male")) {
                        genderInt = 1;
                    } else {
                        genderInt = 2;
                    }

                    Person psn = new Person(id, firstName, lastName, genderInt, age);
                    people.add(i,psn);
                }

            } catch (Exception e) {
                Log.e("Exception", e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            adapter.setList(people);
            adapter.orderList();
            adapter.notifyDataSetChanged();
        }
    }

    public static class PieChart extends View {

        private RectF rect;
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        float sD = 0;
        float wD = 0;
        float rD = 0;

        int size = 0;
        int start = 0;

        int xO = 0;
        int yO = 0;

        public PieChart(Context context, LinearLayout l, float sD, float wD, float rD) {
            super(context);

            if(l.getWidth() < l.getHeight()){
                size = l.getWidth();
                start = 0;
            } else {
                size = l.getHeight();
                start = (l.getWidth() - l.getHeight()) / 2;
            }

            xO = l.getWidth() / 2;
            yO = l.getHeight() / 2;

            rect = new RectF(start, 0, start + size, size);

            this.sD = sD;
            this.wD = wD;
            this.rD = rD;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            paint.setColor(Color.BLACK);
            canvas.drawArc(rect, 0, sD, true, paint);

            paint.setColor(Color.YELLOW);
            canvas.drawArc(rect, sD, wD, true, paint);

            paint.setColor(Color.RED);
            canvas.drawArc(rect, sD + wD, rD, true, paint);
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();

            RotateAnimation a = new RotateAnimation(0,360,xO,yO);
            a.setDuration(1000);
            a.setFillAfter(true);
            this.startAnimation(a);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Conference List";
                case 1:
                    return "Pie Chart";
            }
            return null;
        }
    }
}
