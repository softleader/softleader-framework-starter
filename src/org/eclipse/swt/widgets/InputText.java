package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;

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

}
