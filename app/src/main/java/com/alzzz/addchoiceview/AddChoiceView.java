package com.alzzz.addchoiceview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Discription:
 * Created by sz on 2018/7/17.
 */

public class AddChoiceView extends LinearLayout {
    private static final int DEFAULT_CHOICE_CONTENT_LIMIT = 16;
    Context mContext;
    int minChoiceCount;
    int maxChoiceCount;
    int choiceMaxLength;
    OnAddChoiceListener onAddChoiceListener;
    OnFocusChangedListener onFocusChangedListener;
    private boolean delEnable = true;


    public AddChoiceView(Context context) {
        this(context, null);
    }

    public AddChoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddChoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initViews(attrs, defStyleAttr);
    }


    public void setOnAddChoiceListener(OnAddChoiceListener onAddChoiceListener) {
        this.onAddChoiceListener = onAddChoiceListener;
    }

    public void setOnFocusChangedListener(OnFocusChangedListener onFocusChangedListener) {
        this.onFocusChangedListener = onFocusChangedListener;
    }

    private void initViews(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.AddChoiceView, defStyleAttr, 0);
        minChoiceCount = a.getInt(R.styleable.AddChoiceView_min_choice_count, 2);
        maxChoiceCount = a.getInt(R.styleable.AddChoiceView_max_choice_count, 12);
        a.recycle();

        setOrientation(VERTICAL);
        for (int i = 0; i < minChoiceCount; i++) {
            addChoiceItem(i,"");
        }

    }

    /**
     * 添加选项
     */
    public void addChoice(String voteName) {
        addChoiceItem(getChildCount(), voteName);
    }

    /**
     * 添加选项
     *
     * @param index 当前view的index 从0开始
     * @param voteName 投票名称
     */
    private void addChoiceItem(int index, String voteName) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_choice_view, this, false);
        final EditText editText = view.findViewById(R.id.et_choice_content);
        ImageView imageView = view.findViewById(R.id.iv_choice_del);

        if (!TextUtils.isEmpty(voteName)){
            editText.setText(voteName);
        }

        imageView.setTag(view);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View child = (View) v.getTag();
                if (getChildCount() > minChoiceCount) {
                    removeView(child);
                    if (onAddChoiceListener != null) {
                        onAddChoiceListener.delChoice(getChildCount(), maxChoiceCount);
                    }
                } else if (onAddChoiceListener != null) {
                    onAddChoiceListener.onBelowLimit(minChoiceCount);
                }
                resetAllHint();
            }
        });
        setHint(editText, index);
        if (index < maxChoiceCount) {
            addView(view);
            if (onAddChoiceListener != null) {
                onAddChoiceListener.addChoice(getChildCount(), maxChoiceCount);
            }
        } else {
            if (onAddChoiceListener != null) {
                onAddChoiceListener.onBeyondLimit(maxChoiceCount);
            }
            return;
        }

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (onFocusChangedListener != null){
                    onFocusChangedListener.onFocusChange(v, hasFocus, DEFAULT_CHOICE_CONTENT_LIMIT-editText.getText().length());
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (onFocusChangedListener != null){
                    onFocusChangedListener.onNameTextChanged(DEFAULT_CHOICE_CONTENT_LIMIT - editable.length());
                }
            }
        });

        if (!delEnable){
            delEnable = true;
            setDelEnable(delEnable);
        }
    }

    private void resetAllHint() {
        for (int i = 0; i < getChildCount(); i++) {
            EditText editText = getChildAt(i).findViewById(R.id.et_choice_content);
            if (editText != null) {
                setHint(editText, i);
            }
        }
    }

    /**
     * 获取选项列表
     *
     * @return
     */
    public List<String> getChoiceList(){
        List<String> choiceList = new ArrayList<>();
        for (int i=0; i<getChildCount(); i++){
            if (getChildAt(i).findViewById(R.id.et_choice_content) != null){
                choiceList.add(((EditText)getChildAt(i).findViewById(R.id.et_choice_content)).getText().toString());
            }
        }

        return choiceList;
    }

    private void setHint(EditText editText, int index) {
        editText.setHint("选项" + (index + 1));
    }

    /**
     * 最小选择数量
     * @return
     */
    public int getMinChoiceCount() {
        return minChoiceCount;
    }

    /**
     * 最大选择数量
     * @return
     */
    public int getMaxChoiceCount() {
        return maxChoiceCount;
    }

    /**
     * 设置是否可以删除
     */
    public void setDelEnable(boolean delEnable){
        this.delEnable = delEnable;
        for (int i=0; i<getChildCount(); i++){
            View view = getChildAt(i).findViewById(R.id.iv_choice_del);
            if (view != null){
                if (delEnable){
                    view.setVisibility(VISIBLE);
                } else {
                    view.setVisibility(GONE);
                }
            }
        }
    }

    public void setVoteList(List<String> voteList) {
        int i = 0;
        for (String vote: voteList){
            if (getChildAt(i) != null && getChildAt(i).findViewById(R.id.et_choice_content) != null){
                ((EditText) getChildAt(i).findViewById(R.id.et_choice_content)).setText(vote);
            } else {
                addChoice(vote);
            }
            i++;
        }
    }


    public interface OnAddChoiceListener {
        void addChoice(int count, int maxCount);

        void onBeyondLimit(int maxCount);

        void onBelowLimit(int minCount);

        void delChoice(int count, int maxCount);
    }

    public interface OnFocusChangedListener{
        void onFocusChange(View view, boolean focus, int limit);
        void onNameTextChanged(int count);
    }

}
