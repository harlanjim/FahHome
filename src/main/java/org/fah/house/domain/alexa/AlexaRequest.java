package org.fah.house.domain.alexa;

public class AlexaRequest {
    private Session session;

    private Request request;

    private String version;

    public Session getSession ()
    {
        return session;
    }

    public void setSession (Session session)
    {
        this.session = session;
    }

    public Request getRequest ()
    {
        return request;
    }

    public void setRequest (Request request)
    {
        this.request = request;
    }

    public String getVersion ()
    {
        return version;
    }

    public void setVersion (String version)
    {
        this.version = version;
    }

    @Override
    public String toString()
    {
        return "AlexaRequest [session = "+session+", request = "+request+", version = "+version+"]";
    }
}
