package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

/**
 * 讓 widget 生命週期結束後還能取的到值
 * 
 * @author Matt
 *
 */
public class InputText extends Text {

	private String value;

	public InputText(Composite parent, int style) {
		super(parent, style);

		addListener(SWT.Modify, new Listener() {

			@Override
			public void handleEvent(Event event) {
				value = getText().trim();
			}
		});
	}

	public String getValue() {
		return value;
	}

	public InputText enabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public InputText width(int width) {
		((GridData) getLayoutData()).widthHint = width;
		return this;
	}

	public InputText text(String value) {
		setText(value);
		return this;
	}

	public InputText listener(int eventType, Listener listener) {
		addListener(eventType, listener);
		return this;
	}

}
