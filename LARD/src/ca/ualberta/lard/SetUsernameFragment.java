package ca.ualberta.lard;

import ca.ualberta.lard.model.User;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SetUsernameFragment extends Fragment {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_username, container, false);
    }
	
	public void onClickCancelButton(View v) {
		getActivity().finish();
	}
	
	public void onClickSelectButton(View v) {
		TextView userNameEditTextView = (TextView) getActivity().findViewById(R.id.usernameEditText);
		// There must be text in the bodyTextEditTextView field for the comment to be valid
		if (userNameEditTextView.getText().toString().isEmpty()) {
			Toast.makeText(getActivity().getApplicationContext(), "Missing comment text.", Toast.LENGTH_SHORT).show();
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
		
		getActivity().finish();
	}
}
