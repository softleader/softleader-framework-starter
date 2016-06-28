package tw.com.softleader.starter.pojo;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;

public class Group<T> {

	public static enum Layout {
		/**
		 * VERTICAL
		 */
		V(SWT.VERTICAL),
		/**
		 * HORIZONTAL
		 */
		H(SWT.HORIZONTAL);

		Layout(int swt) {
			this.swt = swt;
		}

		public final int swt;

	}

	public static enum Style {

		CHECK(SWT.CHECK), RADIO(SWT.RADIO),;

		Style(int swt) {
			this.swt = swt;
		}

		public final int swt;
	}

	private Collection<T> data = new ArrayList<>();
	private String text;
	private Layout layout = Layout.V;
	private Style style;

	public Collection<T> getData() {
		return data;
	}

	public void setData(Collection<T> data) {
		this.data = data;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

}
