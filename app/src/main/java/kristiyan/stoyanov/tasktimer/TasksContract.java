package kristiyan.stoyanov.tasktimer;

import static kristiyan.stoyanov.tasktimer.AppProvider.CONTENT_AUTHORITY;
import static kristiyan.stoyanov.tasktimer.AppProvider.CONTENT_AUTHORITY_URI;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class TasksContract {


    static final String Table_Name = "Tasks";

    // Tasks fields
    public static class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String TASKS_NAME = "Name";
        public static final String TASKS_DESCRIPTION = "Description";
        public static final String TASKS_SORTORDER = "SortOrder";

        private Columns() {
            // private constructor to prevent instantiation
        }
    }

    /**
     * The URI to access the Tasks table
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, Table_Name);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + Table_Name;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + Table_Name;

    static Uri buildTaskUri(long taskId) {

        return ContentUris.withAppendedId(CONTENT_URI, taskId);
    }

    static long getTaskId(Uri uri) {
        return ContentUris.parseId(uri);
    }

}




