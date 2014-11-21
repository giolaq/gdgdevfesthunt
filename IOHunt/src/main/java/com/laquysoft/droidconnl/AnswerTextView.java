/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.laquysoft.droidconnl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AnswerTextView extends TextView implements OnTouchListener {

    public int answerNum;
    public Boolean isDown = false;

    public AnswerTextView(Context context) {
        super(context);
        this.setPadding(10, 10, 10, 10);
    }

    public AnswerTextView(Context context, AttributeSet attr) {
        super(context, attr);
        this.setPadding(10, 10, 10, 10);
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        TextView tv = (TextView) v;
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            ((LinearLayout) tv.getParent()).setBackgroundColor(getResources()
                    .getColor(R.color.default_background));
            isDown = true;
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            isDown = false;
        }

        return false;
    }
}
