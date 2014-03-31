package brainstonemod.templates.bsp_filters;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public final class ReleaseFilter implements Filter {
	@Override
	public boolean isLoggable(LogRecord record) {
		final Level lvl = record.getLevel();

		return (lvl != Level.FINE) && (lvl != Level.FINER)
				&& (lvl != Level.FINEST) && (lvl != Level.INFO);
	}
}