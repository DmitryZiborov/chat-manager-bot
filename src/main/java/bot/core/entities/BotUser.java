package bot.core.entities;

import java.util.ArrayList;
import java.util.Date;

public class BotUser
{
    private long ID;
    private String userName;
    private String firstName;
    private String lastName;
    private boolean isBot;
    private Date birthDate;
    private ArrayList<BotUserRole> roles;

    public BotUser (long ID, String userName, String firstName, String lastName, boolean isBot)
    {
        this.ID = ID;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isBot = isBot;
        this.roles = BotUserRole.USER_ROLE_LIST;
    }

    public long getID ()
    {
        return ID;
    }

    public String getUserName ()
    {
        return userName;
    }

    public String getFirstName ()
    {
        return firstName;
    }

    public String getLastName ()
    {
        return lastName;
    }

    public boolean isBot ()
    {
        return isBot;
    }

    public Date getBirthDate ()
    {
        return birthDate;
    }

    public ArrayList<BotUserRole> getRoles ()
    {
        return roles;
    }

    public void setRoles (ArrayList<BotUserRole> roles)
    {
        this.roles = roles;
    }

    public boolean isAdmin()
    {
        boolean isAdmin = false;
        for(BotUserRole userRole: roles)
        {
            isAdmin = (userRole.getID () == BotUserRole.ADMINISTRATOR_ROLE);
        }
        return isAdmin;
    }

    @Override
    public String toString ()
    {
        return "(ID:" + ID + "; userName:" + userName + "; firstName:" + firstName + "; lastName:" + lastName + ")";
    }
}
