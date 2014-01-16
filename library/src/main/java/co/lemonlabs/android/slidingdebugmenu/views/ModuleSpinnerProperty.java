package co.lemonlabs.android.slidingdebugmenu.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import co.lemonlabs.android.slidingdebugmenu.R;

/**
 * A ModuleProperty containing a TextView label
 * and a Spinner
 * <p/>
 * Created by balysv on 13/01/14.
 * www.lemonlabs.co
 */
public class ModuleSpinnerProperty extends FrameLayout {

    private TextView mPropertyName;
    private Spinner mPropertySpinner;

    public ModuleSpinnerProperty(Context context, String propertyName) {
        this(context);
        setPropertyName(propertyName);
    }

    public ModuleSpinnerProperty(Context context, int propertyResId) {
        this(context);
        setPropertyName(propertyResId);
    }

    public ModuleSpinnerProperty(Context context) {
        super(context);
        inflate();
    }

    private void inflate() {
        LayoutInflater.from(getContext()).inflate(R.layout.sdm__module_property_spinner, this, true);
        mPropertyName = (TextView) findViewById(R.id.sdm__module_property_name);
        mPropertySpinner = (Spinner) findViewById(R.id.sdm__module_property_spinner);
    }

    /**
     * Set name of property
     *
     * @param name
     */
    public void setPropertyName(String name) {
        mPropertyName.setText(name);
    }

    /**
     * Set name of property
     *
     * @param resId
     */
    public void setPropertyName(int resId) {
        mPropertyName.setText(resId);
    }

    /**
     * Set Adapter for Spinner
     *
     * @param adapter
     */
    public void setSpinnerAdapter(SpinnerAdapter adapter) {
        mPropertySpinner.setAdapter(adapter);
    }

    /**
     * Set Spinner Item Selected callback
     *
     * @param listener
     */
    public void setOnSpinnerItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        mPropertySpinner.setOnItemSelectedListener(listener);
    }

    /**
     * Get the Spinner associated with this property
     *
     * @return
     */
    public Spinner getPropertySpinner() {
        return mPropertySpinner;
    }
}
