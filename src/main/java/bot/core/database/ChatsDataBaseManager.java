package bot.core.database;

import bot.core.entities.BotChat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatsDataBaseManager extends DataBaseManager
{
    private static final String CHATS_HTML_COUNTER_SYMBOL = "#counter#";

    public static boolean addChat (BotChat chat)
    {
        boolean chatAdded = false;
        try (Connection connection = DataBaseManager.getDataSource ().getConnection ();
             PreparedStatement pstmt = connection.prepareStatement (DataBaseDAO.CHAT_INSERT))
        {
            pstmt.setLong (1, chat.getID ());
            pstmt.setByte (2, chat.getType ());
            pstmt.setString (3, chat.getTitle ());
            chatAdded = pstmt.executeUpdate () > 0;
        }
        catch (SQLException throwables)
        {
            logger.error ("Error during adding chat", throwables);
        }
        if (chatAdded)
        {
            logger.debug ("Chat added " + chat);
        }
        return chatAdded;
    }

    public static boolean chatExists (long chatID)
    {
        boolean chatExists = false;
        try (Connection connection = DataBaseManager.getDataSource ().getConnection ();
             PreparedStatement pstmt = connection.prepareStatement (DataBaseDAO.CHAT_EXISTS))
        {
            pstmt.setLong (1, chatID);
            try (ResultSet rs = pstmt.executeQuery ())
            {
                chatExists = rs.next ();
            }
        }
        catch (SQLException throwables)
        {
            logger.error ("Error during searching chat", throwables);
        }
        if (chatExists)
        {
            logger.debug ("Chat with id " + chatID + " exists");
        }
        return chatExists;
    }

    public static String getAllChatsHTML (boolean forAdmin)
    {
        String query = forAdmin ? DataBaseDAO.CHATS_GET_ALL_FOR_HTML_FOR_ADMIN : DataBaseDAO.CHATS_GET_ALL_FOR_HTML;
        StringBuffer resultHTML = new StringBuffer ();
        try (Connection connection = DataBaseManager.getDataSource ().getConnection ();
             PreparedStatement pstmt = connection.prepareStatement (query);
             ResultSet rs = pstmt.executeQuery ())
        {
            ArrayList<String> MDChats = new ArrayList ();
            ArrayList<String> ERISChats = new ArrayList ();
            ArrayList<String> otherChats = new ArrayList ();
            while (rs.next ())
            {
                StringBuffer HTMLRow = new StringBuffer ();
                HTMLRow.append ("<tr>\n");
                HTMLRow.append ("<td>").append (CHATS_HTML_COUNTER_SYMBOL).append ("</td>\n");
                HTMLRow.append ("<td>").append (rs.getLong ("id")).append ("</td>\n");
                String titleChat = rs.getString ("title");
                HTMLRow.append ("<td>").append (titleChat).append ("</td>\n");
                HTMLRow.append ("<td>").append (rs.getString ("type")).append ("</td>\n");
                HTMLRow.append ("<td>").append (rs.getInt ("mes_count")).append ("</td>\n");
                HTMLRow.append ("<td>").append (new SimpleDateFormat ("dd.MM.yyyy").format (rs.getDate ("last_mess"))).append ("</td>\n");
                HTMLRow.append ("</tr>\n");
                if (titleChat.startsWith ("МД") || titleChat.contains ("олголетие"))
                {
                    MDChats.add (HTMLRow.toString ());
                }
                else
                {
                    if (titleChat.startsWith ("ЕРИС"))
                    {
                        ERISChats.add (HTMLRow.toString ());
                    }
                    else
                    {
                        otherChats.add (HTMLRow.toString ());
                    }
                }
            }
            int counter = 1;
            for (String MDRow : MDChats)
            {
                if (counter == 1)
                {
                    resultHTML.append ("<tr><td colspan=\"10\"><b>Московское долголетие</b></td></tr>\n");
                }
                resultHTML.append (MDRow.replace (CHATS_HTML_COUNTER_SYMBOL, String.valueOf (counter)));
                counter++;
            }
            counter = 1;
            for (String ERISRow : ERISChats)
            {
                if (counter == 1)
                {
                    resultHTML.append ("<tr><td colspan=\"10\"><b>ЕРИС</b></td></tr>\n");
                }
                resultHTML.append (ERISRow.replace (CHATS_HTML_COUNTER_SYMBOL, String.valueOf (counter)));
                counter++;
            }
            counter = 1;
            for (String otherROW : otherChats)
            {
                if (counter == 1)
                {
                    resultHTML.append ("<tr><td colspan=\"10\"><b>Остальное</b></td></tr>\n");
                }
                resultHTML.append (otherROW.replace (CHATS_HTML_COUNTER_SYMBOL, String.valueOf (counter)));
                counter++;
            }
        }
        catch (SQLException throwables)
        {
            logger.error ("Error during searching user", throwables);
        }
        return resultHTML.toString ();
    }

    public static List<BotChat> getAllNonPrivateChats ()
    {
        List<BotChat> chats = new ArrayList ();
        try (Connection connection = DataBaseManager.getDataSource ().getConnection ();
             PreparedStatement pstmt = connection.prepareStatement (DataBaseDAO.CHATS_GET_NON_PRIVATE);
             ResultSet rs = pstmt.executeQuery ())
        {
            while (rs.next ())
            {
                long chatID = rs.getLong ("id");
                byte chatType = rs.getByte ("id_chat_type");
                String chatTitle = rs.getString ("title");
                chats.add (new BotChat (chatID, chatType, chatTitle));
            }
        }
        catch (SQLException throwables)
        {
            logger.error ("Error during getting chats", throwables);
        }
        return chats;
    }
}
