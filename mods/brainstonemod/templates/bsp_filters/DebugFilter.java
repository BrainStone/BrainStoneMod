package mods.brainstonemod.templates.bsp_filters;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public final class DebugFilter implements Filter {
	@Override
	public boolean isLoggable(LogRecord record) {
		return true;
	}
}