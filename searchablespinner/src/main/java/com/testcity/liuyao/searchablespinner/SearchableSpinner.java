package com.testcity.liuyao.searchablespinner;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.security.InvalidParameterException;
import java.util.List;

import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealFrameLayout;

public class SearchableSpinner extends RelativeLayout {
    private RevealFrameLayout revealFrameLayout;
    private RelativeLayout normal_status;
    private RelativeLayout drop_status;
    private ImageView normal_status_arrow;
    private ImageView drop_status_arrow;
    private MyAutoCompleteTextView spinnerEdit;
    private TextView spinnerText;
    private String defaultTextColorString = "#a6000000";
    private int spinnerTextColor = Color.parseColor(defaultTextColorString);
    private String defaultHintColorString = "#40000000";
    private int spinnerHintColor = Color.parseColor(defaultHintColorString);
    private int textSizeSp = 15;
    private Drawable normalStatusBackground = getResources().getDrawable(R.drawable.shape_my_spinner_normal_status);
    private Drawable dropStatusBackground = getResources().getDrawable(R.drawable.shape_my_spinner_drop_status);
    private  boolean alreadyLoad = false;
    private String hintText = "";
    //-1表示没有选择任何下拉列表
    private int currentSelectedIndex = -1;
    private String currentSelectedString;

    private List<String> allItems;
    //隐藏输入框的时候，焦点会自动被其他组件获取，感觉不自然，这个用一个看不见的edittext来接管焦点，其他部分的组件不会突然获取焦点
    private EditText rob_focus;
    ViewState viewState;

    public enum ViewState{
        //DropState是可以选择下拉列表或者可以输入文字的那个状态
        //NormalState是非编辑的那种状态
        DropState, NormalState
    }

    public SearchableSpinner(Context context) {
        super(context);
        init(context, null);
    }

