package bot.core.database;

public class DataBaseDAO
{
    public static final String CHAT_INSERT = "INSERT INTO public.chats (ID, ID_chat_type, title) VALUES (?, ?, ?)";
    public static final String CHAT_EXISTS = "SELECT 1 FROM public.chats WHERE ID = ?";
    public static final String CHATS_GET_NON_PRIVATE = "select id, id_chat_type, title from chats where id_chat_type != 1";
    public static final String USER_EXISTS = "SELECT 1 FROM public.users WHERE ID = ?";
    public static final String USER_INSERT = "INSERT INTO public.users (ID, user_name, first_name, last_name, is_bot) VALUES (?, ?, ?, ?, ?)";
    public static final String USER_INSERT_ROLE = "INSERT INTO public.users_user_roles (id_user, id_user_role) VALUES (?, ?)";
    public static final String USER_GET_BY_ID = "select * from users where id = ?";
    public static final String MESSAGE_INSERT = "INSERT INTO public.messages (ID, date, text, id_user, id_chat) VALUES (?, ?, ?, ?, ?)";
    public static final String CHATS_GET_ALL_FOR_HTML = "SELECT\n" + "    ch.id,\n" + "    ch.title,\n" + "    ch_tps.type,\n" + "    COUNT(ms.id) mes_count,\n" + "    MAX(ms.date) last_mess\n" + "FROM\n" + "    chats ch,\n" + "    chat_types ch_tps,\n" + "    messages ms\n" + "WHERE\n" + "    ms.id_chat = ch.id\n" + "    AND ch.id_chat_type = ch_tps.id\n" + "    AND ch.title is not null\n" + "GROUP BY\n" + "    ch.id,\n" + "    ch_tps.type\n" + "ORDER BY last_mess DESC";
    public static final String CHATS_GET_ALL_FOR_HTML_FOR_ADMIN = "SELECT\n" + "    ch.id,\n" + "    MAX(COALESCE(ch.title, concat('PRIVATE chat with ', us.user_name, '-', us.first_name, '-',  us.last_name))) title,\n" + "    ch_tps.type,\n" + "    COUNT(ms.id) mes_count,\n" + "    MAX(ms.date) last_mess\n" + "FROM\n" + "    chats ch,\n" + "    chat_types ch_tps,\n" + "    messages ms,\n" + "    users us\n" + "WHERE\n" + "    ms.id_chat = ch.id\n" + "    AND ch.id_chat_type = ch_tps.id\n" + "    AND us.id = ms.id_user\n" + "GROUP BY\n" + "    ch.id,\n" + "    ch_tps.type\n" + "ORDER BY last_mess DESC";
    public static final String USER_GET_ROLE = "select usr.id_user_role from users_user_roles usr where usr.id_user = ?";
    public static final String USER_GET_ALL_INFO = "SELECT\n" + "    us.id user_id,\n" + "    us.user_name login,\n" + "    us.first_name,\n" + "    us.last_name,\n" + "    us.is_bot,\n" + "    count(ms.id) mes_count,\n" + "    us.birth_date,\n" + "    max(bl.date)   ban_date,\n" + "    max(bl.reason) ban_reason,\n" + "    MIN(ur.id_user_role) role\n, max(ms.date) mes_date " + "FROM\n" + "    users_user_roles ur,\n" + "    users us\n" + "        LEFT JOIN ban_list bl ON us.id = bl.user_id\n" + "        LEFT JOIN messages ms ON us.id = ms.id_user\n" + "WHERE\n" + "    ur.id_user = us.id\n" + "GROUP BY\n" + "    us.id\n" + "ORDER BY mes_count DESC";
    public static final String BAN_LIST_INSERT = "INSERT INTO public.ban_list (user_id, date, reason) VALUES (?, ?, ?)";
}
