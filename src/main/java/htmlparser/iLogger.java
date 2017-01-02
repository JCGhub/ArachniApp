package htmlparser;

public interface iLogger{
	public enum Level {DEBUG, INFO, WARNING, ERROR};
    
	public void log(String message);
	public void log(String message, Level level);
    
	public void setLevel(Level level);
	public Level getLevel();
}