package org.robobinding.viewattribute.property;

import android.view.View;


/**
 *
 * @since 1.0
 * @version $Revision: 1.0 $
 * @author Cheng Wei
 */
public interface MultiTypePropertyViewAttributeFactory<T extends View> {
    MultiTypePropertyViewAttribute<T> create();

}
