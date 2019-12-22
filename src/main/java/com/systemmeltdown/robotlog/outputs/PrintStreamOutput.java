package com.systemmeltdown.robotlog.outputs;

import java.io.PrintStream;

public class PrintStreamOutput extends SimpleLogOutput {
	public PrintStreamOutput(final PrintStream stream) {
		super(new PrintStreamLogWriter(stream));
	}
}
