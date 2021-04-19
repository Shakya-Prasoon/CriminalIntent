package android.bignerdranch.criminalintent;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.File;

public class ImageViewFragment extends DialogFragment {
    private static final String TAG = "ImageViewDialog";
    ImageView mPhotoView;

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(null);
            getView().setBackground(null);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_image, container, false);

        String path = getArguments().getString("filePath");
        File file = new File(path);
        mPhotoView = view.findViewById(R.id.dialog_image);

        if(file == null || !file.exists()) {
            mPhotoView.setImageDrawable(null);

        }
        // otherwise, sets the widget with the picture.
        else {
            Bitmap bm = PictureUtils.getScaledBitmap(
                    file.getPath(), getActivity());
            mPhotoView.setImageBitmap(bm);

        }


        return view;
    }

}
