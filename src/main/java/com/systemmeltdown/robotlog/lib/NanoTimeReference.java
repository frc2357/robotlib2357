package com.systemmeltdown.robotlog.lib;

/**
 * Holds a relative time reference.
 */
public interface NanoTimeReference {
	public long convertNanosToRelative(long nanos);
}