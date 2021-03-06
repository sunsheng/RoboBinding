package org.robobinding.viewattribute.property;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.robobinding.attribute.Attributes.aValueModelAttribute;
import static org.robobinding.viewattribute.property.MockPresentationModelAdapterBuilder.aPresentationModelAdapterWithReadOnlyProperty;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.robobinding.attribute.ValueModelAttribute;
import org.robobinding.presentationmodel.PresentationModelAdapter;
import org.robobinding.property.ValueModel;
import org.robobinding.property.ValueModelUtils;
import org.robobinding.util.RandomValues;

import android.view.View;

/**
 * 
 * @since 1.0
 * @version $Revision: 1.0 $
 * @author Cheng Wei
 */
@RunWith(MockitoJUnitRunner.class)
public class OneWayBindingPropertyTest {
	private static final String PROPERTY_NAME = "readOnlyProperty1";
	@Mock
	View view;
	private PropertyViewAttributeSpy viewAttributeSpy;
	private ValueModel<Integer> valueModel;

	@Before
	public void setUp() {
		viewAttributeSpy = new PropertyViewAttributeSpy();
		valueModel = ValueModelUtils.create(-1);
	}

	@Test
	public void givenABoundProperty_whenUpdateValueModel_thenViewIsSynchronized() {
		aBoundProperty();

		Integer newValue = RandomValues.anyInteger();
		valueModel.setValue(newValue);

		assertThat(viewAttributeSpy.viewValue, is(newValue));
	}

	private OneWayBindingProperty<View, Integer> aBoundProperty() {
		ValueModelAttribute attribute = aValueModelAttribute(PROPERTY_NAME);
		OneWayBindingProperty<View, Integer> bindingProperty = new OneWayBindingProperty<View, Integer>(view, viewAttributeSpy, attribute);

		PresentationModelAdapter presentationModelAdapter = aPresentationModelAdapterWithReadOnlyProperty(PROPERTY_NAME, valueModel);
		bindingProperty.performBind(presentationModelAdapter);
		return bindingProperty;
	}

	@Test
	public void givenABoundProperty_whenViewIsUpdated_thenValueModelShouldRemainUnChanged() {
		aBoundProperty();
		Integer initailValue = valueModel.getValue();

		Integer newValue = RandomValues.anyInteger();
		viewAttributeSpy.simulateViewUpdate(newValue);

		assertThat(valueModel.getValue(), is(initailValue));
	}

}
