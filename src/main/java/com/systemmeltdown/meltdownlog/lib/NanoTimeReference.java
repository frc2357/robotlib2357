package com.systemmeltdown.meltdownlog.lib;

/**
 * Holds a relative time reference.
 */
public interface NanoTimeReference {
	public long convertNanosToRelative(long nanos);
}