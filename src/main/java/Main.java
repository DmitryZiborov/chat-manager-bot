import bot.UpdatesFlowProcessor;
import bot.core.Logger;
import bot.core.database.DataBaseManager;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;



public class Main implements Logger
{
    public static void main (String[] args)
    {
        DataBaseManager.setDbConnectionDbName (args[0]);
        DataBaseManager.setDbConnectionUserName (args[1]);
        DataBaseManager.setDbConnectionPassword (args[2]);
        DataBaseManager.setDbConnectionHost (args[3]);
        DataBaseManager.setDbConnectionPort (args[4]);
        UpdatesFlowProcessor.setBotToken ((args[5]));
        try
        {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new UpdatesFlowProcessor ());
        }
        catch (TelegramApiException e)
        {
            logger.error ("Error during bot initiation", e);
        }

    }
}
