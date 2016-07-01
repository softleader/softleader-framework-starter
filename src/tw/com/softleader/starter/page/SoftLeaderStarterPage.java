package tw.com.softleader.starter.page;

import org.eclipse.jface.util.BidiUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.InputText;
import org.eclipse.swt.widgets.Label;

public interface SoftLeaderStarterPage {

	public static final int TEXT_WIDTH = 600;

	default InputText createText(Composite parent, String labelText) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(labelText);
		label.setFont(parent.getFont());

		InputText text = new InputText(parent, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setFont(parent.getFont());
		BidiUtils.applyBidiProcessing(text, BidiUtils.BTD_DEFAULT);
		return text;
	}

}
