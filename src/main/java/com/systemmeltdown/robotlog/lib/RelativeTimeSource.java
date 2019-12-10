package com.systemmeltdown.robotlog.lib;

@FunctionalInterface
public interface RelativeTimeSource {
	public long convertToRelativeNanos(long nanos);
}
