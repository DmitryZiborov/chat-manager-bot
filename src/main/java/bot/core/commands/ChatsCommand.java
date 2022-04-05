package bot.core.commands;

import bot.core.Logger;
import bot.core.database.ChatsDataBaseManager;
import bot.core.database.DataBaseManager;
import bot.core.entities.BotChat;
import bot.core.entities.BotUser;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class ChatsCommand extends BasicCommand implements Logger
{
    private static final HashMap<String, String> htmlTemplates = new HashMap<>();
    private static final String HTML_DELIMITER = "<!--DELIMITER-->";
    private static final String HTML_TOP_KEY = "HTML-TOP";
    private static final String HTML_BOTTOM_KEY = "HTML-BOTTOM";
    private static final String PATH_TO_HTML_TEMPLATE = "/chats.html";
    @Override
    public void execute (BotChat chat, BotUser user, AbsSender sender) throws TelegramApiException
    {
        this.user = user;
        if(htmlTemplates.isEmpty ())
        {
            String HTMLTemplateContent = getHTMLTemplate ();
            int delimiterIndex = HTMLTemplateContent.indexOf (HTML_DELIMITER);
            htmlTemplates.put(HTML_TOP_KEY, HTMLTemplateContent.substring (0, delimiterIndex));
            htmlTemplates.put (HTML_BOTTOM_KEY, HTMLTemplateContent.substring (delimiterIndex, HTMLTemplateContent.length ()));
        }
        SendDocument document = new SendDocument ();
        document.setChatId (String.valueOf (chat.getID ()));
        document.setDocument (buildFileAnswer ());
        sender.execute (document);
    }

    private String getHTMLTemplate()
    {
        Path pathToHTMLTemplate = Paths.get (PATH_TO_HTML_TEMPLATE);
        String HTMLTemplateContent = "";
        try
        {
            HTMLTemplateContent = new String (getClass ().getResourceAsStream (PATH_TO_HTML_TEMPLATE).readAllBytes ());
        }
        catch (IOException e)
        {
            logger.error ("Error during reading " + PATH_TO_HTML_TEMPLATE, e);
        }
        return HTMLTemplateContent;
    }

    private InputFile buildFileAnswer ()
    {
        try
        {
            String HTMLMiddle;
            Path chatsListFilePath = Files.createTempFile (null, null);
            Files.write (chatsListFilePath, (htmlTemplates.get (HTML_TOP_KEY) + ChatsDataBaseManager.getAllChatsHTML (user.isAdmin ()) + htmlTemplates.get (HTML_BOTTOM_KEY)).getBytes (StandardCharsets.UTF_8));
            File chatsListFile = chatsListFilePath.toFile ();
            chatsListFile.deleteOnExit ();
            return new InputFile (chatsListFile, "Chats.html");
        }
        catch (IOException e)
        {
            logger.error ("Error during creation of chat file ", e);
        }
        return null;
    }
}
