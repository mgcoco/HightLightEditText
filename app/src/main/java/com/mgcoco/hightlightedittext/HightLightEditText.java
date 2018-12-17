package com.mgcoco.hightlightedittext;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class HightLightEditText extends LinearLayout {

    private static final boolean DEF_IS_NECESSARY_COLUMN = false;

    private TextView mHightLightTextView;

    private View mEditMask;

    private EditText mEditText;

    private ImageView mIcon;

    private boolean mSingleLineEllipsis;

    private int mHightlightColor;

    private int mHightlightTextSize;

    private String mHightlightText;

    private boolean mHightlightFixed;

    private int mTextColor;

    private int mTextSize;

    private Drawable mTextBackground;

    private String mText;

    private int mInputType;

    private int mImeOptions;

    private boolean mIsNecessary;

    private boolean mIsFocusable;

    private int mLines;

    private Drawable mResIcon;

    private String mHint;

    private int mFocusColor;

    private int mHintTextColor;

    public HightLightEditText(Context context) {
        super(context);
        init(context, null);
    }

    public HightLightEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HightLightEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View v = inflate(getContext(), R.layout.widget_mcs_edittext, this);
        mEditText = v.findViewById(R.id.mcs_edittext);
        mHightLightTextView = v.findViewById(R.id.mcs_hightlight_text);
        mEditMask = v.findViewById(R.id.mcs_edittext_msk);
        mIcon = v.findViewById(R.id.mcs_icon);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HightLightEditText);

        mSingleLineEllipsis = typedArray.getBoolean(R.styleable.HightLightEditText_mcs_singleLineEllipsis, false);
        mHightlightColor = typedArray.getColor(R.styleable.HightLightEditText_mcs_hightlight_text_color, Color.WHITE);
        mHightlightTextSize = typedArray.getInteger(R.styleable.HightLightEditText_mcs_hightlight_text_size, 15);
        mHightlightText = typedArray.getString(R.styleable.HightLightEditText_mcs_hightlight_text);
        mHightlightFixed = typedArray.getBoolean(R.styleable.HightLightEditText_mcs_hightlight_fixed, false);

        mIsFocusable = typedArray.getBoolean(R.styleable.HightLightEditText_mcs_focusable, true);

        mTextColor = typedArray.getColor(R.styleable.HightLightEditText_mcs_text_color, Color.WHITE);
        mTextSize = typedArray.getInteger(R.styleable.HightLightEditText_mcs_text_size, 18);
        mText = typedArray.getString(R.styleable.HightLightEditText_mcs_text);
        mTextBackground = typedArray.getDrawable(R.styleable.HightLightEditText_mcs_text_background);

        mHintTextColor = typedArray.getColor(R.styleable.HightLightEditText_mcs_hint_text_color, Color.GRAY);

        mFocusColor = typedArray.getColor(R.styleable.HightLightEditText_mcs_focus_color, mTextColor);

        mResIcon = typedArray.getDrawable(R.styleable.HightLightEditText_mcs_icon);

        mIsNecessary = typedArray.getBoolean(R.styleable.HightLightEditText_mcs_is_necessary, DEF_IS_NECESSARY_COLUMN);

        mInputType = EditorInfo.TYPE_CLASS_TEXT;
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            if(attr == R.styleable.HightLightEditText_android_inputType){
                mInputType = typedArray.getInt(attr, EditorInfo.TYPE_CLASS_TEXT);
            }
            else if(attr == R.styleable.HightLightEditText_android_imeOptions){
                mImeOptions = typedArray.getInt(attr, 0);
            }
            else if(attr == R.styleable.HightLightEditText_android_lines){
                mLines = typedArray.getInt(attr, 1);
            }
            else if(attr == R.styleable.HightLightEditText_android_hint){
                mHint = typedArray.getString(attr);
            }
        }
        typedArray.recycle();

        initViewParam();
    }

    public void setInputType(int inputType){
        mInputType = inputType;
        mEditText.setInputType(inputType);
    }

    public void setEditFocusable(boolean focusable){
        mIsFocusable = focusable;
        mEditText.setFocusable(focusable);
        mEditText.setEnabled(focusable);
        mEditText.setFocusableInTouchMode(focusable);

        if(!mIsFocusable) {
            mEditMask.setVisibility(VISIBLE);
            mEditMask.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    performClick();
                }
            });
        }
        else{
            mEditMask.setVisibility(GONE);
        }
    }

    @Override
    public void setSelected(boolean isSelected){
        super.setSelected(isSelected);
        mEditText.setSelected(isSelected);
    }

    public void setNecessary(boolean isNecessary){
        mIsNecessary = isNecessary;
        findViewById(R.id.mcs_necessary).setVisibility(mIsNecessary ? VISIBLE : GONE);
    }

    private void initViewParam(){
        mHightLightTextView.setText(mHightlightText);
        mHightLightTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mHightlightTextSize);
        mHightLightTextView.setTextColor(mHightlightColor);

        mEditText.setText(mText);
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTextSize);
        mEditText.setTextColor(mTextColor);
        mEditText.setHintTextColor(mHintTextColor);

        mEditText.setSingleLine(mSingleLineEllipsis);
        mEditText.setInputType(mInputType);
        mEditText.setImeOptions(mImeOptions);
        mEditText.setHint(mHint);

        if(mHint != null && mHint.length() > 0){
            mEditText.setHint(mHint);
        }
        else if(!mHightlightFixed){
            mEditText.setHint(mHightlightText);
        }

        if(mTextBackground != null) {
            mEditText.setBackground(mTextBackground);
            mEditText.setBackgroundTintList(null);
        }
        else{
            int[][] states = new int[][] {
                new int[] { android.R.attr.state_focused},
                new int[] { -android.R.attr.state_focused},
            };
            ColorStateList colorStateList = new ColorStateList(states, new int[]{mFocusColor, mHightlightColor});
            mEditText.setBackgroundTintList(colorStateList);
        }

        if(mHightlightFixed){
            mHightLightTextView.setVisibility(VISIBLE);
        }
        else{
            if(mEditText.getText() == null || mEditText.getText().length() == 0)
                mHightLightTextView.setVisibility(INVISIBLE);
            else
                mHightLightTextView.setVisibility(VISIBLE);
        }

        mIcon.getLayoutParams().width = (int)convertDpToPixel(mTextSize, getContext());
        mIcon.getLayoutParams().height = (int)convertDpToPixel(mTextSize, getContext());

        if(mResIcon != null)
            mIcon.setImageDrawable(mResIcon);
        else
            mIcon.setVisibility(GONE);

        if(!mIsFocusable) {
            mEditText.setEnabled(false);
            mEditMask.setVisibility(VISIBLE);
            mEditMask.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    performClick();
                }
            });
        }
        if(mLines > 1) {
            mEditText.setGravity(Gravity.LEFT|Gravity.TOP);
            mEditText.setLines(mLines);
        }

        ((TextView)findViewById(R.id.mcs_necessary)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, mHightlightTextSize);
        findViewById(R.id.mcs_necessary).setVisibility(mIsNecessary ? VISIBLE : GONE);

        setCursorColor(mFocusColor);

        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus) {
                    mHightLightTextView.setTextColor(mFocusColor);
                }
                else{
                    mHightLightTextView.setTextColor(mHightlightColor);
                }
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!mHightlightFixed && s.length() == 0)
                    mHightLightTextView.setVisibility(INVISIBLE);
                else
                    mHightLightTextView.setVisibility(VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setCursorColor(int color) {
        try {
            // Get the cursor resource id
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            int drawableResId = field.getInt(mEditText);

            // Get the editor
            field = TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            Object editor = field.get(mEditText);

            // Get the drawable and set a color filter
            Drawable drawable = getContext().getResources().getDrawable(drawableResId, null);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            Drawable[] drawables = {drawable, drawable};

            // Set the drawables
            field = editor.getClass().getDeclaredField("mCursorDrawable");
            field.setAccessible(true);
            field.set(editor, drawables);
        } catch (Exception ignored) {
        }
    }

    public String getHightLightText(){
        return mHightlightText;
    }

    public boolean isNecessary(){
        return mIsNecessary;
    }

    public String getText(){
        if(mEditText.getText() != null)
            return mEditText.getText().toString();
        return null;
    }

    public void setText(String text){
        mEditText.setText(text);
    }

    public void setOnIconClickListener(OnClickListener listener){
        mIcon.setOnClickListener(listener);
    }
    
    public EditText getEditText(){
        return mEditText;
    }

    public TextView getHightLightTextView(){
        return mHightLightTextView;
    }

    public ImageView getIcon() {
        return mIcon;
    }

    public static String[] checkAllNecessaryField(View parent){
        ArrayList<HightLightEditText> necessaryField = new ArrayList<>();
        if(parent != null && parent instanceof ViewGroup){
            ViewGroup root = (ViewGroup)parent;
            appendMcsEditText(necessaryField, root, true);
        }
        else if(parent instanceof HightLightEditText && parent.getVisibility() == VISIBLE){
            if(((HightLightEditText)parent).isNecessary() && (((HightLightEditText)parent).getText() == null || ((HightLightEditText)parent).getText().length() == 0)){
                necessaryField.add((HightLightEditText)parent);
            }
        }
        ArrayList<String> field = new ArrayList<>();
        for(HightLightEditText child:necessaryField){
            field.add(child.getHightLightText());
        }

        return field.toArray(new String[field.size()]);
    }

    public static ArrayList<HightLightEditText> getAllHightlightEditText(View parent){
        ArrayList<HightLightEditText> list = new ArrayList<>();
        if(parent != null && parent instanceof ViewGroup){
            ViewGroup root = (ViewGroup)parent;
            appendMcsEditText(list, root, false);
        }
        else if(parent instanceof HightLightEditText && parent.getVisibility() == VISIBLE){
            if(((HightLightEditText)parent).isNecessary() && (((HightLightEditText)parent).getText() == null || ((HightLightEditText)parent).getText().length() == 0)){
                list.add((HightLightEditText)parent);
            }
        }

        return list;
    }

    private static void appendMcsEditText(ArrayList<HightLightEditText> necessaryField, ViewGroup root, boolean appendNecessary){
        for(int i = 0; i < root.getChildCount(); i++){
            View child = root.getChildAt(i);
            if(child instanceof HightLightEditText && child.getVisibility() == VISIBLE){
                if(appendNecessary) {
                    if (((HightLightEditText) child).isNecessary() && (((HightLightEditText) child).getText() == null || ((HightLightEditText) child).getText().length() == 0)) {
                        necessaryField.add((HightLightEditText) child);
                    }
                }
                else{
                    necessaryField.add((HightLightEditText) child);
                }
            }
            else if(child instanceof ViewGroup){
                appendMcsEditText(necessaryField, (ViewGroup) child, appendNecessary);
            }
        }
    }

    public static View findViewParent(View root){
        if(root.getParent() instanceof ViewGroup)
            return findViewParent((View)root.getParent());
        return root;
    }

    public static float convertDpToPixel(float dp, Context context){
        float px = dp * getDensity(context);
        return px;
    }

    public static float getDensity(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density;
    }
}
