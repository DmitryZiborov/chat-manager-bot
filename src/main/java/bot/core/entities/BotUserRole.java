package bot.core.entities;

import java.util.ArrayList;
import java.util.Arrays;

public class BotUserRole
{
    private int ID;
    public static byte ADMINISTRATOR_ROLE = 0;
    public static byte USER_ROLE = 1;
    public static final ArrayList<BotUserRole> USER_ROLE_LIST = new ArrayList (Arrays.asList(new BotUserRole (USER_ROLE)));

    public BotUserRole (int ID)
    {
        this.ID = ID;
    }

    public int getID ()
    {
        return ID;
    }

}
