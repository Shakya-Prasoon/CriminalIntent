package android.bignerdranch.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID mId;       // holds the crime id
    private String mTitle;  // the title of the crime
    private Date mDate;     // the date of the crime
    private boolean mSolved;// is the crime solved?
    private boolean mRequirePolice; // to determine if the case is serious or not

    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getID() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public boolean isRequirePolice() {
        return mRequirePolice;
    }

    public void setRequirePolice(boolean mRequirePolice) {
        this.mRequirePolice = mRequirePolice;
    }

}

