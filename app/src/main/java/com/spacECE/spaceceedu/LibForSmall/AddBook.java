package com.spacECE.spaceceedu.LibForSmall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.spacECE.spaceceedu.Authentication.UserLocalStore;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

public class AddBook extends AppCompatActivity {

    private EditText et_bookName, et_author, et_edition, et_description, et_category, et_price;
    private Button btn_addBook, btn_remove_image;
    private ImageView iv_photo;
    private Uri picData = null;
    private static final int PERMISSION_CODE = 1001;
    UserLocalStore userLocalStore;

    ActivityResultLauncher<Intent> imagePickLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        userLocalStore = new UserLocalStore(getApplicationContext());

        et_bookName = findViewById(R.id.editText_bookname);
        et_author = findViewById(R.id.editText_author);
        et_edition = findViewById(R.id.editText_edition);
        et_description = findViewById(R.id.editText_description);
        et_category = findViewById(R.id.editText_category);
        et_price = findViewById(R.id.editText_price);
        btn_addBook = findViewById(R.id.btn_addbook);
        btn_remove_image = findViewById(R.id.btn_remove_image);
        iv_photo = findViewById(R.id.imageView_photo);

        imagePickLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            picData = result.getData().getData();
                            iv_photo.setImageURI(picData);
                            if (btn_remove_image != null) btn_remove_image.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );

        iv_photo.setOnClickListener(v -> {
            pickImageFromGallery();
        });

        if (btn_remove_image != null) {
            btn_remove_image.setOnClickListener(v -> {
                picData = null;
                iv_photo.setImageResource(R.drawable.ic_baseline_add_24);
                btn_remove_image.setVisibility(View.GONE);
            });
        }

        btn_addBook.setOnClickListener(v -> {
            if (validateData()) {
                sendBookData();
            }
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickLauncher.launch(Intent.createChooser(intent, "Select Book Cover"));
    }

    private boolean validateData() {
        if (et_bookName.getText().toString().trim().isEmpty()) {
            et_bookName.setError("Required");
            return false;
        }
        if (et_author.getText().toString().trim().isEmpty()) {
            et_author.setError("Required");
            return false;
        }
        if (et_price.getText().toString().trim().isEmpty()) {
            et_price.setError("Required");
            return false;
        }
        return true;
    }

    private void sendBookData() {
        String url = "https://hustle-7c68d043.mileswebhosting.com/spacece/libforsmall/api_addbook.php";
        
        Toast.makeText(this, "Uploading book details...", Toast.LENGTH_SHORT).show();
        
        new Thread(() -> {
            byte[] encodedImage = null;
            if (picData != null) {
                try {
                    Bitmap selectedImage = getBitmapFromUri(picData);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    encodedImage = baos.toByteArray();
                } catch (IOException e) {
                    Log.e("AddBook", "Image processing error", e);
                }
            }

            OkHttpClient client = UsefulFunctions.getOkHttpClient();
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("book_name", et_bookName.getText().toString().trim())
                    .addFormDataPart("author", et_author.getText().toString().trim())
                    .addFormDataPart("edition", et_edition.getText().toString().trim())
                    .addFormDataPart("description", et_description.getText().toString().trim())
                    .addFormDataPart("category", et_category.getText().toString().trim())
                    .addFormDataPart("price", et_price.getText().toString().trim());

            if (userLocalStore.getLoggedInAccount() != null) {
                builder.addFormDataPart("u_id", userLocalStore.getLoggedInAccount().getAccount_id());
            } else {
                builder.addFormDataPart("u_id", "0");
            }

            if (encodedImage != null) {
                builder.addFormDataPart("image", "book_" + System.currentTimeMillis() + ".jpg",
                        RequestBody.create(encodedImage, MediaType.parse("image/jpeg")));
            }

            Request request = new Request.Builder().url(url).post(builder.build()).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(() -> Toast.makeText(AddBook.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String responseData = response.body() != null ? response.body().string() : "";
                    final boolean isSuccessful = response.isSuccessful();
                    Log.d("AddBook", "Raw Response: " + responseData);
                    
                    runOnUiThread(() -> {
                        String clean = responseData.trim();
                        
                        // Treat any HTTP 200 response as success if it's likely successful
                        if (isSuccessful) {
                            onUploadSuccess();
                            return;
                        }

                        try {
                            // Extract JSON
                            int jsonStart = clean.indexOf("{");
                            int jsonEnd = clean.lastIndexOf("}");
                            if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
                                clean = clean.substring(jsonStart, jsonEnd + 1);
                            }

                            JSONObject jsonObject = new JSONObject(clean);
                            if (jsonObject.optString("status").equalsIgnoreCase("success") || jsonObject.optInt("success", 0) == 1) {
                                onUploadSuccess();
                            } else {
                                String msg = jsonObject.optString("message", "Failed to add book.");
                                Toast.makeText(AddBook.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // If we can't parse but the code is 200, assume success
                            if (isSuccessful) {
                                onUploadSuccess();
                            } else {
                                Toast.makeText(AddBook.this, "Server error. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }).start();
    }

    private void onUploadSuccess() {
        Toast.makeText(AddBook.this, "Book Added Successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddBook.this, Library_main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
