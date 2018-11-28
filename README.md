
# # HightLightEditText  
  
## Demo  
  
- Support inputType, imeOptions, lines, hint etc. attribute.  
- Automatically mark the required fields.  
- Check all required field HightLightEditText in viewGroup and return string array of hight light text.  
  
<img src="https://github.com/mgcoco/HightLightEditText/blob/master/screenshot/1.jpg" width="250px" />  
<img src="https://github.com/mgcoco/HightLightEditText/blob/master/screenshot/2.jpg" width="250px" />  
<img src="https://github.com/mgcoco/HightLightEditText/blob/master/screenshot/3.jpg" width="250px" />  
  
## Gradle Dependency  
  
```  
allprojects {  
    repositories {  
      ...  
      maven { url 'https://jitpack.io' }  
    }  
}  
  
dependencies {  
   implementation 'com.github.mgcoco:HightLightEditText:v1.0.12'
}  
```  
  
## Basic  
  
Xml File  
  
```  
<com.mgcoco.hightlightedittext.HightLightEditText    
   android:id="@+id/password"    
   android:layout_width="match_parent"    
   android:layout_height="wrap_content"  
   android:hint="Password"    
   android:inputType="textPassword"    
   app:mcs_hightlight_text="Password"    
   app:mcs_hightlight_text_color="@android:color/holo_green_dark"  
   //If fixed is true the HightlightTextView will always be shown
   app:mcs_hightlight_fixed="true"
   //Hight light text color, under line color and cursor color will be the same color  
   //when Eidttext is focused.  
   app:mcs_focus_color="@android:color/holo_blue_dark"    
   //Icon drawable of right button.  
   app:mcs_icon="@drawable/icon_visible"    
   //Single line    
   app:mcs_singleLineEllipsis="true"   
   //Edittext text color  
   app:mcs_text_color="@android:color/black"  
   //Edittext background shoudld be a drawable resource
   app:mcs_text_background="@drawable/background"  
   //true/false if this field is reuqired  
   app:mcs_is_necessary="true"  
   //If focusable value is false, this field will never be focused.   
   //Only setText programmatically can change the text of Edittext.  
   app:mcs_focusable="false"/>  
```  
  
Java Code  
  
```  
final HightLightEditText password = findViewById(R.id.password);  
password.setOnIconClickListener(new View.OnClickListener() {    
     @Override    
     public void onClick(View v) {    
        if(v.isSelected()){    
          ((ImageView)v).setImageResource(R.drawable.icon_visible);    
        }    
        else{    
          ((ImageView)v).setImageResource(R.drawable.icon_invisible);    
        }  
        v.setSelected(!v.isSelected());    
     }    
});  
  
//Customize attribue  
EditText editText = password.getEditText();  
TextView textView = password.getHightLightTextView();  
ImageView icon = password.getIcon();  
  
//If the filed is required and textValue is empty. It will be returned in this array.  
//This function will check all required field in this view.  
String[] requiredField = HightLightEditText.checkAllNecessaryField(view);