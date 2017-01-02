package htmlparser;

public class DefaultLogger implements iLogger{
	Level currLevel = Level.ERROR;
    
	protected boolean isOutputEnabledFor(Level level){
		switch(currLevel){
			case DEBUG:         // all messages
				return true;
			case INFO:          // all messages but debugging
				if(level == Level.DEBUG){ 
					return false; 
				}
				return true;
			case WARNING:       // all warnings or errors
				if(level == Level.INFO || level == Level.DEBUG){ 
					return false; 
				}
				return true;
			case ERROR:
				if(level == Level.INFO || level == Level.DEBUG || level == Level.WARNING){ 
					return false; 
				}
				return true;
		}
		return false;
	}

	public void log(String message){
		System.out.println(message);
	}

	public void log(String message, Level level){
		if(isOutputEnabledFor(level)){
			System.out.println(message);
		}
	}

	public void setLevel(Level level){
		currLevel = level;
	}

	public Level getLevel(){
		return currLevel;
	}    
}