package bot.core.database;

import bot.core.entities.BotChatMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class MessageDataBaseManager extends DataBaseManager
{
    public static boolean addMessage (BotChatMessage message)
    {
        boolean messageAdded = false;
        try (Connection connection = DataBaseManager.getDataSource ().getConnection ();
             PreparedStatement pstmtUser = connection.prepareStatement (DataBaseDAO.MESSAGE_INSERT))
        {
            pstmtUser.setLong (1, message.getID ());
            pstmtUser.setTimestamp (2, Timestamp.valueOf (message.getSendDate ()));
            pstmtUser.setString (3, message.getText ());
            pstmtUser.setLong (4, message.getUser ().getID ());
            pstmtUser.setLong (5, message.getChat ().getID ());
            messageAdded = pstmtUser.executeUpdate () > 0;
        }
        catch (SQLException throwables)
        {
            logger.error ("Error during adding message", throwables);
        }
        if (messageAdded)
        {
            logger.debug ("Message added " + message);
        }
        return messageAdded;
    }
}
