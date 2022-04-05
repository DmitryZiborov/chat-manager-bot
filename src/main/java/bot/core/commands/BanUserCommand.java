package bot.core.commands;

import bot.core.Logger;
import bot.core.entities.BotChat;
import bot.core.entities.BotUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

public class BanUserCommand extends BasicCommand implements Logger
{
    private static final String PROVIDE_USERID_ANSWER = "Please send me user id and ban reason by following text </ban_user:[user_id] reason:[reason]>\nFor example, /ban_user:323637632 reason:Leaved company";
    @Override

    public void execute (BotChat chat, BotUser user, AbsSender sender) throws TelegramApiException
    {
        this.user = user;
        if (user.isAdmin ())
        {
            SendMessage sendMessage = new SendMessage ();
            sendMessage.setChatId (String.valueOf (chat.getID ()));
            sendMessage.setText (PROVIDE_USERID_ANSWER);
            ArrayList<MessageEntity> htmlMess = new ArrayList<> ();
            htmlMess.add (new MessageEntity ("bold", PROVIDE_USERID_ANSWER.indexOf ("/ban"), 35));
            sendMessage.setEntities (htmlMess);
            sender.execute (sendMessage);
        }
        else
        {
            SendMessage sendMessage = new SendMessage ();
            sendMessage.setChatId (String.valueOf (chat.getID ()));
            sendMessage.setText (ERROR_GRANTS_ANSWER);
            sender.execute (sendMessage);
        }
    }
}
