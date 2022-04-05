package bot.core.commands;

import bot.core.entities.BotChat;
import bot.core.entities.BotUser;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class BasicCommand
{
    public static final String CHATS_COMMAND_NAME = "/chats";
    public static final String USERS_COMMAND_NAME = "/users";
    public static final String BAN_USER_COMMAND_NAME = "/ban";
    public static final String BAN_USER_WITH_USER_COMMAND_NAME = "/ban_user";
    protected static final String ERROR_GRANTS_ANSWER = "Ooops. You don't have enough grants to execute this command. Don't get upset :)";
    protected static final String ERROR_COMMAND_FORMAT_ANSWER = "Your command format is not fully correct :(\nPlease read example above more accurately and send me command again.\nI have faith in you!";

    public BotUser user;

    public abstract void execute(BotChat chat, BotUser user, AbsSender sender) throws TelegramApiException;
}
