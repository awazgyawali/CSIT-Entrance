package np.com.aawaz.csitentrance.interfaces;

import android.view.View;

public interface ClickListener {
    void itemClicked(View view, int position);

    void itemLongClicked(View view, int position);

    void upVoteClicked(View view, int position);
}
