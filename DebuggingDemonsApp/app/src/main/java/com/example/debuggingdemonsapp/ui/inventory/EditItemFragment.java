package com.example.debuggingdemonsapp.ui.inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.debuggingdemonsapp.MainActivity;
import com.example.debuggingdemonsapp.R;
import com.example.debuggingdemonsapp.model.Photograph;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

/**
 * Fragment for editing item details.
 */
public class EditItemFragment extends Fragment {
    private EditText DoP;
    private EditText Description;
    private EditText Make;
    private EditText Model;
    private EditText SerialNumber;
    private EditText EstimatedValue;
    private EditText Comment;
    private ImageButton imageButton1;
    private MutableLiveData<Photograph> liveData1;
    private String image1;
    private String image2;
    private String image3;
    private ImageButton imageButton2;
    private MutableLiveData<Photograph> liveData2;
    private ImageButton imageButton3;
    private MutableLiveData<Photograph> liveData3;

    private FirebaseFirestore db;
    private CollectionReference itemsRef;
    private int GALLERY_REQUEST_CODE = 203;

    /**
     * Default constructor for EditItemFragment.
     */
    public EditItemFragment() {

    }

    public void onSelectImageClick(View view) {
        // Create an AlertDialog to show the options
        final CharSequence[] options = {"Choose from Gallery", "Choose from Photo List", "Remove Photo"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY_REQUEST_CODE); // You need to define this constant
                } else if (options[item].equals("Choose from Photo List")) {
                    // You need to define this action in your navigation graph
                    Bundle bundle = new Bundle();
                    bundle.putInt("button", view.getId());
                    Navigation.findNavController(view).navigate(R.id.action_navigation_editItem_to_navigation_photosList,bundle);
                } else if (options[item].equals("Remove Photo")){
                    ImageButton imageButton = (ImageButton) view;
                    imageButton.setImageDrawable(null);

                }
            }
        });
        builder.show();
    }

    /**
     * Creates and returns the view associated with the fragment.
     * @param inflater           LayoutInflater that can be used to inflate views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment will be re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    // Initialize Database and collection reference
        InventoryViewModel viewModel = new ViewModelProvider(requireActivity(),new InventoryViewModelFactory(((MainActivity)getActivity()).current_user)).get(InventoryViewModel.class);
        db = FirebaseFirestore.getInstance();
        itemsRef = db.collection("users"+"/"+((MainActivity)getActivity()).current_user+"/"+"items");
    // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.item_edit, container, false);
        DoP = view.findViewById(R.id.dateOfPurchase);
        Description = view.findViewById(R.id.description);
        Make = view.findViewById(R.id.make);
        Model = view.findViewById(R.id.model);
        SerialNumber = view.findViewById(R.id.serial_number);
        EstimatedValue = view.findViewById(R.id.estimated_value);
        Comment = view.findViewById(R.id.comment);

        imageButton1 = view.findViewById(R.id.editImage1);
        imageButton2 = view.findViewById(R.id.editImage2);
        imageButton3 = view.findViewById(R.id.editImage3);

        Button confirmButton = view.findViewById(R.id.confirm_button);
        Button cancelButton = view.findViewById(R.id.back_button);

        // When any of the three image buttons are pressed in the edit page the image is removed from the item

        imageButton1.setOnClickListener(this::onSelectImageClick);
        imageButton2.setOnClickListener(this::onSelectImageClick);
        imageButton3.setOnClickListener(this::onSelectImageClick);

        getChosenImage(container);
        // Retrieve data from bundle if not null and initialize the UI
        Bundle bundle = getArguments();
        if (bundle != null) {
            String doP = bundle.getString("doP");
            String description = bundle.getString("description");
            String make = bundle.getString("make");
            String model = bundle.getString("model");
            String serialNumber = bundle.getString("serialNumber");
            String estimatedValue = bundle.getString("estimatedValue");
            String comment = bundle.getString("comment");

            // Gets image Uri's from the bundle for images that exist
            // If an image wasn't selected in the add menu then the string is empty (i.e. the object would be null)
            image1 = bundle.getString("image1");
            image2 = bundle.getString("image2");
            image3 = bundle.getString("image3");

            // Sets the imageButtons' image to the one matching the saved URIs
            setImage(image1, imageButton1);
            setImage(image2,imageButton2);
            setImage(image3, imageButton3);

            DoP.setText(doP);
            Description.setText(description);
            Make.setText(make);
            Model.setText(model);
            SerialNumber.setText(serialNumber);
            EstimatedValue.setText(estimatedValue);
            Comment.setText(comment);
        }


        // Set click listeners for confirm button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                // Fetch updated items from ViewModel
//                viewModel.fetchItems();
                try{
                    // As the navigation request occurs before the data saving ends, Thread.sleep is needed to ensure that the data is saved before navigating
                    Thread.sleep(200);
                }catch (Exception e){
                    Toast.makeText(getContext(),"Error Occurred", Toast.LENGTH_SHORT).show();
                }
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.navigation_inventory);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the inventory fragment
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.navigation_inventory);
//                Navigation.findNavController(v).popBackStack();
            }
        });
        return view;
    }

    /**
     * This method is used to get live data that is passed from the photo list when an image is chosen
     * @param container This is a ViewGroup object that is used to work with the navigation controller
     */
    private void getChosenImage(ViewGroup container) {
        liveData1 = Navigation.findNavController(container).getCurrentBackStackEntry()
                .getSavedStateHandle().getLiveData("image1");
        liveData1.observe(getViewLifecycleOwner(), new Observer<Photograph>() {
            @Override
            public void onChanged(Photograph photo) {
                setImage(liveData1.getValue().getUri(), imageButton1);
//                imageButton1.setRotation(90);

            }
        });

        liveData2 = Navigation.findNavController(container).getCurrentBackStackEntry()
                .getSavedStateHandle().getLiveData("image2");
        liveData2.observe(getViewLifecycleOwner(), new Observer<Photograph>() {
            @Override
            public void onChanged(Photograph photo) {
                setImage(liveData2.getValue().getUri(), imageButton2);
//                imageButton2.setRotation(90);

            }
        });

        liveData3 = Navigation.findNavController(container).getCurrentBackStackEntry()
                .getSavedStateHandle().getLiveData("image3");
        liveData3.observe(getViewLifecycleOwner(), new Observer<Photograph>() {
            @Override
            public void onChanged(Photograph photo) {
                setImage(liveData3.getValue().getUri(), imageButton3);
//                imageButton3.setRotation(90);

            }
        });
    }

    /**
     * Save edited item data to Database.
     * Used to set the image of a certain ImageButton to the Uri that corresponds to the image chosen in 'Add item' page
     * @param imageUri The Uri of the image as a string
     * @param imageButton Corresponds to a ImageButton object that is passed in
     */
    public void setImage(String imageUri, ImageButton imageButton){
        if (imageUri != null){
            // Glide found in https://stackoverflow.com/questions/52651815/how-to-place-a-firebase-generated-url-into-an-imageview-with-glide/52652077#52652077 and https://www.geeksforgeeks.org/image-loading-caching-library-android-set-2/
            // Glide is a library (https://github.com/bumptech/glide) used to load an image from a Uri
            Glide.with(getContext())
                    .load(imageUri)
                    .into(imageButton);

        }
    }


    /**
     * This method is used to get the updated image
     * @param imageButton  Corresponds to a ImageButton object that is passed in
     * @param imageNumber Corresponds to the number (1-3) of the image
     * @return updatedImage A string which holds either null or the Uri of an image as a string
     */
    public String updateImage(ImageButton imageButton,int imageNumber){
        Drawable updatedDrawable = imageButton.getDrawable();
        String updatedImage = "";

        switch (imageNumber){
                case 1:
                    if (liveData1.getValue() != null){
                        updatedImage = liveData1.getValue().getUri();
                    } else{
                        if(imageButton.getDrawable() != null){
                            updatedImage = image1;
                        }
                    }
                    break;
                case 2:
                    if(liveData2.getValue() != null){
                        updatedImage = liveData2.getValue().getUri();
                    }else{
                        if(imageButton.getDrawable() != null){
                            updatedImage = image2;
                        }
                    }
                    break;
                case 3:
                    if(liveData3.getValue() != null){
                        updatedImage = liveData3.getValue().getUri();
                    }else{
                        if(imageButton.getDrawable() != null) {
                            updatedImage = image3;
                        }
                    }
                    break;
            }
        return updatedImage;
    }
    private void saveData(){
        // Retrieve updated data from UI elements
        String updatedDoP = DoP.getText().toString();
        String updatedDescription = Description.getText().toString();
        String updatedMake = Make.getText().toString();
        String updatedModel = Model.getText().toString();
        String updatedSerialNumber = SerialNumber.getText().toString();
        String updatedEstimatedValue = EstimatedValue.getText().toString();
        String updatedComment = Comment.getText().toString();

        // Get the updated images for all three images
        // As of right now this just focuses on deleting images; Future -> add choosing new image functionality

        String updatedImage1 = updateImage(imageButton1, 1);
        String updatedImage2 = updateImage(imageButton2,2);
        String updatedImage3 = updateImage(imageButton3,3);
        // Query data for the item with the specified description

        Query query = itemsRef.whereEqualTo("description", updatedDescription);

        // Perform the query and update the item details
        query.get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Update item details in Database
                        System.out.println(document.getReference());
                        document.getReference().update("dateOfPurchase", updatedDoP,
                                        "make", updatedMake,
                                        "model", updatedModel,
                                        "serialNumber", updatedSerialNumber,
                                        "estimatedValue", updatedEstimatedValue,
                                        "comment", updatedComment,
                                        "image1",updatedImage1,
                                        "image2", updatedImage2,
                                        "image3", updatedImage3
                                       )
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // Show a Toast message based on the update result

                                        if (task.isSuccessful()) {

                                            Toast.makeText(getActivity(), "Updated successfully!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Update failed, please try again later.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

}
