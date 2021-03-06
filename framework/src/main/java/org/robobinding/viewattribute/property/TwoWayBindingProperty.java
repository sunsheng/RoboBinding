package org.robobinding.viewattribute.property;

import org.robobinding.attribute.ValueModelAttribute;
import org.robobinding.presentationmodel.PresentationModelAdapter;
import org.robobinding.property.PropertyChangeListener;
import org.robobinding.property.ValueModel;

/**
 * 
 * @since 1.0
 * @version $Revision: 1.0 $
 * @author Cheng Wei
 */
public class TwoWayBindingProperty<ViewType, PropertyType> extends AbstractBindingProperty<ViewType, PropertyType> {
	private final TwoWayPropertyViewAttribute<ViewType, PropertyType> viewAttribute;
	private final ViewUpdatePropagationLatch viewUpdatePropagationLatch;

	public TwoWayBindingProperty(ViewType view, TwoWayPropertyViewAttribute<ViewType, PropertyType> viewAttribute, ValueModelAttribute attribute) {
		super(view, viewAttribute, attribute);
		this.viewAttribute = viewAttribute;
		this.viewUpdatePropagationLatch = new ViewUpdatePropagationLatch();
	}

	@Override
	public void performBind(PresentationModelAdapter presentationModelAdapter) {
		ValueModel<PropertyType> valueModel = getPropertyValueModel(presentationModelAdapter);
		valueModel = new PropertyValueModelWrapper(valueModel);
		observeChangesOnTheValueModel(valueModel);
		viewAttribute.observeChangesOnTheView(view, valueModel);
	}

	private void observeChangesOnTheValueModel(final ValueModel<PropertyType> valueModel) {
		valueModel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChanged() {
				if (viewUpdatePropagationLatch.tryToPass())
					updateView(valueModel);
			}
		});
	}

	@Override
	public ValueModel<PropertyType> getPropertyValueModel(PresentationModelAdapter presentationModelAdapter) {
		return presentationModelAdapter.getPropertyValueModel(attribute.getPropertyName());
	}

	private class PropertyValueModelWrapper implements ValueModel<PropertyType> {
		private ValueModel<PropertyType> propertyValueModel;

		public PropertyValueModelWrapper(ValueModel<PropertyType> propertyValueModel) {
			this.propertyValueModel = propertyValueModel;
		}

		@Override
		public PropertyType getValue() {
			return propertyValueModel.getValue();
		}

		@Override
		public void setValue(PropertyType newValue) {
			viewUpdatePropagationLatch.turnOn();
			try {
				propertyValueModel.setValue(newValue);
			} finally {
				viewUpdatePropagationLatch.turnOff();
			}
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			propertyValueModel.addPropertyChangeListener(listener);
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			propertyValueModel.removePropertyChangeListener(listener);
		}
	}
}