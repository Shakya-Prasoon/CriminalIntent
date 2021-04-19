package android.bignerdranch.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID mId;       // holds the crime id
    private String mTitle;  // the title of the crime
    private Date mDate;     // the date of the crime
    private boolean mSolved;// is the crime solved?
    private String mSuspect;// the crime suspect

    public Crime() {
        this(UUID.randomUUID());
    }

    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public String getSuspect() { return mSuspect; }

    public void setSuspect(String suspect) { mSuspect = suspect; }

    public String getPhotoFileName() {
        return "IMG_" + getID().toString() + ".jpg";
    }

    public String getPhotoFileName1() {
        return "IMG1_" + getID().toString() + ".jpg";
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
}

