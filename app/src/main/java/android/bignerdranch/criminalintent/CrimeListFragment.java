package android.bignerdranch.criminalintent;

import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CrimeListFragment extends Fragment {
    // We just added stuff...
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView)view.findViewById(
                R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));

        updateUI(); // fixed now!
        return view;
    }

    public void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
    }

    // the adapter, also an inner class in class CrimeListFragment,
    // requires 3 overrides
    private class CrimeAdapter extends RecyclerView.Adapter{
        private List<Crime> mCrimes;
        private Button callPolice;
        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public int getItemViewType(int position) {
            if (mCrimes.get(position).isRequirePolice() == false){
                return 0;
            }
            return 1;
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view;
            if (viewType == 0) {
                view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
                return new ViewHolderRegular(view);
            }
            view = layoutInflater.inflate(R.layout.list_serious_item_crime, parent,
                    false);
            return new ViewHolderSerious(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            Button callPolice;
            if (getItemViewType(position) == 0){
                //Regular
                ViewHolderRegular viewHolderRegular = (ViewHolderRegular) holder;
                viewHolderRegular.title.setText(mCrimes.get(position).getTitle());
                viewHolderRegular.date.setText(mCrimes.get(position).getDate().toString());

            }
            else{
                //Police
                ViewHolderSerious viewHolderSerious = (ViewHolderSerious) holder;
                viewHolderSerious.title.setText(mCrimes.get(position).getTitle());
                viewHolderSerious.date.setText(mCrimes.get(position).getDate().toString());
                viewHolderSerious.callPolice.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), R.string.calling_police,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), mCrimes.get(position).getTitle() + " clicked!",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }



        class ViewHolderRegular extends RecyclerView.ViewHolder {
            TextView title, date;
            public ViewHolderRegular(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.crime_title);
                date = itemView.findViewById(R.id.crime_date);
            }
        }



        class ViewHolderSerious extends RecyclerView.ViewHolder {
            TextView title, date;
            Button callPolice;
            public ViewHolderSerious(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.crime_title);
                date = itemView.findViewById(R.id.crime_date);
                callPolice = itemView.findViewById(R.id.crime_call_police);
            }
        }
    }
}

