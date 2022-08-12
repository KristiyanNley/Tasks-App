package kristiyan.stoyanov.tasktimer;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import kristiyan.stoyanov.tasktimer.databinding.FragmentAddEditBinding;

public class AddEditActivityFragment extends Fragment {

    private static final String TAG = "AddEditActivityFragment";

    public enum FragmentEditMode {EDIT, ADD}

    private FragmentEditMode mMode;

    private EditText mNameTextView;
    private EditText mDescriptionTextView;
    private EditText mSortOrderTextView;
    private Button mSaveButton;

    private FragmentAddEditBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit, container, false);

        mNameTextView = (EditText) view.findViewById(R.id.addedit_name);
        mDescriptionTextView = (EditText) view.findViewById(R.id.addedit_description);
        mSortOrderTextView = (EditText) view.findViewById(R.id.addedit_sortorder);
        mSaveButton = (Button) view.findViewById(R.id.addedit_save);

        Bundle arguments = getActivity().getIntent().getExtras();

        final Task task;
        if (arguments != null) {
            Log.d(TAG, "onCreateView: retrieving task details.");
            task = (Task) arguments.getSerializable(Task.class.getSimpleName());
            if (task != null) {
                Log.d(TAG, "onCreateView: Task details found, editing...");
                mNameTextView.setText(task.getName());
                mDescriptionTextView.setText(task.getDescription());
                mSortOrderTextView.setText(Integer.toString(task.getSortOrder()));
                mMode = FragmentEditMode.EDIT;
            } else {
                // no task so we must create/ add a new one
                mMode = FragmentEditMode.ADD;
            }
        } else {
            task = null;
            Log.d(TAG, "onCreateView: no arguments, adding a new record");
            mMode = FragmentEditMode.ADD;
        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update the database if at least one field has been changed.
                // - there's no reason to hit the database unless this has happened.
                int so; // to save repeated conversions to int;
                if (mSortOrderTextView.length() > 0) {
                    so = Integer.parseInt(mSortOrderTextView.getText().toString());
                } else {
                    so = 0;
                }
                ContentResolver contentResolver = requireActivity().getContentResolver();
                ContentValues contentvalues = new ContentValues();

                switch (mMode) {
                    case EDIT:
                        if (!mNameTextView.getText().toString().equals(task.getName())) {
                            contentvalues.put(TasksContract.Columns.TASKS_NAME, mNameTextView.getText().toString());
                        }
                        if (!mDescriptionTextView.getText().toString().equals(task.getDescription())) {
                            contentvalues.put(TasksContract.Columns.TASKS_DESCRIPTION, mDescriptionTextView.getText().toString());
                        }
                        if (so != task.getSortOrder()) {
                            contentvalues.put(TasksContract.Columns.TASKS_SORTORDER, so);
                        }
                        if (contentvalues.size() != 0) {
                            Log.d(TAG, "onClick: updating task");
                            contentResolver.update(TasksContract.buildTaskUri(task.getId()), contentvalues, null, null);
                        }
                        break;
                    case ADD:
                        if (mNameTextView.length() > 0) {
                            Log.d(TAG, "onClick: adding new task");
                            contentvalues.put(TasksContract.Columns.TASKS_NAME, mNameTextView.getText().toString());
                            contentvalues.put(TasksContract.Columns.TASKS_DESCRIPTION, mDescriptionTextView.getText().toString());
                            contentvalues.put(TasksContract.Columns.TASKS_SORTORDER, so);
                            contentResolver.insert(TasksContract.CONTENT_URI, contentvalues);
                        }
                        break;
                }
                Log.d(TAG, "onClick: done editing");
            }
        });
        Log.d(TAG, "onCreateView: exiting");

        return view;
    }
}