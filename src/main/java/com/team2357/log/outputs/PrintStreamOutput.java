package com.systemmeltdown.robotlog.outputs;

import java.io.PrintStream;

public class PrintStreamOutput extends ThreadLogOutput {
	public PrintStreamOutput(final String prefix, final PrintStream stream) {
		super(new PrintStreamLogWriter(prefix, stream));
	}
}
