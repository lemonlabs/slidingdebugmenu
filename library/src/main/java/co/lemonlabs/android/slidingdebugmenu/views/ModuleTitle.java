package co.lemonlabs.android.slidingdebugmenu.views;

import android.content.Context;
import android.widget.TextView;

import co.lemonlabs.android.slidingdebugmenu.R;

/**
 * Created by balysv on 10/01/14.
 * www.lemonlabs.co
 */
public class ModuleTitle extends TextView {

    public ModuleTitle(Context context, String title) {
        super(context);
        setTextAppearance(context, R.style.Widget_Sdm_TextView_ModuleTitle);
        setBackgroundResource(R.drawable.sdm__module_title_bg);

        setText(title.toUpperCase());
    }
}
