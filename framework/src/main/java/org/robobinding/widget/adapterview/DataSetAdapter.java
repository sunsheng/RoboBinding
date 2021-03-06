package org.robobinding.widget.adapterview;

import org.robobinding.itempresentationmodel.ItemPresentationModel;
import org.robobinding.property.DataSetValueModel;
import org.robobinding.property.DataSetValueModelWrapper;
import org.robobinding.property.PropertyChangeListener;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 
 * @since 1.0
 * @author Cheng Wei
 * @author Robert Taylor
 */
public class DataSetAdapter<T> extends BaseAdapter {
	private enum ViewType {
		ITEM_LAYOUT, DROPDOWN_LAYOUT
	}

	private final boolean preInitializeViews;
	private final DataSetValueModel<T> dataSetValueModel;

	private final ItemLayoutBinder itemLayoutBinder;
	private final ItemLayoutBinder dropdownLayoutBinder;

	private boolean propertyChangeEventOccurred;

	public DataSetAdapter(DataSetValueModel<T> dataSetValueModel, ItemLayoutBinder itemLayoutBinder, ItemLayoutBinder dropdownLayoutBinder,
			boolean preInitializeViews) {
		this.preInitializeViews = preInitializeViews;

		this.dataSetValueModel = createValueModelFrom(dataSetValueModel);
		this.itemLayoutBinder = itemLayoutBinder;
		this.dropdownLayoutBinder = dropdownLayoutBinder;

		propertyChangeEventOccurred = false;
	}

	private DataSetValueModel<T> createValueModelFrom(DataSetValueModel<T> valueModel) {
		if (!preInitializeViews) {
			return wrapAsZeroSizeDataSetUntilPropertyChangeEvent(valueModel);
		} else {
			return valueModel;
		}
	}

	public void observeChangesOnTheValueModel() {
		dataSetValueModel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChanged() {
				propertyChangeEventOccurred = true;
				notifyDataSetChanged();
			}
		});
	}

	private DataSetValueModel<T> wrapAsZeroSizeDataSetUntilPropertyChangeEvent(final DataSetValueModel<T> valueModel) {
		return new DataSetValueModelWrapper<T>(valueModel) {
			@Override
			public int size() {
				if (propertyChangeEventOccurred)
					return valueModel.size();

				return 0;
			}
		};
	}

	@Override
	public int getCount() {
		if (dataSetValueModel == null)
			return 0;

		return dataSetValueModel.size();
	}

	@Override
	public T getItem(int position) {
		return dataSetValueModel.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, ViewType.ITEM_LAYOUT);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, ViewType.DROPDOWN_LAYOUT);
	}

	private View createViewFromResource(int position, View convertView, ViewGroup parent, ViewType viewType) {
		if (convertView == null) {
			return newView(position, parent, viewType);
		} else {
			updateItemPresentationModel(convertView, position);
			return convertView;
		}
	}

	private View newView(int position, ViewGroup parent, ViewType viewType) {
		ItemPresentationModel<T> itemPresentationModel = dataSetValueModel.newItemPresentationModel();
		itemPresentationModel.updateData(position, getItem(position));

		View view;
		if (viewType == ViewType.ITEM_LAYOUT) {
			view = itemLayoutBinder.inflateAndBindTo(itemPresentationModel);
		} else {
			view = dropdownLayoutBinder.inflateAndBindTo(itemPresentationModel);
		}
		view.setTag(itemPresentationModel);
		return view;
	}

	private void updateItemPresentationModel(View view, int position) {
		@SuppressWarnings("unchecked")
		ItemPresentationModel<T> itemPresentationModel = (ItemPresentationModel<T>) view.getTag();
		itemPresentationModel.updateData(position, getItem(position));
	}
}