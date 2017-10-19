package org.fah.house.domain.alexa;

public class Session {
	private String newFlag;

    private Application application;

    private String sessionId;

    private Attribute attributes;

    private User user;

    public Application getApplication ()
    {
        return application;
    }

    public void setApplication (Application application)
    {
        this.application = application;
    }

    public String getSessionId ()
    {
        return sessionId;
    }

    public void setSessionId (String sessionId)
    {
        this.sessionId = sessionId;
    }

    public Attribute getAttributes ()
    {
        return attributes;
    }

    public void setAttributes (Attribute attributes)
    {
        this.attributes = attributes;
    }

    public User getUser ()
    {
        return user;
    }

    public void setUser (User user)
    {
        this.user = user;
    }

	public String getNewFlag() {
		return newFlag;
	}

	public void setNewFlag(String newFlag) {
		this.newFlag = newFlag;
	}
	
    @Override
    public String toString()
    {
        return "Session [newFlag = "+newFlag+", application = "+application+", sessionId = "+sessionId+", attributes = "+attributes+", user = "+user+"]";
    }

}
