package android.bignerdranch.criminalintent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class CrimeFragment extends Fragment {
    private Crime mCrime;           // a crime object reference.
    private EditText mTitleField;   // an EditText reference
    private Button mDateButton;     // a Button reference
    private CheckBox mSolvedCheckBox;   // CheckBox reference
    private CheckBox mSeriousCrime; // Another Checkbox to allow user to select the
                                            // seriousness of the crime

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrime = new Crime();       // Instantiate a new Crime object.
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
        mDateButton.setEnabled(false);

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setOnCheckedChangeListener(
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(
                    CompoundButton buttonView,
                    boolean isChecked) {
                    mCrime.setSolved(isChecked); } } );

        mSeriousCrime = (CheckBox)v.findViewById(R.id.crime_call_police);
        mSeriousCrime.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(
                            CompoundButton buttonView,
                            boolean isChecked) {
                        mCrime.setRequirePolice(isChecked); } } );
        return v;
    }
}

