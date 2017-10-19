package org.fah.house.domain.alexa;

public class Application {
    private String applicationId;

    public String getApplicationId ()
    {
        return applicationId;
    }

    public void setApplicationId (String applicationId)
    {
        this.applicationId = applicationId;
    }

    @Override
    public String toString()
    {
        return "Application [applicationId = "+applicationId+"]";
    }
}
