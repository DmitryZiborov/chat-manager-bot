package bot.core.entities;

import java.time.LocalDateTime;
import java.util.Date;

public class BotChatMessage
{
    private long ID;
    private LocalDateTime sendDate;
    private String text = "";
    private BotUser user;
    private BotChat chat;
    public static final String MESSAGE_BOT_COMMAND = "bot_command";

    public BotChatMessage (long ID, LocalDateTime sendDate, String text, BotUser user, BotChat chat)
    {
        this.ID = ID;
        this.sendDate = sendDate;
        this.user = user;
        this.chat = chat;
        if (text != null && text.length () >= 100)
        {
            this.text = text.substring (0, 96) + "...";
        }
        else
        {
            this.text = text;
        }
    }

    public long getID ()
    {
        return ID;
    }

    public LocalDateTime getSendDate ()
    {
        return sendDate;
    }

    public String getText ()
    {
        return text;
    }

    public BotUser getUser ()
    {
        return user;
    }

    public BotChat getChat ()
    {
        return chat;
    }

    @Override
    public String toString ()
    {
        return "(ID:" + ID + "; sendDate:" + sendDate + "; text:" + text + "; userID:" + user.getID () + "; chatID:" + chat.getID () + ")";
    }
}
