package bot.core.entities;

public class BotChat
{
    private long ID;
    private byte type;
    private String title;
    public static final byte PRIVATE_CHAT_TYPE = 1;
    public static final String PRIVATE_CHAT_TYPE_STRING = "private";
    public static final byte GROUP_CHAT_TYPE = 2;
    public static final String GROUP_CHAT_TYPE_STRING = "group";
    public static final byte SUPER_GROUP_CHAT_TYPE = 3;
    public static final String SUPER_GROUP_CHAT_TYPE_STRING = "supergroup";
    public static final byte CHANNEL_CHAT_TYPE = 4;
    public static final String CHANNEL_CHAT_TYPE_STRING = "channel";


    public BotChat (long ID, String type, String title)
    {
        switch (type)
        {
            case PRIVATE_CHAT_TYPE_STRING:
                this.type = PRIVATE_CHAT_TYPE;
                break;
            case GROUP_CHAT_TYPE_STRING:
                this.type = GROUP_CHAT_TYPE;
                break;
            case SUPER_GROUP_CHAT_TYPE_STRING:
                this.type = SUPER_GROUP_CHAT_TYPE;
                break;
            case CHANNEL_CHAT_TYPE_STRING:
                this.type = CHANNEL_CHAT_TYPE;
                break;
            default:
                this.type = 0;
                break;
        }
        this.ID = ID;
        this.title = title;
    }

    public BotChat (long ID, byte type, String title)
    {
        this.type = type;
        this.ID = ID;
        this.title = title;
    }

    public long getID ()
    {
        return ID;
    }

    public byte getType ()
    {
        return type;
    }

    public String getTitle ()
    {
        return title;
    }

    @Override
    public String toString ()
    {
        return "(ID:" + ID + "; type:" + type + "; title:" + title + ")";
    }
}
