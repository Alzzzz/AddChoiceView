package com.alzzz.addchoiceview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Discription:
 * Created by sz on 2018/7/17.
 */

public class AddChoiceView extends LinearLayout {
    Context mContext;
    int choiceCount;
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

    private void initViews(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.AddChoiceView, defStyleAttr, 0);
        this.choiceCount = a.getInt(R.styleable.AddChoiceView_min_choice_count, 3);
        a.recycle();

        setOrientation(VERTICAL);
        for (int i=0; i<choiceCount; i++){
            addChoiceItem(i);
        }

    }

    /**
     * 添加选项
     */
    public void addChoice(){
        addChoiceItem(getChildCount());
    }

    private void addChoiceItem(int index) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_choice_view, this, false);
        EditText editText = view.findViewById(R.id.et_choice_content);
        ImageView imageView = view.findViewById(R.id.iv_choice_del);

        imageView.setTag(view);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View child = (View) v.getTag();
                removeView(child);
                resetAllHint();
            }
        });
        setHint(editText, index);
        addView(view);
    }

    private void resetAllHint() {
        for (int i=0; i<getChildCount(); i++){
            EditText editText = getChildAt(i).findViewById(R.id.et_choice_content);
            if (editText != null){
                setHint(editText, i);
            }
        }
    }

    private void setHint(EditText editText, int index) {
        editText.setHint("选项"+(index+1));
    }

}