    public SearchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        LayoutInflater.from(getContext()).inflate(R.layout.layout_searchable_spinner, this, true);
        revealFrameLayout = findViewById(R.id.revealFrameLayout);
        normal_status = findViewById(R.id.normal_status);
        drop_status = findViewById(R.id.drop_status);
        normal_status_arrow = findViewById(R.id.normal_status_arrow);
        drop_status_arrow = findViewById(R.id.drop_status_arrow);
        spinnerEdit = findViewById(R.id.spinnerEdit);
        spinnerText = findViewById(R.id.spinnerText);
        rob_focus = findViewById(R.id.rob_focus);
        drop_status_arrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormalStatus(true, true);
            }
        });
        normal_status.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alreadyLoad){
                    showDropStatus(true);
                }

            }
        });

        if(attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchableSpinner);
            if (typedArray != null){
                String hintTextTemp = typedArray.getString(R.styleable.SearchableSpinner_SearchableSpinner_HintText);
                if(!TextUtils.isEmpty(hintTextTemp)){
                    hintText = hintTextTemp;
                }
                spinnerTextColor = typedArray.getColor(R.styleable.SearchableSpinner_SearchableSpinner_TextColor, Color.parseColor(defaultTextColorString));
                int textSizePx = typedArray.getDimensionPixelSize(R.styleable.SearchableSpinner_SearchableSpinner_TextSize, DensityUtil.sp2px(getContext(), 15));
                textSizeSp = DensityUtil.px2Sp(getContext(), textSizePx);
                Drawable normalStatusBackgroundTemp = typedArray.getDrawable(R.styleable.SearchableSpinner_SearchableSpinner_Normal_Status_Background);
                if(normalStatusBackgroundTemp != null){
                    normalStatusBackground = normalStatusBackgroundTemp;
                }
                Drawable dropStatusBackgroundTemp = typedArray.getDrawable(R.styleable.SearchableSpinner_SearchableSpinner_Drop_Status_Background);
                if(dropStatusBackgroundTemp != null){
                    dropStatusBackground = dropStatusBackgroundTemp;
                }
            }
        }
        normal_status.setBackground(normalStatusBackground);
        drop_status.setBackground(dropStatusBackground);
        spinnerEdit.setTextSize(textSizeSp);
        spinnerText.setTextSize(textSizeSp);
        spinnerEdit.setTextColor(spinnerTextColor);

        showNormalStatus(false, true);

    }

    /**
     *
     * @param animation
     * @param clear 是否清空之前选择的选项
     */
    public void showNormalStatus(boolean animation, boolean clear){
        if(clear){
            clearSelected();
        }
        if(currentSelectedIndex != -1){
            setSpinnerText(currentSelectedString);
        }else{
            setSpinnerHint(hintText);
        }
        if(spinnerEdit.isFocused()){
            rob_focus.requestFocus();
        }
        if(animation){
            showNormalStatusAnimation();
        }else{
            drop_status.setVisibility(INVISIBLE);
            normal_status.setVisibility(VISIBLE);
        }

        viewState = ViewState.NormalState;
        hideInput();
    }

    /**
     * 清空已经选择的
     */
    public void clearSelected(){
        this.currentSelectedIndex = -1;
        this.currentSelectedString = null;
        setSpinnerHint(hintText);
    }

    /**
     * 编程式选择item
     * @param index
     */
    public void programSelect(int index){
         if(alreadyLoad){
             this.currentSelectedIndex = index;
             this.currentSelectedString = allItems.get(index);
             showNormalStatus(false, false);
         }else {
             throw new RuntimeException("还没有设置items");
         }
    }

    public void showDropStatus(boolean animation){
        spinnerEdit.setText("");
        if(animation){
            showDropStatusAnimation();
        }else{
            drop_status.setVisibility(VISIBLE);
            normal_status.setVisibility(INVISIBLE);
        }
        viewState = ViewState.DropState;
        spinnerEdit.requestFocus();
        spinnerEdit.performFiltering("");
        spinnerEdit.showDropDown();
    }

    public boolean isNormalState(){
        if(ViewState.NormalState.equals(viewState)){
            return true;
        }else{
            return false;
        }
    }

    public void setSpinnerText(String text){
        spinnerText.setText(text);
        spinnerText.setTextColor(spinnerTextColor);
    }

    public  void setSpinnerHint(String text){
        spinnerText.setText(text);
        spinnerText.setTextColor(spinnerHintColor);
    }


    public void hideInput(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
    }


    private  void showNormalStatusAnimation(){
        // get the center for the clipping circle
        int cx = drop_status.getLeft();
        int cy = (drop_status.getTop() + drop_status.getBottom()) / 2;
        float finalRadius = Math.max(this.getWidth(), this.getHeight());

        // Android native animator
        Animator animator =
                ViewAnimationUtils.createCircularReveal(drop_status, cx, cy, finalRadius, 0);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(150);
        animator.addListener(new SimpleAnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                normal_status.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                drop_status.setVisibility(INVISIBLE);
            }
        });
        animator.start();
    }

    private  void showDropStatusAnimation(){
        // get the center for the clipping circle
        int cx = drop_status.getRight();
        int cy = (drop_status.getTop() + drop_status.getBottom()) / 2;

        float finalRadius = Math.max(this.getWidth(), this.getHeight());

        // Android native animator
        Animator animator =
                ViewAnimationUtils.createCircularReveal(drop_status, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(150);
        animator.addListener(new SimpleAnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                drop_status.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                normal_status.setVisibility(GONE);
            }
        });
        animator.start();
    }



    public void setListItem(List<String> items){
        if(items == null || items.size() == 0){
            throw new InvalidParameterException("不能设空");
        }
        this.allItems = items;
        alreadyLoad = true;
        //其实设置0也会被设置成1
        spinnerEdit.setThreshold(0);
        spinnerEdit.setAdapter(new AutoTextViewAdapter(getContext(), items));
        spinnerEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //记录选择的字符
                SearchableSpinner.this.currentSelectedString = spinnerEdit.getText().toString();
                SearchableSpinner.this.currentSelectedIndex = indexOfItems(SearchableSpinner.this.currentSelectedString);
                spinnerText.setText(spinnerEdit.getText());
                spinnerEdit.setText("");
                showNormalStatus(true, false);
            }
        });

        //调整下下拉dialog与textview的垂直距离
        post(new Runnable() {
            @Override
            public void run() {
                float offset = (getMeasuredHeight() - spinnerEdit.getMeasuredHeight()) / 2f;
                spinnerEdit.setDropDownVerticalOffset((int) offset);
            }
        });
        showNormalStatus(false, true);
    }

    public int indexOfItems(String item){
          return allItems.indexOf(item);
    }
    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    public int getCurrentSelectedIndex() {
        if(!isNormalState()){
            throw new RuntimeException("current state is DropState");
        }
        return currentSelectedIndex;
    }

    private void setCurrentSelectedIndex(int currentSelectedIndex) {
        this.currentSelectedIndex = currentSelectedIndex;
    }

    public String getCurrentSelectedString() {
        if(!isNormalState()){
            throw new RuntimeException("current state is DropState");
        }
        return currentSelectedString;
    }

    private void setCurrentSelectedString(String currentSelectedString) {
        this.currentSelectedString = currentSelectedString;
    }

    public void setSpinnerTextSize(int sp){
        textSizeSp = sp;
        spinnerText.setTextSize(textSizeSp);
        spinnerEdit.setTextSize(textSizeSp);
    }

    public void setSpinnerNormalStatusBackground(Drawable normalStatusBackground){
        this.normalStatusBackground = normalStatusBackground;
        normal_status.setBackground(normalStatusBackground);
    }

    public void setSpinnerDropStatusBackground(Drawable dropStatusBackground){
        this.dropStatusBackground = dropStatusBackground;
        drop_status.setBackground(dropStatusBackground);
    }

    public void setSpinnerHintText(String hintText){
        this.hintText = hintText;
    }
}
