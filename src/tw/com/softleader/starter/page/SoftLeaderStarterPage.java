package tw.com.softleader.starter.page;

import org.eclipse.jface.util.BidiUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.InputText;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public interface SoftLeaderStarterPage {

	public static final int TEXT_WIDTH = 500;

	default InputText createText(Composite parent, String labelText, String initialValue, int witdh,
			Listener modifyListener) {
		InputText text = createText(parent, labelText, initialValue, witdh);
		text.addListener(SWT.Modify, modifyListener);
		return text;
	}

	default InputText createText(Composite parent, String labelText, String initialValue, int width) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(labelText);
		label.setFont(parent.getFont());

		InputText text = new InputText(parent, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = width;
		text.setLayoutData(data);
		text.setFont(parent.getFont());

		if (text != null) {
			text.setText(initialValue);
		}
		BidiUtils.applyBidiProcessing(text, BidiUtils.BTD_DEFAULT);
		return text;
	}

}
