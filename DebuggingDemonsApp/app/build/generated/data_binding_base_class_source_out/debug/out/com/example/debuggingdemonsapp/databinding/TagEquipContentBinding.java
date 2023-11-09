// Generated by view binder compiler. Do not edit!
package com.example.debuggingdemonsapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.debuggingdemonsapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class TagEquipContentBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final CheckBox tagCheckbox;

  @NonNull
  public final TextView tagName;

  private TagEquipContentBinding(@NonNull ConstraintLayout rootView, @NonNull CheckBox tagCheckbox,
      @NonNull TextView tagName) {
    this.rootView = rootView;
    this.tagCheckbox = tagCheckbox;
    this.tagName = tagName;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static TagEquipContentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static TagEquipContentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.tag_equip_content, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static TagEquipContentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.tag_checkbox;
      CheckBox tagCheckbox = ViewBindings.findChildViewById(rootView, id);
      if (tagCheckbox == null) {
        break missingId;
      }

      id = R.id.tag_name;
      TextView tagName = ViewBindings.findChildViewById(rootView, id);
      if (tagName == null) {
        break missingId;
      }

      return new TagEquipContentBinding((ConstraintLayout) rootView, tagCheckbox, tagName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
