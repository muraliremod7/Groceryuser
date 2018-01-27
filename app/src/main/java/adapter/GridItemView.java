package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.indianservers.onlinegrocery.R;

/**
 * Created by Prabhu on 29-12-2017.
 */

public class GridItemView extends FrameLayout {

    private TextView textView;
    private LinearLayout linearLayout;

    public GridItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.single_grid, this);
        textView = (TextView) getRootView().findViewById(R.id.pharmacysingletextview);
        linearLayout = (LinearLayout)getRootView().findViewById(R.id.single);
    }

    public void display(String text, boolean isSelected) {
        textView.setText(text);
        display(isSelected);
    }

    public void display(boolean isSelected) {
        linearLayout.setBackgroundResource(isSelected ? R.color.colorPrimary : R.color.white);
    }
}
