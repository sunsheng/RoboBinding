package org.robobinding.widget.adapterview;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robobinding.attribute.Attributes.aValueModelAttribute;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.robobinding.BindingContext;
import org.robobinding.SubViewBinder;
import org.robobinding.attribute.ValueModelAttribute;

import android.view.View;

/**
 * 
 * @since 1.0
 * @version $Revision: 1.0 $
 * @author Cheng Wei
 */
@RunWith(MockitoJUnitRunner.class)
public class SubViewPresentationModelAttributeTest {
	@Mock
	private SubViewHolder subViewHolder;
	@Mock
	private View boundSubView;

	private int layoutId = 2;
	private String presentationModelPropertyName = "propertyName";
	private ValueModelAttribute valueModelAttribute = aValueModelAttribute(presentationModelPropertyName);

	@Test
	public void whenBindTo_thenBoundSubViewIsSetOnHolder() {
		BindingContext bindingContext = mock(BindingContext.class);
		SubViewBinder viewBinder = mock(SubViewBinder.class);
		when(bindingContext.createSubViewBinder()).thenReturn(viewBinder);
		when(viewBinder.inflateAndBind(layoutId, presentationModelPropertyName)).thenReturn(boundSubView);
		SubViewPresentationModelAttribute subViewAttribute = new SubViewPresentationModelAttribute(layoutId, subViewHolder);
		subViewAttribute.setAttribute(valueModelAttribute);

		subViewAttribute.bindTo(bindingContext);

		verify(subViewHolder).setSubView(boundSubView);
	}
}
