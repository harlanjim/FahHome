package org.fah.house.domain.alexa;

public class Slots {
    private Room room;

    public Room getRoom ()
    {
        return room;
    }

    public void setRoom (Room Room)
    {
        this.room = Room;
    }

    @Override
    public String toString()
    {
        return "Slots [Room = "+room+"]";
    }
}
