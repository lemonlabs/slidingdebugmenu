package co.lemonlabs.android.slidingdebugmenu.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import co.lemonlabs.android.slidingdebugmenu.R;

/**
 * Simple module property containing
 * two text views
 * <p/>
 * Created by balysv on 10/01/14.
 * www.lemonlabs.co
 */
public class ModuleSimpleProperty extends FrameLayout {

    private TextView mPropertyName;
    private TextView mPropertyValue;

    public ModuleSimpleProperty(Context context, String propertyName) {
        this(context);
        setPropertyName(propertyName);
    }

    public ModuleSimpleProperty(Context context, int propertyResId) {
        this(context);
        setPropertyName(propertyResId);
    }

    public ModuleSimpleProperty(Context context) {
        super(context);
        inflate();
    }

    private void inflate() {
        LayoutInflater.from(getContext()).inflate(R.layout.sdm__module_property_simple, this, true);
        mPropertyName = (TextView) findViewById(R.id.sdm__module_property_name);
        mPropertyValue = (TextView) findViewById(R.id.sdm__module_property_value);
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
     * Set property value
     *
     * @param name
     */
    public void setPropertyValue(String name) {
        mPropertyValue.setText(name);
    }

    /**
     * Set property value
     *
     * @param resId
     */
    public void setPropertyValue(int resId) {
        mPropertyValue.setText(resId);
    }
}
