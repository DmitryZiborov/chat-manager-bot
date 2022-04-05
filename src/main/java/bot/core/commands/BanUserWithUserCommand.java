package bot.core.commands;

import bot.UpdatesFlowProcessor;
import bot.core.Logger;
import bot.core.database.ChatsDataBaseManager;
import bot.core.database.DataBaseManager;
import bot.core.database.UserDataBaseManager;
import bot.core.entities.BotChat;
import bot.core.entities.BotUser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BanUserWithUserCommand extends BasicCommand implements Logger
{
    private static final String COMMAND_REGEX = "^(/ban_user:)(-?)\\d+\\s(reason:).+";
    private static final String ID_USER_REGEX = "^(/ban_user:)(-?)\\d+";
    private static final String COMMAND_START_REGEX = "^(/ban_user:)";
    private static final String REASON_START_REGEX = "^(/ban_user:)(-?)\\d+\\s(reason:)";
    private static final String SPAM_CHAT_ID = "-1001793556184";

    @Override
    public void execute (BotChat chat, BotUser user, AbsSender sender) throws TelegramApiException
    {
        this.user = user;
        SendMessage sendMessage = new SendMessage ();

        sendMessage.setChatId (String.valueOf (chat.getID ()));
        if (user.isAdmin ())
        {
            String commandText = (((UpdatesFlowProcessor) sender).getMessageFromUpdate ().getText ());
            if (isValidCommand (commandText))
            {
                String userIDToBan = "";
                String reason = "";
                Matcher matcherUserID = Pattern.compile (ID_USER_REGEX).matcher (commandText);
                Matcher matcherCommandStart = Pattern.compile (COMMAND_START_REGEX).matcher (commandText);
                if (matcherUserID.find () && matcherCommandStart.find ())
                {
                    userIDToBan = commandText.substring (matcherCommandStart.end (), matcherUserID.end ());
                    Matcher matcherReasonStart = Pattern.compile (REASON_START_REGEX).matcher (commandText);
                    if (matcherReasonStart.find ())
                    {
                        reason = commandText.substring (matcherReasonStart.end ());
                        UserDataBaseManager.banUser (Long.parseLong (userIDToBan), reason);
                        BanChatMember member;
                        boolean isBaned = false;
                        for (BotChat chatForBan : ChatsDataBaseManager.getAllNonPrivateChats ())
                        {
                            try
                            {
                                if (chatForBan.getID () != Long.parseLong (SPAM_CHAT_ID))
                                {
                                    member = new BanChatMember (String.valueOf (chatForBan.getID ()), Long.parseLong (userIDToBan));
                                    isBaned = sender.execute (member);
                                }
                            }
                            catch (TelegramApiException exception)
                            {
                                logger.error ("Error during ban user from chat" + chatForBan + exception.getMessage ());
                            }
                        }
                        BotUser userToBan = UserDataBaseManager.getUserByID (Long.parseLong (userIDToBan));
                        if (isBaned)
                        {
                            sendMessage.setText ("User " + userToBan + " successfully baned from all chats.");
                        }
                        else
                        {
                            sendMessage.setText ("User " + userToBan + " for some reason wasn't baned...");
                        }
                    }
                }
                else
                {
                    logger.error ("Can't find user id in command");
                }

            }
            else
            {
                sendMessage.setText (ERROR_COMMAND_FORMAT_ANSWER);
            }
        }
        else

        {
            sendMessage.setText (ERROR_GRANTS_ANSWER);
        }
        sender.execute (sendMessage);

    }

    private boolean isValidCommand (String commandText)
    {
        return Pattern.compile (COMMAND_REGEX).matcher (commandText).find ();
    }
}
