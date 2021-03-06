package ca.ualberta.lard;

import ca.ualberta.lard.model.User;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This username fragment is used to let the user set his username. it first inflates 
 * the layout, then sets up a builder so that the user can input his desired username.
 */
public class SetUsernameFragment extends DialogFragment {
	private User user;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		user = new User(getActivity().getSharedPreferences(User.PREFS_NAME, Context.MODE_PRIVATE));
		View v = inflater.inflate(R.layout.fragment_set_username, null);
		final TextView userNameEditTextView = (TextView) v.findViewById(R.id.usernameEditText);
		userNameEditTextView.setText(user.getUsernameWithoutHash());
		
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
				
				user.setUsername(userNameEditTextView.getText().toString());
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
