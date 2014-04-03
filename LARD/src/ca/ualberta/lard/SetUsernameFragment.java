package ca.ualberta.lard;

import ca.ualberta.lard.model.User;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SetUsernameFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View v = inflater.inflate(R.layout.fragment_set_username, null);
		final TextView userNameEditTextView = (TextView) v.findViewById(R.id.usernameEditText);
		
		String currentUsername = getActivity().getSharedPreferences(User.PREFS_NAME, Context.MODE_PRIVATE).getString("username", null);
		if (currentUsername != null) {
			userNameEditTextView.setText(currentUsername);
		}
		
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(v);
		builder.setTitle(R.string.set_username);
		builder.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User clicked Set button
				// There must be text in the userNameEditTextView field for the Username to be valid
				if (userNameEditTextView.getText().toString().isEmpty()) {
					Toast.makeText(getActivity().getApplicationContext(), "Username can not be empty.", Toast.LENGTH_SHORT).show();
					return;
				}

				// Username may not contain "#" character
				if (userNameEditTextView.getText().toString().contains("#")) {
					Toast.makeText(getActivity().getApplicationContext(), "Username may not contain \"#\" character", Toast.LENGTH_SHORT).show();
					return;
				}

				Editor editor = getActivity().getSharedPreferences(User.PREFS_NAME, Context.MODE_PRIVATE).edit();
				editor.putString("username", userNameEditTextView.getText().toString());
				editor.commit();
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		});
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
