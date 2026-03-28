package com.spacECE.spaceceedu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentProfile extends Fragment {

    private ImageView profileImage;
    private EditText imagePath;
    private Button uploadButton;
    private View contactUsContainer;
    private View contactUsText;
    private View menu_icon;

    private final ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    profileImage.setImageURI(uri);
                    imagePath.setText(uri.toString());
                    Toast.makeText(getContext(), "Image selected successfully", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = view.findViewById(R.id.child_profile_image);
        imagePath = view.findViewById(R.id.child_profile_image_path);
        uploadButton = view.findViewById(R.id.child_profile_upload_button);
        contactUsContainer = view.findViewById(R.id.profile_contact_us_container);
        contactUsText = view.findViewById(R.id.CantactUs);
        menu_icon = view.findViewById(R.id.profile_hamburger_menu);

        if (uploadButton != null) {
            uploadButton.setOnClickListener(v -> {
                try {
                    imagePickerLauncher.launch("image/*");
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error opening gallery", Toast.LENGTH_SHORT).show();
                }
            });
        }

        View.OnClickListener contactListener = v -> {
            openEmail();
        };

        if (contactUsContainer != null) {
            contactUsContainer.setOnClickListener(contactListener);
        }
        if (contactUsText != null) {
            contactUsText.setOnClickListener(contactListener);
        }

        if (menu_icon != null) {
            menu_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showHamburgerMenu(v);
                }
            });
        }

        return view;
    }

    private void showHamburgerMenu(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenu().add(1, 1, 1, "Contact Us");
        popup.getMenu().add(1, 2, 2, "About Us");
        popup.getMenu().add(1, 3, 3, "Privacy Policy");
        popup.getMenu().add(1, 4, 4, "Help");

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1: // Contact Us
                    case 4: // Help
                        openEmail();
                        return true;
                    case 2: // About Us
                        openUrl("https://www.spacece.in/about-us");
                        return true;
                    case 3: // Privacy Policy
                        openUrl("https://www.spacece.co/privacy-policy");
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void openEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:contact@spacece.in"));
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "No email app found", Toast.LENGTH_SHORT).show();
        }
    }
}
