package com.learnitbro.testing.tool.window.jtextfield;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class JTextFieldNumberLimit extends PlainDocument {
	private int limit;

	public JTextFieldNumberLimit(int limit) {
		super();
		this.limit = limit;
	}

	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null)
			return;
		
		if (str != null &! str.matches("[-+]?\\d*\\.?\\d+"))
			return;

		if ((getLength() + str.length()) <= limit) {
			super.insertString(offset, str, attr);
		}
	}
}