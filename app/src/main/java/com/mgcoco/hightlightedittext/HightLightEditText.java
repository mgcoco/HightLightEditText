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

    private TextView mHightlightTextView;

    private View mEditMask;

    private EditText mEditText;

    private ImageView mIcon;

    private boolean mSingleLineEllipsis;

    private int mHightlightTextColor;

    private int mHightlightTextSize;

    private CharSequence mHightlightText;

    private boolean mHightlightFixed;

    private int mTextColor;

    private int mTextSize;

    private Drawable mTextBackground;

    private CharSequence mText;

    private int mInputType;

    private int mImeOptions;

    private boolean mIsNecessary;

    private boolean mIsFocusable;

    private int mLines;

    private Drawable mResIcon;

    private int mResIconColor;

    private CharSequence mHint;

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
        mHightlightTextView = v.findViewById(R.id.mcs_hightlight_text);
        mEditMask = v.findViewById(R.id.mcs_edittext_msk);
        mIcon = v.findViewById(R.id.mcs_icon);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HightLightEditText);

        mSingleLineEllipsis = ta.getBoolean(R.styleable.HightLightEditText_mcs_singleLineEllipsis, false);
        mHightlightTextColor = ta.getColor(R.styleable.HightLightEditText_mcs_hightlight_text_color, Color.WHITE);
        mHightlightTextSize = ta.getDimensionPixelSize(R.styleable.HightLightEditText_mcs_hightlight_text_size, 15);
        mHightlightText = ta.getString(R.styleable.HightLightEditText_mcs_hightlight_text);
        mHightlightFixed = ta.getBoolean(R.styleable.HightLightEditText_mcs_hightlight_fixed, false);

        mIsFocusable = ta.getBoolean(R.styleable.HightLightEditText_mcs_focusable, true);

        mTextColor = ta.getColor(R.styleable.HightLightEditText_mcs_text_color, Color.WHITE);
        mTextSize = ta.getDimensionPixelSize(R.styleable.HightLightEditText_mcs_text_size, 18);
        mText = ta.getString(R.styleable.HightLightEditText_mcs_text);
        mTextBackground = ta.getDrawable(R.styleable.HightLightEditText_mcs_text_background);

        mHintTextColor = ta.getColor(R.styleable.HightLightEditText_mcs_hint_text_color, Color.GRAY);

        mFocusColor = ta.getColor(R.styleable.HightLightEditText_mcs_focus_color, mTextColor);

        mResIcon = ta.getDrawable(R.styleable.HightLightEditText_mcs_icon);
        mResIconColor = ta.getColor(R.styleable.HightLightEditText_mcs_icon_tint_color, Color.WHITE);

        mIsNecessary = ta.getBoolean(R.styleable.HightLightEditText_mcs_is_necessary, DEF_IS_NECESSARY_COLUMN);

        mInputType = EditorInfo.TYPE_CLASS_TEXT;
        int n = ta.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = ta.getIndex(i);
            if(attr == R.styleable.HightLightEditText_android_inputType){
                mInputType = ta.getInt(attr, EditorInfo.TYPE_CLASS_TEXT);
            }
            else if(attr == R.styleable.HightLightEditText_android_imeOptions){
                mImeOptions = ta.getInt(attr, 0);
            }
            else if(attr == R.styleable.HightLightEditText_android_lines){
                mLines = ta.getInt(attr, 1);
            }
            else if(attr == R.styleable.HightLightEditText_android_hint){
                mHint = ta.getString(attr);
            }
        }
        ta.recycle();

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
        setHightlightText(mHightlightText);
        setHightlightTextSize(mHightlightTextSize);
        setHightlightTextColor(mHightlightTextColor);

        setText(mText);
        setTextSize(mTextSize);
        setTextColor(mTextColor);

        setHintTextColor(mHintTextColor);

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
            ColorStateList colorStateList = new ColorStateList(states, new int[]{mFocusColor, mHightlightTextColor});
            mEditText.setBackgroundTintList(colorStateList);
        }

        setHightlightFixed(mHightlightFixed);

        mIcon.getLayoutParams().width = (int)convertDpToPixel(mTextSize, getContext());
        mIcon.getLayoutParams().height = (int)convertDpToPixel(mTextSize, getContext());

        if(mResIcon != null) {
            mIcon.setImageDrawable(mResIcon);
            setIconTintColor(mResIconColor);
        }
        else
            mIcon.setVisibility(GONE);

        setEditFocusable(mIsFocusable);

        if(mLines > 1) {
            mEditText.setGravity(Gravity.LEFT|Gravity.TOP);
            mEditText.setLines(mLines);
        }

        ((TextView)findViewById(R.id.mcs_necessary)).setTextSize(TypedValue.COMPLEX_UNIT_PX, mHightlightTextSize);
        findViewById(R.id.mcs_necessary).setVisibility(mIsNecessary ? VISIBLE : GONE);

        setCursorColor(mFocusColor);

        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus) {
                    mHightlightTextView.setTextColor(mFocusColor);
                }
                else{
                    mHightlightTextView.setTextColor(mHightlightTextColor);
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
                    mHightlightTextView.setVisibility(INVISIBLE);
                else
                    mHightlightTextView.setVisibility(VISIBLE);
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

    public CharSequence getHightLightText(){
        return mHightlightTextView.getText();
    }

    public boolean isNecessary(){
        return mIsNecessary;
    }

    public CharSequence getText(){
        return mEditText.getText();
    }

    public void setText(String text){
        mEditText.setText(text);
    }

    public void setOnIconClickListener(OnClickListener listener){
        mIcon.setOnClickListener(listener);
    }

    public void setHightlightTextColor(int color){
        mHightlightTextColor = color;
        mHightlightTextView.setTextColor(mHightlightTextColor);
    }

    public void setHightlightTextSize(int size){
        mHightlightTextSize = size;
        mHightlightTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setHightlightFixed(boolean isFixed){
        mHightlightFixed = isFixed;
        if(mHightlightFixed){
            mHightlightTextView.setVisibility(VISIBLE);
        }
        else{
            if(mEditText.getText() == null || mEditText.getText().length() == 0)
                mHightlightTextView.setVisibility(INVISIBLE);
            else
                mHightlightTextView.setVisibility(VISIBLE);
        }
    }

    public void setHightlightText(CharSequence text){
        mHightlightText = text;
        mHightlightTextView.setText(text);
    }

    public void setTextColor(int color){
        mTextColor = color;
        mEditText.setTextColor(mTextColor);
    }

    public void setTextSize(int size){
        mTextSize = size;
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setText(CharSequence text){
        mText = text;
        mEditText.setText(mText);
    }

    public void setBackground(Drawable drawable){
        mTextBackground = drawable;
        mEditText.setBackground(drawable);
    }

    public void setBackground(int drawable){
        mTextBackground = getResources().getDrawable(drawable, null);
        mEditText.setBackground(mTextBackground);
    }

    public void setHintTextColor(int color){
        mHintTextColor = color;
        mEditText.setHintTextColor(mHightlightTextColor);
    }

    public void setHint(CharSequence hint){
        mHint = hint;
        mEditText.setHint(hint);
    }

    public void setIconDrawable(int drawable){
        mResIcon = getResources().getDrawable(drawable, null);
        mIcon.setImageDrawable(mResIcon);
    }

    public void setIconTintColor(int color){
        mResIconColor = color;
        int[][] states = new int[][] {
                new int[] { -android.R.attr.state_focused},
        };
        ColorStateList colorStateList = new ColorStateList(states, new int[]{mResIconColor});
        mIcon.setImageTintList(colorStateList);
    }

    public EditText getEditText(){
        return mEditText;
    }

    public TextView getHightLightTextView(){
        return mHightlightTextView;
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
            field.add(child.getHightLightText() + "");
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
