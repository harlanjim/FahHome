package org.fah.house.domain.alexa;

public class Intent {
    private Slots slots;

    private String name;

    public Slots getSlots ()
    {
        return slots;
    }

    public void setSlots (Slots slots)
    {
        this.slots = slots;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "Intent [slots = "+slots+", name = "+name+"]";
    }
}
