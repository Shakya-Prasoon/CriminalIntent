package android.bignerdranch.criminalintent;

import android.text.format.Time;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID mId;       // holds the crime id
    private String mTitle;  // the title of the crime
    private Date mDate;     // the date of the crime
    private int[] mTime;

    private boolean mSolved;// is the crime solved?

    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
        mTime = new int[]{0, 0};
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

    public int getHour() {
        return mTime[0];
    }

    public void setHour(int Time) {
        this.mTime[0] = Time;
    }

    public int getMin() {
        return mTime[1];
    }

    public void setMin(int Time) {
        this.mTime[1] = Time;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}

