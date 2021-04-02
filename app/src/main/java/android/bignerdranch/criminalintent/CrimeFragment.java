package android.bignerdranch.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    // request code:
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;

    // DatePickerFragment's tag:
    private static final String DIALOG_DATE = "DialogDate";
    private Crime mCrime;           // a crime object reference.
    private EditText mTitleField;   // an EditText reference
    private Button mDateButton;     // a Button reference
    private Button mReportButton;   // a reference to the report crime button.
    private Button mSuspectButton;  // a reference to the pick suspect button.
    private CheckBox mSolvedCheckBox;   // CheckBox reference
    private Button mRemoveButton;   // a reference to the remove crime button.
    // an argument to add to the bundle
    private static final String ARG_CRIME_ID = "crime_id";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);

    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK)
            return;

        if(requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(
                    DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            mDateButton.setText(mCrime.getDate().toString());
        }
        // handle the request for contact info
        else if(requestCode == REQUEST_CONTACT && data != null){
            Uri contactUri = data.getData();
            // we want the query to return values for these fields
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME
            };

            // do the query, the Uri is our "where" clause
            Cursor c = getActivity().getContentResolver().query(
                    contactUri, queryFields, null, null, null);

            try {
                if (c.getCount() == 0)
                    return;

                // get the field -- suspect's name
                c.moveToFirst();
                String suspect = c.getString(0);        // from column 0
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            }
            finally {
                c.close();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(
                R.layout.fragment_crime,    // layout resource id
                container,                  // the view's parent
                false);                     // view gets added in view activity's code.

        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        // set the text...
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() { // set listener
            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {
                // required to override this method, but we're not using it.
            }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // required to override this method, but we're not using it.
            }
        });

        mDateButton = (Button)v.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getDate().toString());
        // mDateButton.setEnabled(false); <-- delete this
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                // replace this
                // DatePickerFragment dialog = new DatePickerFragment();
                // with this
                DatePickerFragment dialog =
                        DatePickerFragment.newInstance(mCrime.getDate());

                // set the target fragment:
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm,DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        // update the check box...
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(
                            CompoundButton buttonView,
                            boolean isChecked) {
                        mCrime.setSolved(isChecked); } } );

        // listener for the report crime button
        mReportButton = (Button)v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_subject))
                        .setChooserTitle(getString(R.string.send_report))
                        .createChooserIntent();
                startActivity(i);

//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
//                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
//                i = Intent.createChooser(i, getString(R.string.send_report));
//                startActivity(i);

            }
        });


        // make the intent
        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);

        // get a reference for the suspect button
        mSuspectButton = (Button)v.findViewById(R.id.crime_suspect);

        // add a listener for the suspect button
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // start the activity, and request a result.
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        // if we got the result, update the button text.
        if(mCrime.getSuspect() != null)
            mSuspectButton.setText(mCrime.getSuspect());

        // get a reference to the Activity's PackageManager
        PackageManager pm = getActivity().getPackageManager();

        // search for an activity matching the intent we gave,
        // and match only activities with the CATEGORY_DEFAULT flag.
        // if it can't find a match, deactivate the button.
        if(pm.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null)
            mSuspectButton.setEnabled(false);

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.delete_crime:            // this is the id of the menu option selected
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                getActivity().finish();
            default:    // if the selected option isn't found, defer to the superclass
                return super.onOptionsItemSelected(item);
        }
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // helper method that generates the report
    private String getCrimeReport() {
        String solvedString = null;

        // each argument after the first in getString(...) replaces a place holder.
        if(mCrime.isSolved())
            solvedString = getString(R.string.crime_report_solved);
        else
            solvedString = getString(R.string.crime_report_unsolved);

        String dateFormat = "EEE, MMM, dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();

        if(suspect == null)
            suspect = getString(R.string.crime_report_nosuspect);
        else
            suspect = getString(R.string.crime_report_suspect, suspect);

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString,
                solvedString, suspect);

        return report;
    }
}

