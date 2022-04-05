package bot.core.database;

import bot.core.entities.BotUser;
import bot.core.entities.BotUserRole;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class UserDataBaseManager extends DataBaseManager
{
    public static boolean addUser (BotUser user)
    {
        boolean userAdded = false;
        try (Connection connection = DataBaseManager.getDataSource ().getConnection ();
             PreparedStatement pstmtUser = connection.prepareStatement (DataBaseDAO.USER_INSERT);
             PreparedStatement pstmtUserRole = connection.prepareStatement (DataBaseDAO.USER_INSERT_ROLE))
        {
            pstmtUser.setLong (1, user.getID ());
            pstmtUser.setString (2, user.getUserName ());
            pstmtUser.setString (3, user.getFirstName ());
            pstmtUser.setString (4, user.getLastName ());
            pstmtUser.setBoolean (5, user.isBot ());
            userAdded = pstmtUser.executeUpdate () > 0;
            for (BotUserRole userRole : user.getRoles ())
            {
                pstmtUserRole.setLong (1, user.getID ());
                pstmtUserRole.setInt (2, userRole.getID ());
                pstmtUserRole.addBatch ();
            }
            userAdded = pstmtUserRole.executeBatch ().length > 0;
        }
        catch (SQLException throwables)
        {
            logger.error ("Error during adding user", throwables);
        }
        if (userAdded)
        {
            logger.debug ("User added " + user);
        }
        return userAdded;
    }

    public static boolean userExists (long userID)
    {
        boolean userExists = false;
        try (Connection connection = DataBaseManager.getDataSource ().getConnection ();
             PreparedStatement pstmt = connection.prepareStatement (DataBaseDAO.USER_EXISTS))
        {
            pstmt.setLong (1, userID);
            try (ResultSet rs = pstmt.executeQuery ())
            {
                userExists = rs.next ();
            }
        }
        catch (SQLException throwables)
        {
            logger.error ("Error during searching user", throwables);
        }
        if (userExists)
        {
            logger.debug ("User with id " + userID + " exists");
        }
        return userExists;
    }

    public static ArrayList<BotUserRole> getUserRoles (long userID)
    {
        ArrayList<BotUserRole> userRoles = new ArrayList<> ();
        try (Connection connection = DataBaseManager.getDataSource ().getConnection ();
             PreparedStatement pstmt = connection.prepareStatement (DataBaseDAO.USER_GET_ROLE))
        {
            pstmt.setLong (1, userID);
            try (ResultSet rs = pstmt.executeQuery ())
            {
                while (rs.next())
                {
                    userRoles.add (new BotUserRole (rs.getInt ("id_user_role")));
                }
            }
        }
        catch (SQLException throwables)
        {
            logger.error ("Error during searching user", throwables);
        }
        return userRoles;
    }

    public static String getAllUsersHTML (boolean forAdmin)
    {
        StringBuffer resultHTML = new StringBuffer ();
        try (Connection connection = DataBaseManager.getDataSource ().getConnection ();
             PreparedStatement pstmt = connection.prepareStatement (DataBaseDAO.USER_GET_ALL_INFO);
             ResultSet rs = pstmt.executeQuery ())
        {
            int counter = 1;
            SimpleDateFormat sdf = new SimpleDateFormat ("dd.MM.yyyy");
            while (rs.next ())
            {
                resultHTML.append ("<tr>\n");
                String someText;
                resultHTML.append ("<td>").append (String.valueOf (counter)).append ("</td>\n");
                resultHTML.append ("<td>").append (rs.getLong ("user_id")).append ("</td>\n");
                someText = rs.getString ("last_name");
                resultHTML.append ("<td>").append (someText == null ? "": someText).append ("</td>\n");
                someText = rs.getString ("first_name");
                resultHTML.append ("<td>").append (someText == null ? "": someText).append ("</td>\n");
                someText = rs.getString ("login");
                resultHTML.append ("<td>").append (someText == null ? "": someText).append ("</td>\n");
                Date someDate = rs.getDate ("birth_date");
                resultHTML.append ("<td>").append (someDate != null ? sdf.format (someDate) : "").append ("</td>\n");
                resultHTML.append ("<td>").append (rs.getInt ("mes_count")).append ("</td>\n");
                someDate = rs.getDate ("mes_date");
                resultHTML.append ("<td>").append (someDate != null ? sdf.format (someDate) : "").append ("</td>\n");
                resultHTML.append ("<td>").append ((rs.getBoolean ("is_bot")) ? "<input type=\"checkbox\" disabled checked>" : "").append ("</td>\n");
                resultHTML.append ("<td>").append ((rs.getInt ("role") == BotUserRole.ADMINISTRATOR_ROLE) ? "<input type=\"checkbox\" disabled checked>" : "").append ("</td>\n");
                someDate = rs.getDate ("ban_date");
                resultHTML.append ("<td>").append (someDate != null ? sdf.format (someDate) : "").append ("</td>\n");
                someText = rs.getString ("ban_reason");
                resultHTML.append ("<td>").append (someText == null ? "": someText).append ("</td>\n");
                resultHTML.append ("</tr>\n");
                counter++;
            }
        }
        catch (SQLException throwables)
        {
            logger.error ("Error during searching user", throwables);
        }
        return resultHTML.toString ();
    }

    public static boolean banUser (long userID, String reason)
    {
        boolean userBaned = false;
        try (Connection connection = DataBaseManager.getDataSource ().getConnection ();
             PreparedStatement pstmtBan = connection.prepareStatement (DataBaseDAO.BAN_LIST_INSERT))
        {
            pstmtBan.setLong (1, userID);
            pstmtBan.setTimestamp (2, Timestamp.valueOf(LocalDateTime.now ()));
            pstmtBan.setString (3, reason);
            userBaned = pstmtBan.executeUpdate () > 0;
        }
        catch (SQLException throwables)
        {
            logger.error ("Error during baning user", throwables);
        }
        if (userBaned)
        {
            logger.debug ("User baned " + userID);
        }
        return userBaned;
    }

    public static BotUser getUserByID (long userID)
    {
        BotUser botUser = null;
        try (Connection connection = DataBaseManager.getDataSource ().getConnection ();
             PreparedStatement pstmt = connection.prepareStatement (DataBaseDAO.USER_GET_BY_ID))
        {
            pstmt.setLong (1, userID);
            try (ResultSet rs = pstmt.executeQuery ())
            {
                while (rs.next())
                {
                    botUser = new BotUser (rs.getLong ("id"),
                            rs.getString ("user_name"),
                            rs.getString ("first_name"),
                            rs.getString ("last_name"),
                            rs.getBoolean ("is_bot"));
                }
            }
        }
        catch (SQLException throwables)
        {
            logger.error ("Error during baning user", throwables);
        }
        return botUser;
    }

}
