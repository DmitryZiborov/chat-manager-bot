package bot;

import bot.core.Logger;
import bot.core.commands.*;
import bot.core.database.ChatsDataBaseManager;
import bot.core.database.MessageDataBaseManager;
import bot.core.database.UserDataBaseManager;
import bot.core.entities.BotChat;
import bot.core.entities.BotChatMessage;
import bot.core.entities.BotUser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class UpdatesFlowProcessor extends TelegramLongPollingBot implements Logger
{
    private HashMap<String, BasicCommand> commands;
    private static final String BOT_NAME = "ChatManagerZBot";
    private static String BOT_TOKEN ="";
    Message messageFromUpdate;

    public UpdatesFlowProcessor ()
    {
        super ();
        commands = new HashMap<> ();
        commands.put (BasicCommand.CHATS_COMMAND_NAME, new ChatsCommand ());
        commands.put(BasicCommand.USERS_COMMAND_NAME, new UsersCommand ());
        commands.put(BasicCommand.BAN_USER_COMMAND_NAME, new BanUserCommand ());
        commands.put(BasicCommand.BAN_USER_WITH_USER_COMMAND_NAME, new BanUserWithUserCommand ());
    }

    public String getBotToken ()
    {
        return BOT_TOKEN;
    }

    public static void setBotToken (String botToken)
    {
        BOT_TOKEN = botToken;
    }

    public String getBotUsername ()
    {
        return BOT_NAME;
    }


    public void onUpdateReceived (Update update)
    {
        logger.debug ("Update received " + update);
        messageFromUpdate = update.getMessage ();
        if (messageFromUpdate != null && messageFromUpdate.getChat () != null)
        {
            BotChat chat = new BotChat (messageFromUpdate.getChatId (), messageFromUpdate.getChat ().getType (), messageFromUpdate.getChat ().getTitle ());
            if (!ChatsDataBaseManager.chatExists (messageFromUpdate.getChatId ()))
            {
                ChatsDataBaseManager.addChat (chat);
            }
            BotUser user = null;
            if (messageFromUpdate.getFrom () != null)
            {
                user = new BotUser (messageFromUpdate.getFrom ().getId (), messageFromUpdate.getFrom ().getUserName (), messageFromUpdate.getFrom ().getFirstName (), messageFromUpdate.getFrom ().getLastName (), messageFromUpdate.getFrom ().getIsBot ());
                if (!UserDataBaseManager.userExists (user.getID ()))
                {
                    UserDataBaseManager.addUser (user);
                }
                else
                {
                    user.setRoles (UserDataBaseManager.getUserRoles(user.getID ()));
                }
                BotChatMessage message = new BotChatMessage (messageFromUpdate.getMessageId (), LocalDateTime.ofInstant (Instant.ofEpochSecond (messageFromUpdate.getDate ()), ZoneId.systemDefault ()), messageFromUpdate.getText (), user, chat);
                MessageDataBaseManager.addMessage (message);
            }
            executeCommand (messageFromUpdate.getEntities (), chat, user);
        }
    }

    private void executeCommand (List<MessageEntity> messageEntites, BotChat chat, BotUser user)
    {
        if (messageEntites == null || messageEntites.size () == 0)
        {
            return;
        }
        for (MessageEntity messageEntity : messageEntites)
        {
            if (BotChatMessage.MESSAGE_BOT_COMMAND.equals (messageEntity.getType ()))
            {
                try
                {
                    BasicCommand command = commands.get (messageEntity.getText ());
                    if (command != null)
                    {
                        command.execute (chat, user, this);
                    }
                }
                catch (TelegramApiException e)
                {
                    logger.error ("Error during execution command ", e);
                }
            }
        }
    }

    public Message getMessageFromUpdate ()
    {
        return messageFromUpdate;
    }
}
